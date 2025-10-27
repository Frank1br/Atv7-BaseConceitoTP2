package com.gerenciamentoInventario.gerenciamentoInventario.Repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.gerenciamentoInventario.gerenciamentoInventario.Model.Produto;
import com.gerenciamentoInventario.gerenciamentoInventario.Util.JsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProdutoRepository {

    private static final String ARQUIVO_PRODUTOS = "produtos.json";
    private Map<String, Produto> produtos;

    public ProdutoRepository() {
        this.produtos = new HashMap<>();
        carregarProdutos();
    }

    public void carregarProdutos() {
        try {
            if (!JsonUtil.arquivoExiste(ARQUIVO_PRODUTOS)) {
                System.out.println("Arquivo de produtos não encontrado. Criando novo arquivo...");
                JsonUtil.criarArquivoVazio(ARQUIVO_PRODUTOS);
                return;
            }

            JsonNode rootNode = JsonUtil.carregarComoJsonNode(ARQUIVO_PRODUTOS);

            if (rootNode == null || !rootNode.isArray()) {
                System.out.println("Arquivo de produtos vazio ou inválido.");
                return;
            }

            produtos.clear();
            for (JsonNode node : rootNode) {
                Produto produto = JsonUtil.converterJsonNode(node, Produto.class);
                produtos.put(produto.getId(), produto);
            }

            System.out.println("✓ " + produtos.size() + " produto(s) carregado(s) com sucesso!");

        } catch (IOException e) {
            System.err.println("Erro ao carregar produtos: " + e.getMessage());
        }
    }

    public void salvarProdutos() {
        try {
            List<Produto> listaProdutos = new ArrayList<>(produtos.values());
            JsonUtil.salvarJson(listaProdutos, ARQUIVO_PRODUTOS);
            System.out.println("✓ Produtos salvos com sucesso!");
        } catch (IOException e) {
            System.err.println("✗ Erro ao salvar produtos: " + e.getMessage());
        }
    }

    public void salvar(Produto produto) {
        produtos.put(produto.getId(), produto);
        salvarProdutos();
    }

    public Produto buscarPorId(String id) {
        return produtos.get(id);
    }

    public List<Produto> buscarTodos() {
        return new ArrayList<>(produtos.values());
    }

    public boolean existe(String id) {
        return produtos.containsKey(id);
    }

    public void remover(String id) {
        produtos.remove(id);
        salvarProdutos();
    }
}