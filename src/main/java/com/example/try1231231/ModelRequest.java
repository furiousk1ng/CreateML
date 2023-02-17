package com.example.try1231231;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelRequest {
    private String algorithm;
    private String name;
    private List<AttributeRequest> attributes;
    private int capacity;
    private String[] hyperparameters;
}

