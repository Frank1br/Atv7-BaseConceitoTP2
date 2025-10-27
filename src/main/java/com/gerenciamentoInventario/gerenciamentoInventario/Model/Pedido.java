package com.gerenciamentoInventario.gerenciamentoInventario.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private String id;
    private String nomeCliente;
    private String emailCliente;
    private List<ItemPedido> itens;
    private StatusPedido status;
    private LocalDateTime dataCriacao;

    // Construtor "vazio"
    public Pedido() {
        this.itens = new ArrayList<>();
        this.status = StatusPedido.NOVO;
        this.dataCriacao = LocalDateTime.now();
    }

    // Construtor com parâmetros básicos
    public Pedido(String id, String nomeCliente, String emailCliente) {
        this();
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.emailCliente = emailCliente;
    }

    // Metodo para calcular o valor total do pedido
    public double getValorTotal() {
        return itens.stream()
                .mapToDouble(ItemPedido::getSubtotal)
                .sum();
    }

    // Metodo para adicionar item ao pedido
    public void adicionarItem(ItemPedido item) {
        this.itens.add(item);
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Override
    public String toString() {
        return String.format("Pedido[ID=%s, Cliente=%s, Status=%s, Total=R$%.2f, Data=%s]",
                id, nomeCliente, status.name(), getValorTotal(), dataCriacao);
    }

    // Metodo para exibir detalhes completos do pedido

    public String getDetalhes() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== DETALHES DO PEDIDO ===\n");
        sb.append(String.format("ID: %s\n", id));
        sb.append(String.format("Cliente: %s\n", nomeCliente));
        sb.append(String.format("Email: %s\n", emailCliente));
        sb.append(String.format("Status: %s - %s\n", status.name(), status.getDescricao()));
        sb.append(String.format("Data: %s\n", dataCriacao));
        sb.append("\nItens do Pedido:\n");
        itens.forEach(item -> sb.append(item.toString()).append("\n"));
        sb.append(String.format("\nVALOR TOTAL: R$%.2f\n", getValorTotal()));
        sb.append("========================\n");
        return sb.toString();
    }
}
