package com.example.try1231231;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.*;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class ModelController {
    @Autowired
    private InMemoryModelStore modelStore;

    @GetMapping("/list")
    public ResponseEntity<List<String>> getModelList() {
        List<String> modelList = new ArrayList<>(modelStore.getModelNames());
        return ResponseEntity.ok(modelList);
    }

    @PostMapping("/model/create")
    public ResponseEntity<String> createModel(@RequestBody ModelRequest request) {

        try {
            Classifier classifier = null;
            switch (request.getAlgorithm()) {
                case "J48":
                    J48 j48 = new J48();
                    j48.setOptions(request.getHyperparameters());
                    classifier = j48;
                    break;
                case "NaiveBayes":
                    NaiveBayes naiveBayes = new NaiveBayes();
                    naiveBayes.setOptions(request.getHyperparameters());
                    classifier = naiveBayes;
                    break;
                case "RandomForest":
                    RandomForest randomForest = new RandomForest();
                    randomForest.setOptions(request.getHyperparameters());
                    classifier = randomForest;
                    break;
                default:
                    return ResponseEntity.badRequest().body("Invalid algorithm specified");
            }
            List<Attribute> attributeList = new ArrayList<>();
            for (AttributeRequest attributeRequest : request.getAttributes()) {
                if (attributeRequest.getType().equals("nominal")) {
                    attributeList.add(new Attribute(attributeRequest.getName(), attributeRequest.getValues()));
                } else {
                    attributeList.add(new Attribute(attributeRequest.getName()));
                }
            }
            Instances instances = new Instances(request.getName(), (ArrayList<Attribute>) attributeList, request.getCapacity());
            instances.setClassIndex(instances.numAttributes() - 1);
            classifier.buildClassifier(instances);
            modelStore.addModel(request.getName(), classifier);
            return ResponseEntity.ok("Model created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/train")
    public ResponseEntity<Object> trainModel(@RequestBody ModelTrainRequest request) {
        try {
            Classifier classifier = modelStore.getModel(request.getModelState());
            Instances instances = createInstances(request.getAttributes(), request.getLabels());
            classifier.buildClassifier(instances);

            AttributeSelection attributeSelection = new AttributeSelection();
            attributeSelection.setEvaluator(new InfoGainAttributeEval());
            attributeSelection.setSearch(new Ranker());
            attributeSelection.SelectAttributes(instances);

            int[] attributesIndices = attributeSelection.selectedAttributes();
            String attributeRanking = Arrays.toString(attributesIndices);

            modelStore.updateModel(request.getModelState(), classifier);
            ModelTrainResponse response = new ModelTrainResponse(request.getModelState(), attributeRanking);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    private Instances createInstances(double[][] attributes, String[] labels) {
        ArrayList<Attribute> atts = new ArrayList<>();
        for (int i = 0; i < attributes[0].length; i++) {
            Attribute att = new Attribute("att" + i);
            atts.add(att);
        }
        List<String> classVal = new ArrayList<>();
        classVal.add("positive");
        classVal.add("negative");
        Attribute classAttribute = new Attribute("class", classVal);
        atts.add(classAttribute);
        Instances instances = new Instances("dataset", atts, attributes.length);
        instances.setClassIndex(atts.size() - 1);
        for (int i = 0; i < attributes.length; i++) {
            double[] vals = attributes[i];
            Instance inst = new DenseInstance(atts.size());
            for (int j = 0; j < vals.length; j++) {
                inst.setValue(atts.get(j), vals[j]);
            }
            inst.setValue(atts.get(atts.size()-1), labels[i]);
            instances.add(inst);
        }
        return instances;
    }

}
