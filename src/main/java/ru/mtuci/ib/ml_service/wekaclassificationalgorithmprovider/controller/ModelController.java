package ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.algorithms.AlgorithmDetails;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.requestes.ModelRequest;
import ru.mtuci.ib.ml_service.wekaclassificationalgorithmprovider.service.ModelService;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ModelController {
    private ModelService modelService;

    @Autowired
    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }


    @GetMapping("/algorithms")
    public List<AlgorithmDetails> getAlgorithms() {
        return modelService.getClassifiers();
    }

    @PostMapping("/model/create")
    public ModelRequest create(@RequestBody ModelRequest createRequest) throws Exception {
        return modelService.createModel(createRequest);
    }


    @PostMapping("/model/train")
    public ModelRequest train(@RequestBody ModelRequest trainRequest) throws Exception {
        return modelService.train(trainRequest);
    }

    @PostMapping("/model/predict")
    public ModelRequest predict(@RequestBody ModelRequest predictionRequest) throws Exception {
        return modelService.predict(predictionRequest);
    }
}
