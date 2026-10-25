package com.smartcommerce.backend.venda;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carrinho")
public class CarrinhoController {

    private final CarrinhoService service;

    public CarrinhoController(CarrinhoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<CarrinhoResponse> obter(Authentication auth) {
        return ResponseEntity.ok(service.obter(auth.getName()));
    }

    @PostMapping("/itens")
    public ResponseEntity<CarrinhoResponse> adicionar(
            Authentication auth,
            @Valid @RequestBody AdicionarItemRequest req) {
        return ResponseEntity.ok(service.adicionar(auth.getName(), req));
    }

    @DeleteMapping("/itens/{produtoId}")
    public ResponseEntity<CarrinhoResponse> remover(
            Authentication auth,
            @PathVariable Long produtoId) {
        return ResponseEntity.ok(service.removerItem(auth.getName(), produtoId));
    }

    @DeleteMapping
    public ResponseEntity<CarrinhoResponse> limpar(Authentication auth) {
        return ResponseEntity.ok(service.limpar(auth.getName()));
    }
}