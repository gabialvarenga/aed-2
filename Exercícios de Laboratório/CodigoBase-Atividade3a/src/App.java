import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class App {

	/** Nome do arquivo de dados. O arquivo deve estar localizado na raiz do projeto */
    static String nomeArquivoDados = "produtos.txt";
    
    /** Scanner para leitura de dados do teclado */
    static Scanner teclado;

    /** Vetor de produtos cadastrados */
    static Produto[] produtosCadastrados;

    /** Quantidade de produtos cadastrados atualmente no vetor */
    static int quantosProdutos = 0;

    static IOrdenator<Produto> ordenador;

    static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
    static void pausa() {
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    /** Cabeçalho principal da CLI do sistema */
    static void cabecalho() {
        System.out.println("AEDs II COMÉRCIO DE COISINHAS");
        System.out.println("=============================");
    }
    
    static <T extends Number> T lerOpcao(String mensagem, Class<T> classe) {
        
    	T valor;
        
    	System.out.println(mensagem);
    	try {
            valor = classe.getConstructor(String.class).newInstance(teclado.nextLine());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException 
        		| InvocationTargetException | NoSuchMethodException | SecurityException e) {
            return null;
        }
        return valor;
    }
    
    /** Imprime o menu principal, lê a opção do usuário e a retorna (int).
     * Perceba que poderia haver uma melhor modularização com a criação de uma classe Menu.
     * @return Um inteiro com a opção do usuário.
    */
    static int menu() {
        cabecalho();
        System.out.println("1 - Ordenar produtos");
        System.out.println("2 - Embaralhar produtos");
        System.out.println("3 - Busca sequencial por produto");
        System.out.println("4 - Busca binária por produto");
        System.out.println("5 - Listar todos os produtos");
        System.out.println("0 - Finalizar");
        return lerOpcao("Digite sua opção: ", Integer.class);
    }
    
    /**
     * Lê os dados de um arquivo-texto e retorna um vetor de produtos. Arquivo-texto no formato
     * N  (quantidade de produtos) <br/>
     * tipo;descrição;preçoDeCusto;margemDeLucro;[dataDeValidade] <br/>
     * Deve haver uma linha para cada um dos produtos. Retorna um vetor vazio em caso de problemas com o arquivo.
     * @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
     * @return Um vetor com os produtos carregados, ou vazio em caso de problemas de leitura.
     */
    static Produto[] lerProdutos(String nomeArquivoDados) {
    	
    	Scanner arquivo = null;
    	int numProdutos;
    	String linha;
    	Produto produto;
    	Produto[] produtosCadastrados;
    	
    	try {
    		arquivo = new Scanner(new File(nomeArquivoDados), Charset.forName("UTF-8"));
    		
    		numProdutos = Integer.parseInt(arquivo.nextLine());
    		produtosCadastrados = new Produto[numProdutos];
    		
    		for (int i = 0; i < numProdutos; i++) {
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
    
    /** Localiza um produto no vetor de produtos cadastrados, a partir do nome de produto informado pelo usuário, e o retorna. 
     *  A busca não é sensível ao caso. Em caso de não encontrar o produto, retorna null */
    static Produto localizarProduto(IBuscador<Produto> buscador) {
        cabecalho();
        System.out.println("Localizando um produto...");
        System.out.print("Digite a descrição do produto: ");
        String desc = teclado.nextLine();
        Produto busca = new ProdutoNaoPerecivel(desc, 0.1);
        Produto encontrado = buscador.buscar(busca);
        
        return encontrado;
    }

    private static void mostrarProduto(Produto produto, IBuscador<Produto> buscador) {
    	
        cabecalho();
        StringBuilder  mensagem = new StringBuilder("Produto não encontrado.\n");
        
        if (produto != null) {
            mensagem = new StringBuilder(String.format("%s\n", produto));            
        }

        mensagem.append(String.format("\nTotal de comparações: %d\n", buscador.getComparacoes()));
        mensagem.append(String.format("Tempo gasto: %,.0f nanossegundos\n", buscador.getTempo()));
        
        System.out.println(mensagem.toString());
    }
    
    static void ordenarProdutos(){
    	
    	cabecalho();
        
        ordenador = new Mergesort<>();
        produtosCadastrados = ordenador.ordenar(produtosCadastrados, Produto::compareTo);

        ordenador = null;
    }

    static void embaralharProdutos(){
        Collections.shuffle(Arrays.asList(produtosCadastrados));
    }

    /** Lista todos os produtos cadastrados, numerados, um por linha */
    static void listarTodosOsProdutos() {
    	
        cabecalho();
        System.out.println("\nPRODUTOS CADASTRADOS:");
        for (int i = 0; i < quantosProdutos; i++) {
        	System.out.println(String.format("%02d - %s", (i + 1), produtosCadastrados[i].toString()));
        }
    }
    
    static void fazerBuscaSequencial() {
        IBuscador<Produto> buscador = new BuscaSequencial<>(produtosCadastrados);
        Produto prod = localizarProduto(buscador);
        mostrarProduto(prod, buscador);
    }

    static void fazerBuscaBinaria() {
        IBuscador<Produto> buscador = new BuscaBinaria<>(produtosCadastrados, Produto::compareTo);
        Produto prod = localizarProduto(buscador);
        mostrarProduto(prod, buscador);
    }

	public static void main(String[] args) {
		teclado = new Scanner(System.in, Charset.forName("UTF-8"));
        nomeArquivoDados = "produtos.txt";
        produtosCadastrados = lerProdutos(nomeArquivoDados);

        embaralharProdutos();

        int opcao = -1;
      
        do{
            opcao = menu();
            switch (opcao) {
            	case 1 -> ordenarProdutos();
            	case 2 -> embaralharProdutos();
            	case 3 -> fazerBuscaSequencial();
            	case 4 -> fazerBuscaBinaria();
            	case 5 -> listarTodosOsProdutos();
            	case 0 -> System.out.println("FLW VLW OBG VLT SMP.");
            }
            pausa();
        }while(opcao != 0);       

        teclado.close();    
    }
}
