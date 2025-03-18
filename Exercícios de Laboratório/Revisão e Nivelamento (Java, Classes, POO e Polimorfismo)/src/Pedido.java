import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Pedido {
    private static final int MAX_PRODUTOS = 10;
    private Produto[] produtos;
    private LocalDate dataPedido;
    private int qtdProdutos;

    public Pedido() {
        produtos = new Produto[MAX_PRODUTOS];
        dataPedido = LocalDate.now();
        qtdProdutos = 0;
    }

    public int incluirProduto(Produto novo) {
        if (qtdProdutos < MAX_PRODUTOS) {
            produtos[qtdProdutos] = novo;
            qtdProdutos++;
            return 0;
        }
        return qtdProdutos;
    }

    public double valorFinal() {
        double total = 0;
        for (int i = 0; i < qtdProdutos; i++) {
            total += produtos[i].getPrecoCusto();
        }
        return total;
    }

    public String toString() {

        StringBuilder stringPedido = new StringBuilder();
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        stringPedido.append("Pedido na data " + formatoData.format(dataPedido) + "\n");

        for (int i = 0; i < qtdProdutos; i++) {
            stringPedido.append(produtos[i].toString() + "\n");
        }

        stringPedido.append("Valor a pagar: R$ " + String.format("%.2f", valorFinal()));

        return stringPedido.toString();
    }

    public String resumo() {
        DateTimeFormatter dataFormatada = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido com ")
                .append(this.qtdProdutos)
                .append(" produtos em ")
                .append(dataPedido.format(dataFormatada))
                .append(", valor total de R$ ")
                .append(String.format("%.2f", valorFinal()));

        return sb.toString();
    }
}
