package com.smartcommerce.backend.cliente;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cliente", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @Column(nullable = false, length = 20)
    private String role = "CUSTOMER";

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    protected Cliente() {}

    public Cliente(String nome, String email, String senhaHash) {
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.role = "CUSTOMER";
    }

    @PrePersist
    void onCreate() { this.criadoEm = LocalDateTime.now(); }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenhaHash() { return senhaHash; }
    public String getRole() { return role; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}