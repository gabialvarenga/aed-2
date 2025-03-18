import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Comercio {
    static final int MAX_NOVOS_PRODUTOS = 10;
    static String nomeArquivoDados;
    static Scanner sc;
    static Produto[] produtosCadastrados;
    static int quantosProdutos = 0;
    static final int MAX_PEDIDOS = 10;
    static Pedido[] pedidosCadastrados;
    static int quantosPedidos;

    static void pausa() {
        System.out.println("Digite enter para continuar...");
        sc.nextLine();
    }

    static void cabecalho() {
        System.out.println("AED-II COMÉRCIO DE COISINHAS");
        System.out.println("===========================");
    }

    static int menu() {
        cabecalho();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar e imprimir os dados de um produto");
        System.out.println("3 - Cadastrar novo produto");
        System.out.println("4 - Iniciar novo pedido");
        System.out.println("5 - Fechar pedido");
        System.out.println("0 - Sair");
        System.out.print("Digite sua opção: ");
        return Integer.parseInt(sc.nextLine());
    }

    static Produto[] lerProdutos(String nomeArquivoDados) {

        Scanner arquivo = null;
        int numProdutos;
        String linha;
        Produto produto;
        Produto[] produtosCadastrados = new Produto[MAX_NOVOS_PRODUTOS];

        try {
            arquivo = new Scanner(new File(nomeArquivoDados), Charset.forName("UTF-8"));

            numProdutos = Integer.parseInt(arquivo.nextLine());
            for (int i = 0; (i < numProdutos && i < MAX_NOVOS_PRODUTOS); i++) {
                linha = arquivo.nextLine();
                produto = Produto.criarDoTexto(linha);
                produtosCadastrados[i] = produto;
            }
            quantosProdutos = numProdutos;

        } catch (IOException excecaoArquivo) {
            produtosCadastrados = null;
        } finally {
            arquivo.close();
        }

        return produtosCadastrados;
    }

    static void localizarProdutos() {
        String descricao;
        ProdutoNaoPerecivel produtoALocalizar;
        Produto produto = null;
        Boolean localizado = false;

        cabecalho();
        System.out.println("Informe a descrição do produto desejado");
        descricao = sc.nextLine();

        produtoALocalizar = new ProdutoNaoPerecivel(descricao, 0.1);
        for (int i = 0; (i < quantosProdutos && !localizado); i++) {
            if (produtosCadastrados[i].equals(produtoALocalizar)) {
                produto = produtosCadastrados[i];
                localizado = true;
            }
        }

        if (!localizado) {
            System.out.println("Produto não localizado!");
        } else {
            System.out.println(produto.toString());
        }
    }

    public static void salvarProdutos(String nomeArquivo) {
        FileWriter arquivo = null;

        try {
            arquivo = new FileWriter((nomeArquivo), Charset.forName("UTF-8"));

            arquivo.append(quantosProdutos + "\n");

            for (int i = 0; i < quantosProdutos; i++) {
                arquivo.append(produtosCadastrados[i].gerarDadosTexto() + "\n");
            }
            arquivo.close();
            System.out.println("Arquivo " + nomeArquivo + " salvo com sucesso.");
        } catch (IOException excecao) {
            System.out.println("Problemas no arquivo " + nomeArquivo + ". Tente novamente");
        }
    }

    static void listarTodosOsProdutos() {
        if (quantosProdutos == 0) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        for (Produto produto : produtosCadastrados) {
            System.out.println(produto.gerarDadosTexto());
        }
    }

    public static void cadastrarProduto() {
        Produto novoProduto;
        System.out.println("\n Escolha o tipo de produto: \n");
        System.out.println("1 - Produto Perecível");
        System.out.println("2 - Produto Não Perecível");
        int tipoProduto = Integer.parseInt(sc.nextLine());

        System.out.print("Informe a descrição do produto: ");
        String descricao = sc.nextLine();

        System.out.print("Informe o preço do produto: ");
        double preco = Double.parseDouble(sc.nextLine());

        System.out.print("Informe a quantidade do produto: ");
        int quantidade = Integer.parseInt(sc.nextLine());

        if (tipoProduto == 1) {
            System.out.print("Informe a data de validade dd/MM/yyyy: ");
            String dataValidadeStr = sc.nextLine();
            DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataValidade = LocalDate.parse(dataValidadeStr, formatoData);
            novoProduto = new ProdutoPerecivel(descricao, preco, quantidade, dataValidade);
        } else if (tipoProduto == 2) {
            novoProduto = new ProdutoNaoPerecivel(descricao, preco, quantidade);
        } else {
            System.out.println("Tipo de produto inválido.");
            return;
        }

        produtosCadastrados[quantosProdutos] = novoProduto;
        quantosProdutos++;

        System.out.println("Produto cadastrado com sucesso!");

        salvarProdutos(nomeArquivoDados);
    }

    public static Pedido iniciarPedido() {
        Pedido novoPedido = new Pedido();
        int opcao;
        Produto produto;

        do {
            System.out.println("Escolha um produto para incluir no pedido: ");
            for (int i = 0; i < quantosProdutos; i++) {
                System.out.println(i + " - " + produtosCadastrados[i].getDescricao());
            }
            System.out.println(quantosProdutos + " - Finalizar pedido");
            opcao = Integer.parseInt(sc.nextLine());

            if (opcao < quantosProdutos) {
                produto = produtosCadastrados[opcao];
                novoPedido.incluirProduto(produto);
            }
        } while (opcao < quantosProdutos);

        return novoPedido;
    }

    public static void finalizarPedido(Pedido pedido) {
        for (int i = 0; i < MAX_PEDIDOS; i++) {
            if (pedidosCadastrados[i] == null) {
                pedidosCadastrados[i] = pedido;
                salvarPedidos(nomeArquivoDados);
                break;
            }
        }

    }

    public static void salvarPedidos(String nomeArquivo) {
        FileWriter arquivo = null;

        try {
            arquivo = new FileWriter((nomeArquivo), Charset.forName("UTF-8"));

            arquivo.append(quantosPedidos + "\n");

            for (int i = 0; i < quantosPedidos; i++) {
                arquivo.append(pedidosCadastrados[i].resumo() + "\n");
            }
            arquivo.close();
            System.out.println("Arquivo " + nomeArquivo + " salvo com sucesso.");
        } catch (IOException excecao) {
            System.out.println("Problemas no arquivo " + nomeArquivo + ". Tente novamente");
        }
    }

    public static void main(String[] args) {
        sc = new Scanner(System.in, Charset.forName("UTF-8"));
        nomeArquivoDados = "dadosProdutos.csv";
        String nomeArquivoPedidos = "dadosPedidos.csv";
        produtosCadastrados = lerProdutos(nomeArquivoDados);
        pedidosCadastrados = new Pedido[MAX_PEDIDOS];
        Pedido pedido = null;

        int opcao = -1;

        do {
            opcao = menu();
            switch (opcao) {
                case 1 -> listarTodosOsProdutos();
                case 2 -> localizarProdutos();
                case 3 -> cadastrarProduto();
                case 4 -> pedido = iniciarPedido();
                case 5 -> finalizarPedido(pedido);
            }
            pausa();
        } while (opcao != 0);

        salvarProdutos(nomeArquivoDados);
        salvarPedidos(nomeArquivoPedidos);
        sc.close();
    }
}
