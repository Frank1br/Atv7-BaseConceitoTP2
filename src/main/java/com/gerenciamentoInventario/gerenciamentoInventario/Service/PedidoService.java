package com.gerenciamentoInventario.gerenciamentoInventario.Service;

import com.gerenciamentoInventario.gerenciamentoInventario.Model.ItemPedido;
import com.gerenciamentoInventario.gerenciamentoInventario.Model.Pedido;
import com.gerenciamentoInventario.gerenciamentoInventario.Model.Produto;
import com.gerenciamentoInventario.gerenciamentoInventario.Model.StatusPedido;
import com.gerenciamentoInventario.gerenciamentoInventario.Repository.PedidoRepository;
import com.gerenciamentoInventario.gerenciamentoInventario.Util.ValidadorUtil;

import java.util.List;
import java.util.Optional;

public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoService produtoService;

    public PedidoService(PedidoRepository pedidoRepository, ProdutoService produtoService) {
        this.pedidoRepository = pedidoRepository;
        this.produtoService = produtoService;
    }

    public Optional<Pedido> criarPedido(String nomeCliente, String emailCliente) {
        if (!ValidadorUtil.validarNome(nomeCliente)) {
            System.err.println("✗ Nome do cliente inválido!");
            return Optional.empty();
        }

        if (!ValidadorUtil.validarEmail(emailCliente)) {
            System.err.println("✗ E-mail inválido! Use o formato: exemplo@dominio.com");
            return Optional.empty();
        }

        String id = pedidoRepository.gerarNovoId();
        Pedido pedido = new Pedido(id, nomeCliente, emailCliente);

        return Optional.of(pedido);
    }

    public boolean adicionarItemAoPedido(Pedido pedido, String produtoId, int quantidade) {
        if (!ValidadorUtil.validarQuantidadePedido(quantidade)) {
            System.err.println("✗ Quantidade inválida! Deve ser maior que zero.");
            return false;
        }

        Optional<Produto> produtoOpt = produtoService.buscarProdutoPorId(produtoId);

        final boolean[] sucesso = {false};

        produtoOpt.ifPresentOrElse(
                produto -> {
                    if (!produtoService.verificarEstoque(produtoId, quantidade)) {
                        System.err.println("✗ Estoque insuficiente! Disponível: " + produto.getQuantidadeEstoque());
                        return;
                    }

                    ItemPedido item = new ItemPedido(
                            produto.getId(),
                            produto.getNome(),
                            produto.getPreco(),
                            quantidade
                    );

                    pedido.adicionarItem(item);
                    System.out.println("✓ Item adicionado: " + produto.getNome() + " (x" + quantidade + ")");
                    sucesso[0] = true;
                },
                () -> System.err.println("✗ Produto não encontrado: " + produtoId)
        );

        return sucesso[0];
    }

    public boolean finalizarPedido(Pedido pedido) {
        if (pedido.getItens().isEmpty()) {
            System.err.println("✗ Não é possível finalizar um pedido sem itens!");
            return false;
        }

        for (ItemPedido item : pedido.getItens()) {
            if (!produtoService.reduzirEstoque(item.getProdutoId(), item.getQuantidade())) {
                System.err.println("✗ Erro ao reduzir estoque do produto: " + item.getProdutoId());
                return false;
            }
        }

        pedidoRepository.salvar(pedido);
        System.out.println("✓ Pedido " + pedido.getId() + " criado com sucesso!");
        return true;
    }

    public Optional<Pedido> buscarPedidoPorId(String id) {
        return Optional.ofNullable(pedidoRepository.buscarPorId(id));
    }

    public boolean atualizarStatusPedido(String id, StatusPedido novoStatus) {
        Optional<Pedido> pedidoOpt = buscarPedidoPorId(id);

        pedidoOpt.ifPresent(pedido -> {
            pedido.setStatus(novoStatus);
            pedidoRepository.salvar(pedido);
            System.out.println("✓ Status do pedido " + id + " atualizado para: " + novoStatus.getDescricao());
        });

        return pedidoOpt.isPresent();
    }

    public List<Pedido> listarTodosPedidos() {
        return pedidoRepository.buscarTodos();
    }

    public void exibirResumoPedido(String id) {
        buscarPedidoPorId(id).ifPresentOrElse(
                pedido -> System.out.println(pedido.toString()),
                () -> System.err.println("✗ Pedido não encontrado: " + id)
        );
    }

    public void exibirDetalhesPedido(String id) {
        buscarPedidoPorId(id).ifPresentOrElse(
                pedido -> System.out.println(pedido.getDetalhes()),
                () -> System.err.println("✗ Pedido não encontrado: " + id)
        );
    }
}