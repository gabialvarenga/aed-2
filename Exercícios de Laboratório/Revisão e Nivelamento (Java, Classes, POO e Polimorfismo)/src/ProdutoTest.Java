import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ProdutoTest {

    static ProdutoNaoPerecivel produtoNP;
    static ProdutoPerecivel produtoValido;
    static ProdutoPerecivel produtoComDesconto;

    @BeforeAll
    static public void prepare() {
        produtoNP = new ProdutoNaoPerecivel("Chocolate", 10, 0.1);
        produtoValido = new ProdutoPerecivel("Queijo", 20, 0.2, LocalDate.now().plusDays(10));
        produtoComDesconto = new ProdutoPerecivel("Iogurte", 10, 0.5, LocalDate.now().plusDays(5));
    }

    @Test
    public void calculaPrecoCorretamente() {
        assertEquals(11.0, produtoNP.valorDeVenda(), 0.01, "O preço do produto não perecível está incorreto!");
        assertEquals(24.0, produtoValido.valorDeVenda(), 0.01, "O preço do produto perecível válido está incorreto!");
        assertEquals(11.25, produtoComDesconto.valorDeVenda(), 0.01,"O preço do produto com desconto está incorreto!");
    }

    @Test
    public void stringComDescricaoEValor() {
        String desc = produtoNP.toString();
        assertTrue(desc.contains("Chocolate") && desc.contains("R$ 11,00"), "A descrição do produto não perecível está incorreta!");

        desc = produtoValido.toString();
        assertTrue(desc.contains("Queijo") && desc.contains("R$ 24,00") &&
                desc.contains("Data de validade: " + produtoValido.getDataDeValidade()), "A descrição do produto perecível válido está incorreta!");

        desc = produtoComDesconto.toString();
        assertTrue(desc.contains("Iogurte") && desc.contains("R$ 11,25") &&
                desc.contains("Data de validade: " + produtoComDesconto.getDataDeValidade()), "A descrição do produto com desconto está incorreta!");
    }

    @Test
    public void naoCriaProdutoComPrecoNegativo() {
        assertThrows(IllegalArgumentException.class, () -> new ProdutoNaoPerecivel("teste", -5, 0.5));
        assertThrows(IllegalArgumentException.class,
                () -> new ProdutoPerecivel("Erro", -5, 0.5, LocalDate.now().plusDays(10)));
    }

    @Test
    public void naoCriaProdutoComMargemNegativa() {
        assertThrows(IllegalArgumentException.class, () -> new ProdutoNaoPerecivel("teste", 5, -1));
        assertThrows(IllegalArgumentException.class,
                () -> new ProdutoPerecivel("Erro", 5, -1, LocalDate.now().plusDays(10)));
    }

    @Test
    public void naoCriaProdutoVencido() {
        assertThrows(IllegalArgumentException.class,
                () -> new ProdutoPerecivel("Leite vencido", 5, 0.2, LocalDate.now().minusDays(1)));
    }

    /* @Test
    public void naoCalculaPrecoParaProdutoVencido() {
        ProdutoPerecivel produtoVencido = new ProdutoPerecivel("Iogurte vencido", 10, 0.5, LocalDate.now());
        produtoVencido.setDataDeValidade(LocalDate.now().minusDays(1));

        assertThrows(IllegalStateException.class, produtoVencido::valorDeVenda);
    } */
}