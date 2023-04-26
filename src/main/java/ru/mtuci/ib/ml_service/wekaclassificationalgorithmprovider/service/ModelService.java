package ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.algorithms.AlgorithmDetails;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.algorithms.Impl.J48Impl;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.algorithms.Impl.NaiveBayesImpl;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.algorithms.Impl.RandomForestImpl;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.algorithms.interfaces.MachineAlgorithms;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.enums.Algorithm;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.exception.IncorrectFeatureLengthException;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.exception.InvalidAlgorithmException;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.exception.MissingAttributeException;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.exception.NumberLabelsException;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.model.SerializedModelData;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.requestes.ModelRequest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Getter
@RequiredArgsConstructor
public class ModelService {
    private final StreamBridge streamBridge;

    @Value("${customErrorTopic.topic}")
    private String errorTopic;

    private MachineAlgorithms classifier;


    public static List<AlgorithmDetails> getClassifiers() {
        return Arrays.stream(Algorithm.values()).map(algorithm -> new AlgorithmDetails(algorithm.getAlgorithmName(), algorithm.getHyperparameters())).collect(Collectors.toList());
    }


    public ArrayList<Attribute> createAtr(List<List<Object>> features, List<Object> labels) throws Exception {

        ArrayList<Attribute> attributes = new ArrayList<>();
        int featureLength = features.get(0).size();

        if (features.size() != labels.size()) {
            throw new NumberLabelsException("Number of labels does not match the number of feature sets");
        }


        for (List<Object> feature : features) {
            if (feature.size() != featureLength) {
                throw new IncorrectFeatureLengthException("Incorrect feature length");
            }
        }
        for (int i = 0; i < features.get(0).size(); i++) {
            Attribute attribute;
            if (features.get(0).get(i) != null) {
                attribute = new Attribute("attr" + i);
            } else {
                throw new MissingAttributeException("Missing attribute");
            }
            attributes.add(attribute);
        }
        // Create a set of unique labels
        Set<String> uniqueLabels = labels.stream().map(Object::toString).collect(Collectors.toSet());
        ArrayList<String> labelList = new ArrayList<>(uniqueLabels);
        Attribute classAttribute = new Attribute("class", labelList);
        attributes.add(classAttribute);

        return attributes;

    }

    public ModelRequest train(ModelRequest trainRequest) throws Exception {


        byte[] modelBytes = Base64.getDecoder().decode(trainRequest.getSerializedModelData().getModel());
        MachineAlgorithms deserializedModel = deserializeModel(modelBytes);
        ArrayList<Attribute> attributes = createAtr(trainRequest.getFeatures(), trainRequest.getLabels());

        deserializedModel.buildClassifier("train", attributes, trainRequest.getFeatures().size(), trainRequest.getFeatures(), trainRequest.getLabels());
        byte[] modelTrainBytes = serializeModel(deserializedModel);
        byte[] attrBytes = serializeAttributes(attributes);

        // Кодирование в Base64
        String modelBase64 = Base64.getEncoder().encodeToString(modelTrainBytes);
        String attrBase64 = Base64.getEncoder().encodeToString(attrBytes);
        SerializedModelData serializedModelData = SerializedModelData.builder()
                .modelId(trainRequest.getModelId())
                .model(modelBase64)
                .attribute(attrBase64)
                .build();
        return ModelRequest.builder().serializedModelData(serializedModelData).build();


    }

