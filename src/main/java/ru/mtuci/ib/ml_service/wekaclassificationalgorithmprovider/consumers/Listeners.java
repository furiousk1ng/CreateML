package ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.consumers;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.exception.ErrorMessage;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.model.SerializedModelData;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.requestes.ModelRequest;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.service.ModelService;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Component
public class Listeners {

    private final ModelService modelService;
    private final StreamBridge streamBridge;


    @Value("${defaultTopic.topic}")
    private String topic;

    @Value("${customErrorTopic.topic}")
    private String errorTopic;


    @Bean
    public Consumer<ModelRequest> modelRequestConsumer() {
        return request -> {
            try {
                ModelRequest data = modelService.process(request);
                streamBridge.send(topic, data);
            } catch (Exception e) {
                sendError(e);
            }
        };
    }

    private void sendError(Exception e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getClass().getSimpleName(), e.getMessage());
        streamBridge.send(errorTopic, errorMessage);
    }


}
