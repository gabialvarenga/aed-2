public class ProdutoNaoPerecivel extends Produto {

    public ProdutoNaoPerecivel(String desc, double precoCusto, double margemLucro) {
        super(desc, precoCusto, margemLucro);
    }

    public ProdutoNaoPerecivel(String desc, double precoCusto) {
        super(desc, precoCusto);

    }

    @Override
    public double valorDeVenda() {
        return (precoCusto * (1 + margemLucro));
    }

    @Override
    public String toString() {
        return super.toString();
    }

   
    @Override
    public String gerarDadosTexto() {
       StringBuilder sb = new StringBuilder("1;");
       sb.append(this.descricao + ";")
       .append(String.format("%.2f", precoCusto) + ";")
       .append(String.format("%.2f", margemLucro) + ";");

       return sb.toString();
    }

}