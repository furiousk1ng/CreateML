package ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.algorithms.Impl;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMachineAlgorithm {

    protected Instances dataSet;
    protected Instance instance;

    protected Instance createInstance(List<Object> feature, ArrayList<Attribute> attributes, List<Object> labels, int index) {
        Instance instance = new DenseInstance(attributes.size());

        for (int i = 0; i < feature.size(); i++) {
            instance.setValue(attributes.get(i), Double.parseDouble(feature.get(i).toString()));
        }

        Attribute classAttribute = attributes.get(attributes.size() - 1);
        instance.setValue(classAttribute, labels.get(index).toString());

        return instance;
    }

    protected Instance createInstance(List<Object> feature, ArrayList<Attribute> attributes) {
        instance = new DenseInstance(attributes.size());
        instance.setDataset(dataSet);

        for (int i = 0; i < feature.size(); i++) {
            instance.setValue(attributes.get(i), Double.parseDouble(feature.get(i).toString()));
        }

        return instance;
    }

    public Instance createInstance(List<Object> feature, ArrayList<Attribute> attributes, Instances dataSet) {
        Instance instance = new DenseInstance(attributes.size());
        for (int i = 0; i < feature.size(); i++) {
            instance.setValue(attributes.get(i), Double.parseDouble(feature.get(i).toString()));
        }
        instance.setDataset(dataSet);
        return instance;
    }
}
