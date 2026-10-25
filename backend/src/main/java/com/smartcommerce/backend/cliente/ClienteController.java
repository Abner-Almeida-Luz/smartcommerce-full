package com.smartcommerce.backend.cliente;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> registrar(
            @Valid @RequestBody ClienteRequest req,
            UriComponentsBuilder uriBuilder) {
        var response = service.registrar(req);
        var uri = uriBuilder.path("/api/v1/clientes/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }
}