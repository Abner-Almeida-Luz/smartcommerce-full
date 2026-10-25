package com.smartcommerce.backend.shared;

import com.smartcommerce.backend.auth.CredenciaisInvalidasException;
import com.smartcommerce.backend.produto.ProdutoNaoEncontradoException;
import com.smartcommerce.backend.cliente.EmailJaCadastradoException;
import com.smartcommerce.backend.venda.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ProblemDetail handleNotFound(ProdutoNaoEncontradoException ex) {
        var pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Produto não encontrado");
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        var pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Erro de validação");
        pd.setTitle("Requisição inválida");

        List<Map<String, String>> errors = ex.getFieldErrors().stream()
                .map(e -> Map.of(
                        "field", e.getField(),
                        "message", e.getDefaultMessage()
                ))
                .toList();

        pd.setProperty("errors", errors);
        return pd;
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ProblemDetail handleEmailDuplicado(EmailJaCadastradoException ex) {
        var pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        pd.setTitle("Email já cadastrado");
        return pd;
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ProblemDetail handleCredenciais(CredenciaisInvalidasException ex) {
        var pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        pd.setTitle("Não autenticado");
        return pd;
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(org.springframework.security.access.AccessDeniedException ex) {
        var pd = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "Acesso negado");
        pd.setTitle("Sem permissão");
        return pd;
    }

    @ExceptionHandler(ProdutoIndisponivelException.class)
    public ProblemDetail handleProdutoIndisponivel(ProdutoIndisponivelException ex) {
        var pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        pd.setTitle("Produto indisponível");
        return pd;
    }

    @ExceptionHandler(ItemCarrinhoNaoEncontradoException.class)
    public ProblemDetail handleItemNaoEncontrado(ItemCarrinhoNaoEncontradoException ex) {
        var pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Item não encontrado");
        return pd;
    }

    @ExceptionHandler(CarrinhoVazioException.class)
    public ProblemDetail handleCarrinhoVazio(CarrinhoVazioException ex) {
        var pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        pd.setTitle("Carrinho vazio");
        return pd;
    }

    @ExceptionHandler(PedidoNaoEncontradoException.class)
    public ProblemDetail handlePedidoNaoEncontrado(PedidoNaoEncontradoException ex) {
        var pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Pedido não encontrado");
        return pd;
    }

    @ExceptionHandler(TransicaoInvalidaException.class)
    public ProblemDetail handleTransicaoInvalida(TransicaoInvalidaException ex) {
        var pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        pd.setTitle("Transição inválida");
        return pd;
    }
}