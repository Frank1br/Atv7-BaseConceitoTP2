package com.gerenciamentoInventario.gerenciamentoInventario;

import com.gerenciamentoInventario.gerenciamentoInventario.Model.Pedido;
import com.gerenciamentoInventario.gerenciamentoInventario.Model.Produto;
import com.gerenciamentoInventario.gerenciamentoInventario.Model.StatusPedido;
import com.gerenciamentoInventario.gerenciamentoInventario.Repository.PedidoRepository;
import com.gerenciamentoInventario.gerenciamentoInventario.Repository.ProdutoRepository;
import com.gerenciamentoInventario.gerenciamentoInventario.Service.PedidoService;
import com.gerenciamentoInventario.gerenciamentoInventario.Service.ProdutoService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class SistemaInventario {

    private final Scanner scanner;
    private final ProdutoService produtoService;
    private final PedidoService pedidoService;

    public SistemaInventario() {
        this.scanner = new Scanner(System.in);

        ProdutoRepository produtoRepository = new ProdutoRepository();
        PedidoRepository pedidoRepository = new PedidoRepository();

        this.produtoService = new ProdutoService(produtoRepository);
        this.pedidoService = new PedidoService(pedidoRepository, produtoService);
    }

    public void iniciar() {
        System.out.println("\n╔═══════════════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE GERENCIAMENTO DE INVENTÁRIO         ║");
        System.out.println("║              E PEDIDOS                            ║");
        System.out.println("╚═══════════════════════════════════════════════════╝\n");

        boolean continuar = true;

        while (continuar) {
            exibirMenuPrincipal();
            int opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    menuGerenciarProdutos();
                    break;
                case 2:
                    menuGerenciarPedidos();
                    break;
                case 0:
                    continuar = false;
                    System.out.println("\n✓ Encerrando sistema. Até logo!");
                    break;
                default:
                    System.out.println("\n✗ Opção inválida! Tente novamente.");
            }
        }

        scanner.close();
    }

    private void exibirMenuPrincipal() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("MENU PRINCIPAL");
        System.out.println("=".repeat(50));
        System.out.println("1 - Gerenciar Produtos");
        System.out.println("2 - Gerenciar Pedidos");
        System.out.println("0 - Sair");
        System.out.print("\nEscolha uma opção: ");
    }

    private void menuGerenciarProdutos() {
        boolean voltar = false;

        while (!voltar) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("GERENCIAR PRODUTOS");
            System.out.println("=".repeat(50));
            System.out.println("1 - Adicionar/Atualizar Produto");
            System.out.println("2 - Listar Todos os Produtos");
            System.out.println("3 - Buscar Produto por ID");
            System.out.println("0 - Voltar ao Menu Principal");
            System.out.print("\nEscolha uma opção: ");

            int opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    adicionarProduto();
                    break;
                case 2:
                    listarProdutos();
                    break;
                case 3:
                    buscarProduto();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("\n✗ Opção inválida!");
            }
        }
    }

    private void menuGerenciarPedidos() {
        boolean voltar = false;

        while (!voltar) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("GERENCIAR PEDIDOS");
            System.out.println("=".repeat(50));
            System.out.println("1 - Criar Novo Pedido");
            System.out.println("2 - Listar Todos os Pedidos");
            System.out.println("3 - Ver Detalhes de um Pedido");
            System.out.println("4 - Atualizar Status do Pedido");
            System.out.println("0 - Voltar ao Menu Principal");
            System.out.print("\nEscolha uma opção: ");

            int opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    criarPedido();
                    break;
                case 2:
                    listarPedidos();
                    break;
                case 3:
                    verDetalhesPedido();
                    break;
                case 4:
                    atualizarStatusPedido();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("\n✗ Opção inválida!");
            }
        }
    }

    private void adicionarProduto() {
        System.out.println("\n--- ADICIONAR/ATUALIZAR PRODUTO ---");

        System.out.print("ID do Produto: ");
        String id = scanner.nextLine().trim();

        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();

        System.out.print("Preço: R$ ");
        double preco = lerDouble();

        System.out.print("Quantidade em Estoque: ");
        int quantidade = lerInteiro();

        produtoService.adicionarOuAtualizarProduto(id, nome, preco, quantidade);
    }

    private void listarProdutos() {
        System.out.println("\n--- LISTA DE PRODUTOS ---");

        List<Produto> produtos = produtoService.listarTodosProdutos();

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        System.out.println("\nTotal de produtos: " + produtos.size() + "\n");
        for (Produto produto : produtos) {
            System.out.println(produto);
        }
    }

    private void buscarProduto() {
        System.out.println("\n--- BUSCAR PRODUTO ---");

        System.out.print("Digite o ID do produto: ");
        String id = scanner.nextLine().trim();

        produtoService.buscarProdutoPorId(id).ifPresentOrElse(
                produto -> {
                    System.out.println("\n✓ Produto encontrado:");
                    System.out.println(produto);
                },
                () -> System.out.println("\n✗ Produto não encontrado com ID: " + id)
        );
    }

    private void criarPedido() {
        System.out.println("\n--- CRIAR NOVO PEDIDO ---");

        System.out.print("Nome do Cliente: ");
        String nomeCliente = scanner.nextLine().trim();

        System.out.print("E-mail do Cliente: ");
        String emailCliente = scanner.nextLine().trim();

        Optional<Pedido> pedidoOpt = pedidoService.criarPedido(nomeCliente, emailCliente);

        if (pedidoOpt.isEmpty()) {
            System.out.println("\n✗ Não foi possível criar o pedido. Verifique os dados informados.");
            return;
        }

        Pedido pedido = pedidoOpt.get();
        System.out.println("\n✓ Pedido iniciado: " + pedido.getId());

        boolean adicionarMais = true;

        while (adicionarMais) {
            System.out.println("\n--- Adicionar Item ao Pedido ---");

            listarProdutos();

            System.out.print("\nID do Produto (ou 0 para finalizar): ");
            String produtoId = scanner.nextLine().trim();

            if (produtoId.equals("0")) {
                break;
            }

            System.out.print("Quantidade: ");
            int quantidade = lerInteiro();

            pedidoService.adicionarItemAoPedido(pedido, produtoId, quantidade);

            System.out.print("\nDeseja adicionar mais itens? (S/N): ");
            String resposta = scanner.nextLine().trim().toUpperCase();
            adicionarMais = resposta.equals("S");
        }

        if (pedidoService.finalizarPedido(pedido)) {
            System.out.println("\n" + pedido.getDetalhes());
        }
    }

    private void listarPedidos() {
        System.out.println("\n--- LISTA DE PEDIDOS ---");

        List<Pedido> pedidos = pedidoService.listarTodosPedidos();

        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido cadastrado.");
            return;
        }

        System.out.println("\nTotal de pedidos: " + pedidos.size() + "\n");
        for (Pedido pedido : pedidos) {
            System.out.println(pedido);
        }
    }

    private void verDetalhesPedido() {
        System.out.println("\n--- DETALHES DO PEDIDO ---");

        System.out.print("Digite o ID do pedido: ");
        String id = scanner.nextLine().trim();

        pedidoService.exibirDetalhesPedido(id);
    }

    private void atualizarStatusPedido() {
        System.out.println("\n--- ATUALIZAR STATUS DO PEDIDO ---");

        System.out.print("Digite o ID do pedido: ");
        String id = scanner.nextLine().trim();

        Optional<Pedido> pedidoOpt = pedidoService.buscarPedidoPorId(id);

        if (pedidoOpt.isEmpty()) {
            System.out.println("\n✗ Pedido não encontrado: " + id);
            return;
        }

        Pedido pedido = pedidoOpt.get();
        System.out.println("\nPedido atual: " + pedido);
        System.out.println("Status atual: " + pedido.getStatus().getDescricao());

        System.out.println("\n--- Opções de Status ---");
        StatusPedido[] statusDisponiveis = StatusPedido.values();
        for (int i = 0; i < statusDisponiveis.length; i++) {
            System.out.println((i + 1) + " - " + statusDisponiveis[i].getDescricao());
        }

        System.out.print("\nEscolha o novo status: ");
        int opcao = lerOpcao();

        if (opcao < 1 || opcao > statusDisponiveis.length) {
            System.out.println("\n✗ Opção inválida!");
            return;
        }

        StatusPedido novoStatus = statusDisponiveis[opcao - 1];
        pedidoService.atualizarStatusPedido(id, novoStatus);
    }

    private int lerOpcao() {
        try {
            int opcao = Integer.parseInt(scanner.nextLine().trim());
            return opcao;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private int lerInteiro() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("✗ Valor inválido! Usando 0.");
            return 0;
        }
    }

    private double lerDouble() {
        try {
            return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            System.out.println("✗ Valor inválido! Usando 0.0.");
            return 0.0;
        }
    }

    public static void main(String[] args) {
        SistemaInventario sistema = new SistemaInventario();
        sistema.iniciar();
    }
}