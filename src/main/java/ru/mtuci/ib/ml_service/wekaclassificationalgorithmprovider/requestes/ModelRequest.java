package ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.requestes;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.algorithms.HyperParameter;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.enums.ModelLabel;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.model.SerializedModelData;

import java.util.List;


@Data
@Getter
@Builder
public class ModelRequest {
    private ModelLabel modelLabel;
    private String modelId;
    private String classifier;
    private List<HyperParameter> options;
    private SerializedModelData serializedModelData;
    private List<List<Object>> features;
    private List<Object> labels;
}


