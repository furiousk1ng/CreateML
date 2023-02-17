package com.example.try1231231;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelTrainResponse {
    private String modelState;
    private String attributeRanking;
}