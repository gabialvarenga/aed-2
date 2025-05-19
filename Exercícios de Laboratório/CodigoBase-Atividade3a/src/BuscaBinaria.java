import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class BuscaBinaria<T> implements IBuscador<T> {
	private T[] dados;
	private long inicio;
	private long fim;
	private long comparacoes;
	private Comparator<T> comparador;

	public BuscaBinaria(T[] dados){
		this.dados = dados;
		inicio = fim = 0;
		comparacoes = 0;
	}

	@Override
	public long getComparacoes() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getTempo() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public T buscar(T dado) {
		return buscar(dado, 0, dados.length - 1);
	}

	private T buscar(T dado, int inicio, int fim) {
		if (inicio > fim) {
			throw new NoSuchElementException("Pesquisa sem sucesso");
		} else {
			int meio = (inicio + fim) / 2;
			if (dados[meio].equals(dado)) {
				return dados[meio];

			} else if (comparador.compare(dado, dados[meio]) < 0) {
				comparacoes++;
				return buscar(dado, meio + 1, fim);
			} else {
				comparacoes++;
				return buscar(dado, inicio , meio - 1);
			}
		}
	}
}
