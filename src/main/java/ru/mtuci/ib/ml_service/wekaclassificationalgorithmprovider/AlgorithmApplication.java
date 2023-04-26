package ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.event.EventListener;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.requestes.WekaProviderInformation;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.service.ModelService;

@SpringBootApplication
//@EnableDiscoveryClient
public class AlgorithmApplication {
    private final StreamBridge streamBridge;
    @Value("${wekaTopic.topic}")
    private  String wekaTopic;
    @Value("${defaultTopic.topic}")
    private  String defaultTopic;

    public AlgorithmApplication(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }


    public static void main(String[] args) {
        SpringApplication.run(AlgorithmApplication.class, args);

    }
    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        streamBridge.send(defaultTopic,new WekaProviderInformation("WekaClassificationAlgorithmProvider",wekaTopic, ModelService.getClassifiers()));
    }

}
