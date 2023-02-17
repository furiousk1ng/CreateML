package com.example.try1231231;

import lombok.Data;

import java.util.List;

@Data
public class AttributeRequest {
    private String name;
    private String type;
    private List<String> values;
}
