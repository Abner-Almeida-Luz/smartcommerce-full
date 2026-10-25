package com.smartcommerce.backend.cliente;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(ClienteRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ClienteResponse registrar(ClienteRequest req) {
        if (repository.existsByEmail(req.email())) {
            throw new EmailJaCadastradoException(req.email());
        }
        var hash = passwordEncoder.encode(req.senha());
        var cliente = new Cliente(req.nome(), req.email(), hash);
        return ClienteResponse.from(repository.save(cliente));
    }
}