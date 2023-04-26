package ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.exception;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public  class ErrorMessage {
    private String modelId;
    private String errorType;
    private String errorMessage;
    private LocalDateTime localDateTime;

    public ErrorMessage(String errorType, String errorMessage) {
        this.errorType = errorType;
        this.errorMessage = errorMessage;
        this.localDateTime = LocalDateTime.now();
    }
}

