import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProdutoPerecivel extends Produto {
    private static final double DESCONTO = 0.25;
    private static final int PRAZO_DESCONTO = 7;
    private LocalDate dataDeValidade;

    public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate validade) {
        super(desc, precoCusto, margemLucro);
        setDataDeValidade(validade);
    }

    public void setDataDeValidade(LocalDate dataDeValidade) {
        if (dataDeValidade.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Produto vencido não pode ser cadastrado.");
        this.dataDeValidade = dataDeValidade;
    }

    public LocalDate getDataDeValidade() {
        return dataDeValidade;
    }

    @Override
    public double valorDeVenda() {
        if (dataDeValidade.isBefore(LocalDate.now())) {
            throw new IllegalStateException("Produto vencido. Não pode ser vendido.");
        }

        double precoVenda = precoCusto * (1 + margemLucro);

        if (LocalDate.now().until(dataDeValidade).getDays() <= PRAZO_DESCONTO) {
            return precoVenda * (1 - DESCONTO);
        }

        return precoVenda;
    }

    @Override
    public String toString() {
        return super.toString() + " Data de validade: " + this.dataDeValidade;

    }

    @Override
    public String gerarDadosTexto() {
        DateTimeFormatter dataFormatada = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder sb = new StringBuilder("2;");
        sb.append(this.descricao + ";")
                .append(String.format("%.2f", precoCusto) + ";")
                .append(String.format("%.2f", margemLucro) + ";")
                .append(dataDeValidade.format(dataFormatada));

        return sb.toString();
    }
}
