package ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.algorithms;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class HyperParameter {
    private String name;
    private String value;
}