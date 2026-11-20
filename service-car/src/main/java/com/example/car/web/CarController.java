package com.example.car.web;

import com.example.car.entities.Car;
import com.example.car.entities.Client;
import com.example.car.repositories.CarRepository;
import com.example.car.services.ClientApi;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    private final CarRepository repository;
    private final ClientApi clientApi;

    public CarController(CarRepository repository, ClientApi clientApi) {
        this.repository = repository;
        this.clientApi = clientApi;
    }

    @PostMapping
    public Car save(@RequestBody Car car) {
        return repository.save(car);
    }

    @GetMapping
    public List<Car> findAll() {
        List<Car> cars = repository.findAll();
        for (Car car : cars) {
            if (car.getClientId() != null) {
                car.setClient(clientApi.findClientById(car.getClientId()));
            }
        }
        return cars;
    }

    @GetMapping("/byClient/{clientId}")
    public List<Car> findByClient(@PathVariable Long clientId) {
        List<Car> cars = repository.findByClientId(clientId);
        if (!cars.isEmpty()) {
            Client client = clientApi.findClientById(clientId);
            cars.forEach(car -> car.setClient(client));
        }
        return cars;
    }
}
