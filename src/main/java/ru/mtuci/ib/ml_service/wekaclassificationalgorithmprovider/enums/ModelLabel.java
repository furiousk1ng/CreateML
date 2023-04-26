package ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ModelLabel {
    CREATE("Создать модель"),
    TRAIN("Обучить модель"),
    PREDICT("Получить предсказания");
    private final String description;
}
