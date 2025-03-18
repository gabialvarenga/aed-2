import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * MIT License
 *
 * Copyright(c) 2025 João Caram <caram@pucminas.br>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

public abstract class Produto {
    private static final double MARGEM_PADRAO = 0.2;
    protected String descricao;
    protected double precoCusto;
    protected double margemLucro;

    /**
     * Inicializador privado. Os valores default em caso de erro são:
     * "Produto sem descrição", R$0.01, 1 unidade, 0 unidades
     * 
     * @param desc          Descrição do produto (mínimo 3 caracteres)
     * @param precoCusto    Preço do produto (mínimo 0.01)
     * @param quant         Quantidade atual no estoque (mínimo 0)
     * @param estoqueMinimo Estoque mínimo (mínimo 0)
     * @param validade      Data de validade passada como parâmetro
     */

    private void init(String desc, double precoCusto, double margemLucro) {

        if (desc.length() < 3 || precoCusto <= 0 || margemLucro <= 0)
            throw new IllegalArgumentException("Valores inválidos para o produto");
        descricao = desc;
        this.precoCusto = precoCusto;
        this.margemLucro = margemLucro;
    }

    /**
     * Construtor completo. Os valores default em caso de erro são:
     * "Produto sem descrição", R$0.01, 1 unidade, 0 unidades
     * 
     * @param desc          Descrição do produto (mínimo 3 caracteres)
     * @param preco         Preço do produto (mínimo 0.01)
     * @param quant         Quantidade atual no estoque (mínimo 0)
     * @param estoqueMinimo Estoque mínimo (mínimo 0)
     * @param validade      Data de validade passada como parâmetro
     */
    protected Produto(String desc, double precoCusto, double margemLucro) {
        init(desc, precoCusto, margemLucro);
    }

    /**
     * Construtor sem estoque mínimo - fica considerado como 0.
     * Os valores default em caso de erro são:
     * "Produto sem descrição", R$0.01, 1 unidade, 0 unidades
     * 
     * @param desc     Descrição do produto (mínimo 3 caracteres)
     * @param preco    Preço do produto (mínimo 0.01)
     * @param quant    Quantidade atual no estoque (mínimo 0)
     * @param validade Data de validade passada como parâmetro
     */
    protected Produto(String desc, double precoCusto) {
        init(desc, precoCusto, MARGEM_PADRAO);
    }

    /**
     * Retorna o valor de venda do produto, considerando seu preço de custo e margem
     * de lucro
     * 
     * @return Valor de venda do produto (double, positivo)
     */
    public abstract double valorDeVenda();

    /**
     * Descrição em string do produto, contendo sua descrição e o valor de venda.
     * 
     * @return String com o formato:
     *         [NOME]: R$ [VALOR DE VENDA]
     */
    @Override
    public String toString() {
        NumberFormat moeda = NumberFormat.getCurrencyInstance();

        return String.format("NOME: %s: %s", descricao, moeda.format(valorDeVenda()));
    }

    @Override
    public boolean equals(Object obj) {
        Produto outro = (Produto) obj;
        return this.descricao.toLowerCase().equals(outro.descricao.toLowerCase());
    }


    static Produto criarDoTexto(String linha) {
        String[] dadosProduto;
        double precoCusto, margemLucro;
        int tipo;
        Produto novoProduto = null;
        LocalDate dataDeValidade;
        DateTimeFormatter formatoData;

        dadosProduto = linha.split(";");

        tipo = Integer.parseInt(dadosProduto[0]);
        precoCusto = Double.parseDouble(dadosProduto[2].replace(",", "."));
        margemLucro = Double.parseDouble(dadosProduto[3].replace(",", "."));

        if (tipo == 1) {
            novoProduto = new ProdutoNaoPerecivel(dadosProduto[1], precoCusto, margemLucro);
        } else {
            formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dataDeValidade = LocalDate.parse(dadosProduto[4], formatoData);
            novoProduto = new ProdutoPerecivel(dadosProduto[1], precoCusto, margemLucro, dataDeValidade);
        }

        return novoProduto;
    }

    
    public abstract String gerarDadosTexto();

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(double precoCusto) {
        this.precoCusto = precoCusto;
    }

    public double getMargemLucro() {
        return margemLucro;
    }

    public void setMargemLucro(double margemLucro) {
        this.margemLucro = margemLucro;
    }

    
}