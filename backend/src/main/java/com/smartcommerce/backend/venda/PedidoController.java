package com.smartcommerce.backend.venda;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> criar(
            Authentication auth,
            @Valid @RequestBody CheckoutRequest req,
            UriComponentsBuilder uriBuilder) {
        var response = service.criar(auth.getName(), req);
        var uri = uriBuilder.path("/api/v1/pedidos/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscar(Authentication auth, @PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(auth.getName(), id));
    }

    @GetMapping
    public ResponseEntity<Page<PedidoResponse>> meusPedidos(
            Authentication auth,
            @PageableDefault(size = 10, sort = "criadoEm") Pageable pageable) {
        return ResponseEntity.ok(service.listarMeus(auth.getName(), pageable));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponse> mudarStatus(
            Authentication auth,
            @PathVariable Long id,
            @Valid @RequestBody MudarStatusRequest req) {
        boolean admin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(service.mudarStatus(auth.getName(), admin, id, req.novoStatus()));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<PedidoResponse>> listarTodos(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(service.listarTodos(pageable));
    }
}