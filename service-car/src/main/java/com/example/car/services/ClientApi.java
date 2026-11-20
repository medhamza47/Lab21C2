package com.example.car.services;

import com.example.car.entities.Client;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Service
public class ClientApi {
    private final WebClient webClient;

    public ClientApi(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://SERVICE-CLIENT").build();
    }

    public Client findClientById(Long id) {
        return webClient.get()
                .uri("/api/clients/{id}", id)
                .retrieve()
                .bodyToMono(Client.class)
                .block(Duration.ofSeconds(5));
    }
}
