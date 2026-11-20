package com.example.client.web;

import com.example.client.entities.Client;
import com.example.client.repositories.ClientRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientRepository repository;

    public ClientController(ClientRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Client> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Client findById(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public Client save(@RequestBody Client client) {
        return repository.save(client);
    }
}
