package com.smartcommerce.backend.produto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    // 🔧 DECISÃO: injeção por construtor (não @Autowired em campo)
    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResponse> listar(Pageable pageable) {
        return repository.findAllByAtivoTrue(pageable)
                .map(ProdutoResponse::from);
    }

    @Transactional(readOnly = true)
    public ProdutoResponse buscar(Long id) {
        return repository.findById(id)
                .filter(Produto::getAtivo)   // 🔧 soft delete: inativo é "não encontrado"
                .map(ProdutoResponse::from)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

    @Transactional
    public ProdutoResponse criar(ProdutoRequest req) {
        var produto = new Produto(req.nome(), req.preco(), req.estoque());
        return ProdutoResponse.from(repository.save(produto));
    }

    @Transactional
    public ProdutoResponse atualizar(Long id, ProdutoRequest req) {
        var produto = repository.findById(id)
                .filter(Produto::getAtivo)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
        produto.atualizar(req.nome(), req.preco(), req.estoque());
        return ProdutoResponse.from(produto);  // JPA flush no commit da transação
    }

    @Transactional
    public void desativar(Long id) {
        var produto = repository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
        produto.desativar();
    }
}