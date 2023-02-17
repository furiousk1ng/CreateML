package com.example.try1231231;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelTrainRequest {
    private String modelState;
    private double[][] attributes;
    private String[] labels;
}