    public ModelRequest predict(ModelRequest request) throws Exception {
        // Декодирование и десериализация модели и атрибутов из запроса

        byte[] modelBytes = Base64.getDecoder().decode(request.getSerializedModelData().getModel());
        byte[] attrBytes = Base64.getDecoder().decode(request.getSerializedModelData().getAttribute());
        MachineAlgorithms deserializedModel = deserializeModel(modelBytes);
        ArrayList<Attribute> deserializedAttributes = deserializeAttributes(attrBytes);
        for (List<Object> featureRow : request.getFeatures()) {
            if (featureRow.size() != deserializedAttributes.size() - 1) {
                throw new IncorrectFeatureLengthException("Incorrect feature length");
            }
        }


        Instances instances = new Instances("predict", deserializedAttributes, request.getFeatures().size());
        instances.setClassIndex(deserializedAttributes.size() - 1);
        List<String> predictions = new ArrayList<>();
        List<Map<String, Double>> distributionsList = new ArrayList<>();

        for (int i = 0; i < request.getFeatures().size(); i++) {
            Instance instance = createInstance(request.getFeatures().get(i), deserializedAttributes, instances);
            instances.add(instance);
        }


        for (int i = 0; i < instances.numInstances(); i++) {
            double classIndex = deserializedModel.classifyInstance(request.getFeatures().get(i), deserializedAttributes);
            String label = instances.classAttribute().value((int) classIndex);
            predictions.add(label);
            double[] distributions = deserializedModel.distributionForInstance(request.getFeatures().get(i), deserializedAttributes);
            Map<String, Double> classProbabilities = new LinkedHashMap<>();
            for (int j = 0; j < distributions.length; j++) {
                String classLabel = instances.classAttribute().value(j);
                classProbabilities.put(classLabel, distributions[j]);
            }
            distributionsList.add(classProbabilities);
        }

        SerializedModelData serializedModelData = SerializedModelData.builder()
                .modelId(request.getModelId())
                .labels(predictions)
                .distributions(distributionsList)
                .build();
        return ModelRequest.builder().serializedModelData(serializedModelData).build();


    }


    private Instance createInstance(List<Object> feature, ArrayList<Attribute> attributes, Instances dataset) {
        Instance instance = new DenseInstance(attributes.size());

        for (int i = 0; i < feature.size(); i++) {
            instance.setValue(attributes.get(i), Double.parseDouble(feature.get(i).toString()));
        }

        instance.setDataset(dataset);
        return instance;
    }


    public ArrayList<Attribute> deserializeAttributes(byte[] attrBytes) throws IOException, ClassNotFoundException {

        ByteArrayInputStream bis = new ByteArrayInputStream(attrBytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        ArrayList<Attribute> attributes = (ArrayList<Attribute>) ois.readObject();
        ois.close();
        return attributes;

    }

    public byte[] serializeModel(MachineAlgorithms model) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(model);
        oos.close();
        return bos.toByteArray();

    }

    public byte[] serializeAttributes(ArrayList<Attribute> attributes) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(attributes);
        oos.close();
        return bos.toByteArray();


    }



    public MachineAlgorithms deserializeModel(byte[] modelBytes) throws IOException, ClassNotFoundException {

        ByteArrayInputStream bis = new ByteArrayInputStream(modelBytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        MachineAlgorithms model = (MachineAlgorithms) ois.readObject();
        ois.close();
        return model;

    }


    public ModelRequest createModel(ModelRequest createRequest) throws Exception {

        classifier = createClassifier(createRequest);
        classifier.setOptions(createRequest.getOptions());
        byte[] modelBytes = serializeModel(classifier);
        String modelBase64 = Base64.getEncoder().encodeToString(modelBytes);
        SerializedModelData serializedModelData = SerializedModelData.builder()
                .modelId(createRequest.getModelId())
                .model(modelBase64)
                .build();
        return ModelRequest.builder().serializedModelData(serializedModelData).build();

    }

    // на будущее
    public MachineAlgorithms createClassifier(ModelRequest request) throws Exception {

        MachineAlgorithms classifier;
        switch (request.getClassifier()) {
            case "J48" -> {
                classifier = new J48Impl();
                return classifier;
            }
            case "NaiveBayes" -> {
                classifier = new NaiveBayesImpl();
                return classifier;
            }
            case "RandomForest" -> {
                classifier = new RandomForestImpl();
                return classifier;
            }
            default -> throw new InvalidAlgorithmException("Invalid algorithm");
        }

    }


    public ModelRequest process(ModelRequest request) throws Exception {

        switch (request.getModelLabel()) {
            case CREATE -> {
                return createModel(request);
            }
            case TRAIN -> {
                return train(request);
            }
            case PREDICT -> {
                return predict(request);
            }
            default -> throw new IllegalArgumentException("Invalid modelLabel: " + request.getModelLabel());
        }

    }

}
