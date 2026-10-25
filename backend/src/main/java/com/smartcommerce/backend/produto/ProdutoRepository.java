package com.smartcommerce.backend.produto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // 🔧 DECISÃO: listagem só mostra ativos — soft delete ficará invisível
    Page<Produto> findAllByAtivoTrue(Pageable pageable);
}