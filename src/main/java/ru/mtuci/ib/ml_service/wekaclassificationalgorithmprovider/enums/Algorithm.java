package ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.enums;

import java.util.Arrays;
import java.util.List;


public enum Algorithm {
    RANDOM_FOREST( "J48",Arrays.asList(
            new Hyperparameter("-C", "Description 1"),
            new Hyperparameter("-D", "Description 2")
    )),
    LINEAR_REGRESSION("J48",Arrays.asList(
            new Hyperparameter("-a", "Description 3"),
            new Hyperparameter("-b", "Description 4")
    )),
    K_NEAREST_NEIGHBORS("J48",Arrays.asList(
            new Hyperparameter("-k", "Description 5"),
            new Hyperparameter("-w", "Description 6")
    )),
    ADA_BOOST("J48",Arrays.asList(
            new Hyperparameter("-k", "Description 5"),
            new Hyperparameter("-w", "Description 6")
    )),
    J48("J48",Arrays.asList(
            new Hyperparameter("-U", "Use unpruned tree."),
            new Hyperparameter("-C <pruning confidence>", "Set confidence threshold for pruning.\n" +
                    "  (default 0.25)"),
            new Hyperparameter("-M <minimum number of instances>", " Set minimum number of instances per leaf.\n" +
                    "  (default 2)"),
            new Hyperparameter("-R", "  Use reduced error pruning."),
            new Hyperparameter("-N <number of folds>", "Set number of folds for reduced error\n" +
                    "  pruning. One fold is used as pruning set.\n" +
                    "  (default 3)"),
            new Hyperparameter("-O", "Do not collapse tree."),
            new Hyperparameter("-B", "Use binary splits only."),
            new Hyperparameter(" -N <number of folds>", " Set number of folds for reduced error\n" +
                    "  pruning. One fold is used as pruning set.\n" +
                    "  (default 3)"),
            new Hyperparameter("-S", " Don't perform subtree raising."),
            new Hyperparameter("-L", " Do not clean up after the tree has been built."),
            new Hyperparameter("-A", "Laplace smoothing for predicted probabilities."),
            new Hyperparameter("-J", "Do not use MDL correction for info gain on numeric attributes."),
            new Hyperparameter("-Q <seed>", "Seed for random data shuffling (default 1)."),
            new Hyperparameter("-doNotMakeSplitPointActualValue", "Do not make split point actual value.")
    ))

    ;

    private final List<Hyperparameter> hyperparameters;
    private final String name;

    Algorithm( String name,List<Hyperparameter> hyperparameters) {
        this.hyperparameters = hyperparameters;
        this.name = name;
    }


    public String getAlgorithmName() {
        return name;
    }

    public List<Hyperparameter> getHyperparameters() {
        return this.hyperparameters;
    }
}
