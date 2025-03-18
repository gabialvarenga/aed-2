public class App {
    public static int somarParesFuncao(int x) {
        if ((x % 2) != 0) {
            x--;
        }
        return somarPares(x);
    }

    public static int somarPares(int num) {
        if (num == 2) {
            return num;
        }
        return somarPares(num - 2) + num;
    }

    public static double somarVetorFuncao(double[] vet) {
        return somarVetor(vet, vet.length - 1);
    }

    public static double somarVetor(double[] vet, int posicao) {
        if (posicao == 0) {
            return vet[posicao];
        }
        return somarVetor(vet, posicao - 1) + vet[posicao];
    }

    public static void main(String[] args) throws Exception {
        int x = 8;
        somarParesFuncao(x);
        System.out.println(somarPares(x));

        double[] vet = { 2.5, 10.0, 3.2, 8.8 };

        somarVetorFuncao(vet);
        System.out.println(somarVetorFuncao(vet));

    }
}
