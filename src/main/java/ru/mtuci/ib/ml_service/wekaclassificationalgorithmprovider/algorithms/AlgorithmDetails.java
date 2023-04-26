package ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.algorithms;

import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.enums.Hyperparameter;

import java.util.List;


public class AlgorithmDetails {
    public String algorithmName;
    public List<Hyperparameter> hyperparameters;

    public AlgorithmDetails(String algorithmName, List<Hyperparameter> hyperparameters) {
        this.algorithmName = algorithmName;
        this.hyperparameters = hyperparameters;
    }

    public AlgorithmDetails(String algorithmName) {
        this.algorithmName = algorithmName;
    }
}




