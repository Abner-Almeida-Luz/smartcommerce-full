package com.smartcommerce.backend.venda;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Page<Pedido> findByClienteId(Long clienteId, Pageable pageable);
    Page<Pedido> findAllByOrderByCriadoEmDesc(Pageable pageable);
}