package com.example.try1231231;

import weka.classifiers.Classifier;

import java.util.List;

public interface ModelStore {
    void addModel(String state, Classifier model);

    Classifier getModel(String state);

    void updateModel(String state, Classifier model);
    List<String> getModelNames();

}