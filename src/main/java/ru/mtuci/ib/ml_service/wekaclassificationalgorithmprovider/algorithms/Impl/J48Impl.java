package ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.algorithms.Impl;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.algorithms.HyperParameter;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.algorithms.Impl.AbstractMachineAlgorithm;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.algorithms.interfaces.MachineAlgorithms;
import weka.attributeSelection.AttributeSelection;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
// абстрактный класс
public class J48Impl extends AbstractMachineAlgorithm implements MachineAlgorithms, Serializable {

    private J48 classifier;
    private Instance instance;
    private Instances dataSet;


    public J48Impl() {
        classifier = new J48();
    }



    @Override
    public void buildClassifier(String name, ArrayList<Attribute> attInfo, int capacity, List<List<Object>> features, List<Object> labels) throws Exception {
        dataSet = new Instances(name, attInfo, capacity);
        dataSet.setClassIndex(attInfo.size() - 1);
        for (int i = 0; i < features.size(); i++) {
            Instance instance = createInstance(features.get(i), attInfo, labels,  i);
            dataSet.add(instance);
        }

        classifier.buildClassifier(dataSet);
    }

    @Override
    public double classifyInstance(List<Object> feature, ArrayList<Attribute> attInfo) throws Exception {
        instance = createInstance(feature, attInfo);
        instance.setDataset(dataSet);
        return classifier.classifyInstance(instance);
    }

    @Override
    public double[] distributionForInstance(List<Object> feature, ArrayList<Attribute> attInfo) throws Exception {
        instance = createInstance(feature,attInfo, dataSet);
        return classifier.distributionForInstance(instance);
    }


    public void setOptions(List<HyperParameter> hyperparameters) throws Exception {
        if (hyperparameters != null && !hyperparameters.isEmpty()) {
            List<String> optionsList = new ArrayList<>();
            for (HyperParameter param : hyperparameters) {
                optionsList.add(param.getName());
                if (param.getValue() != null) {
                    optionsList.add(param.getValue());
                }
            }
            String[] options = optionsList.toArray(new String[optionsList.size()]);
            classifier.setOptions(options);
        }
    }


}
