package com.example.try1231231;

import org.springframework.stereotype.Component;
import weka.classifiers.Classifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryModelStore implements ModelStore {
    private final Map<String, Classifier> modelMap;

    public InMemoryModelStore() {
        modelMap = new HashMap<>();
    }

    @Override
    public void addModel(String state, Classifier model) {
        modelMap.put(state, model);
    }

    @Override
    public Classifier getModel(String state) {
        return modelMap.get(state);
    }

    @Override
    public void updateModel(String state, Classifier model) {
        modelMap.replace(state, model);
    }
    @Override
    public List<String> getModelNames() {
        return new ArrayList<>(modelMap.keySet());
    }
}