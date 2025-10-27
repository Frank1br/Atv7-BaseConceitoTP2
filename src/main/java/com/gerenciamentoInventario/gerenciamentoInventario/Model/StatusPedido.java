package com.gerenciamentoInventario.gerenciamentoInventario.Model;

public enum StatusPedido {
    NOVO("Novo - Pedido recém criado"),
    PROCESSANDO("Processando - Em preparação"),
    ENVIADO("Enviado - A caminho do cliente"),
    ENTREGUE("Entregue - Recebido pelo cliente"),
    CANCELADO("Cancelado - Pedido cancelado");

    private final String descricao;

    StatusPedido(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
