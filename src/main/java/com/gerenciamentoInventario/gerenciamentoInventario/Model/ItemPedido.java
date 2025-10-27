package com.gerenciamentoInventario.gerenciamentoInventario.Model;

import java.util.Objects;

public class ItemPedido {
    private String produtoId;
    private String nomeProduto;  // Denormalizado para facilitar exibição
    private double precoUnitario; // Preço no momento do pedido
    private int quantidade;

    // Construtor vazio
    public ItemPedido() {
    }

    public ItemPedido(String produtoId, String nomeProduto, double precoUnitario, int quantidade) {
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
    }

    public double getSubtotal() {
        return precoUnitario * quantidade;
    }

    public String getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(String produtoId) {
        this.produtoId = produtoId;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "ItemPedido{" +
                "produtoId='" + produtoId + '\'' +
                ", nomeProduto='" + nomeProduto + '\'' +
                ", precoUnitario=" + precoUnitario +
                ", quantidade=" + quantidade +
                '}';
    }
}
