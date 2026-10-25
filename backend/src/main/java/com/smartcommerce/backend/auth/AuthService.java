package com.smartcommerce.backend.auth;

import com.smartcommerce.backend.cliente.ClienteRepository;
import com.smartcommerce.backend.shared.JwtProperties;
import com.smartcommerce.backend.shared.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final ClienteRepository clientes;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    private final JwtProperties props;

    public AuthService(ClienteRepository clientes, PasswordEncoder encoder,
                       JwtService jwt, JwtProperties props) {
        this.clientes = clientes;
        this.encoder = encoder;
        this.jwt = jwt;
        this.props = props;
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest req) {
        var cliente = clientes.findByEmail(req.email())
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!encoder.matches(req.senha(), cliente.getSenhaHash())) {
            throw new CredenciaisInvalidasException();
        }

        var token = jwt.gerar(cliente.getEmail(), cliente.getRole());
        return TokenResponse.bearer(token, props.expirationMinutes());
    }
}