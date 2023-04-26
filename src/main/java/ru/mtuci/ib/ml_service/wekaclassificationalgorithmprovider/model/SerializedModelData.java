package ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SerializedModelData {
    private String modelId;
    private String model;
    private String attribute;
    private List<String> labels;
    private List<Map<String, Double>> distributions;

}