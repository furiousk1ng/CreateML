package ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.requestes;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.algorithms.AlgorithmDetails;

import java.util.List;


@AllArgsConstructor
@Data
public class WekaProviderInformation {
    private String provider;
    private String topic;
    private List<AlgorithmDetails> algorithms;

}
