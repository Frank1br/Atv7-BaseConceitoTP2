package com.gerenciamentoInventario.gerenciamentoInventario.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gerenciamentoInventario.gerenciamentoInventario.Model.Pedido;
import com.gerenciamentoInventario.gerenciamentoInventario.Util.JsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PedidoRepository {

    private static final String ARQUIVO_PEDIDOS = "pedidos.json";
    private Map<String, Pedido> pedidos;

    public PedidoRepository() {
        this.pedidos = new HashMap<>();
        carregarPedidos();
    }

    public void carregarPedidos() {
        try {
            if (!JsonUtil.arquivoExiste(ARQUIVO_PEDIDOS)) {
                System.out.println("Arquivo de pedidos não encontrado. Criando novo arquivo...");
                JsonUtil.criarArquivoVazio(ARQUIVO_PEDIDOS);
                return;
            }

            List<Pedido> listaPedidos = JsonUtil.getObjectMapper().readValue(
                    new java.io.File(ARQUIVO_PEDIDOS),
                    new TypeReference<List<Pedido>>() {}
            );

            pedidos.clear();
            if (listaPedidos != null) {
                for (Pedido pedido : listaPedidos) {
                    pedidos.put(pedido.getId(), pedido);
                }
            }

            System.out.println("✓ " + pedidos.size() + " pedido(s) carregado(s) com sucesso!");

        } catch (IOException e) {
            System.err.println("Erro ao carregar pedidos: " + e.getMessage());
        }
    }

    public void salvarPedidos() {
        try {
            List<Pedido> listaPedidos = new ArrayList<>(pedidos.values());
            JsonUtil.salvarJson(listaPedidos, ARQUIVO_PEDIDOS);
            System.out.println("✓ Pedidos salvos com sucesso!");
        } catch (IOException e) {
            System.err.println("✗ Erro ao salvar pedidos: " + e.getMessage());
        }
    }

    public void salvar(Pedido pedido) {
        pedidos.put(pedido.getId(), pedido);
        salvarPedidos();
    }

    public Pedido buscarPorId(String id) {
        return pedidos.get(id);
    }

    public List<Pedido> buscarTodos() {
        return new ArrayList<>(pedidos.values());
    }

    public boolean existe(String id) {
        return pedidos.containsKey(id);
    }

    public String gerarNovoId() {
        int maxId = 0;
        for (String id : pedidos.keySet()) {
            try {
                int numId = Integer.parseInt(id.replace("PED", ""));
                if (numId > maxId) {
                    maxId = numId;
                }
            } catch (NumberFormatException e) {
            }
        }
        return String.format("PED%03d", maxId + 1);
    }
}

