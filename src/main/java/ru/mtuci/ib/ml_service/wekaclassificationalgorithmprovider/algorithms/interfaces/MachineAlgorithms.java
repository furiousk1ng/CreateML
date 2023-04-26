package ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.algorithms.interfaces;

import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.algorithms.HyperParameter;
import weka.core.Attribute;

import java.util.ArrayList;
import java.util.List;

public interface MachineAlgorithms {
    void buildClassifier(String name, ArrayList<Attribute> attInfo, int capacity, List<List<Object>> features, List<Object> labels) throws Exception;

    double classifyInstance(List<Object> feature, ArrayList<Attribute> attInfo) throws Exception;

    double[] distributionForInstance(List<Object> feature, ArrayList<Attribute> attInfo) throws Exception;

    void setOptions(List<HyperParameter> hyperparameters) throws Exception;
}