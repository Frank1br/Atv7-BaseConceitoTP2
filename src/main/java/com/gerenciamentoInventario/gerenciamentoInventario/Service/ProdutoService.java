package com.gerenciamentoInventario.gerenciamentoInventario.Service;

import com.gerenciamentoInventario.gerenciamentoInventario.Model.Produto;
import com.gerenciamentoInventario.gerenciamentoInventario.Repository.ProdutoRepository;
import com.gerenciamentoInventario.gerenciamentoInventario.Util.ValidadorUtil;

import java.util.List;
import java.util.Optional;

public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public boolean adicionarOuAtualizarProduto(String id, String nome, double preco, int quantidade) {
        if (!ValidadorUtil.validarId(id)) {
            System.err.println("✗ ID inválido!");
            return false;
        }

        if (!ValidadorUtil.validarNome(nome)) {
            System.err.println("✗ Nome inválido! Deve ter pelo menos 3 caracteres.");
            return false;
        }

        if (!ValidadorUtil.validarPreco(preco)) {
            System.err.println("✗ Preço inválido! Deve ser maior que zero.");
            return false;
        }

        if (!ValidadorUtil.validarQuantidade(quantidade)) {
            System.err.println("✗ Quantidade inválida! Deve ser maior ou igual a zero.");
            return false;
        }

        if (produtoRepository.existe(id)) {
            Produto produtoExistente = produtoRepository.buscarPorId(id);
            produtoExistente.setQuantidadeEstoque(produtoExistente.getQuantidadeEstoque() + quantidade);
            produtoRepository.salvar(produtoExistente);
            System.out.println("✓ Produto atualizado! Nova quantidade: " + produtoExistente.getQuantidadeEstoque());
        } else {
            Produto novoProduto = new Produto(id, nome, preco, quantidade);
            produtoRepository.salvar(novoProduto);
            System.out.println("✓ Produto adicionado com sucesso!");
        }

        return true;
    }

    public Optional<Produto> buscarProdutoPorId(String id) {
        return Optional.ofNullable(produtoRepository.buscarPorId(id));
    }

    public List<Produto> listarTodosProdutos() {
        return produtoRepository.buscarTodos();
    }

    public boolean verificarEstoque(String id, int quantidade) {
        Optional<Produto> produto = buscarProdutoPorId(id);
        return produto.map(p -> p.getQuantidadeEstoque() >= quantidade).orElse(false);
    }

    public boolean reduzirEstoque(String id, int quantidade) {
        Optional<Produto> produtoOpt = buscarProdutoPorId(id);

        if (produtoOpt.isEmpty()) {
            return false;
        }

        Produto produto = produtoOpt.get();

        if (produto.getQuantidadeEstoque() < quantidade) {
            return false;
        }

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidade);
        produtoRepository.salvar(produto);
        return true;
    }
}