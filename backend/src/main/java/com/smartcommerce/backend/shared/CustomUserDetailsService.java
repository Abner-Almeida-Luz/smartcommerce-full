package com.smartcommerce.backend.shared;

import com.smartcommerce.backend.cliente.Cliente;
import com.smartcommerce.backend.cliente.ClienteRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final ClienteRepository repo;

    public CustomUserDetailsService(ClienteRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Cliente c = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente não encontrado"));

        return User.builder()
                .username(c.getEmail())
                .password(c.getSenhaHash())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + c.getRole())))
                .build();
    }
}