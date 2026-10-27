import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class QuickSort {

    // Versão recursiva
    public static void quickSort(int[] v, int ini, int fim) {
        if (ini < fim) {
            int r = ini + (int)(Math.random() * (fim - ini + 1));
            troca(v, r, fim);
            int p = particionar(v, ini, fim);
            quickSort(v, ini, p - 1);
            quickSort(v, p + 1, fim);
        }
    }

    // Partição (pivô = último elemento)
    public static int particionar(int[] v, int ini, int fim) {
        int pivo = v[fim];
        int i = ini - 1;
        for (int j = ini; j <= fim - 1; j++) {
            if (v[j] <= pivo) {
                i = i + 1;
                troca(v, i, j);
            }
        }
        troca(v, i + 1, fim);
        return i + 1;
    }

    // Versão iterativa com pilha explícita de intervalos (ini, fim)
    public static void quickSortIterativo(int[] v, int ini, int fim) {
        Deque<int[]> pilha = new ArrayDeque<>();
        pilha.push(new int[]{ini, fim});
        while (!pilha.isEmpty()) {
            int[] intervalo = pilha.pop();
            ini = intervalo[0];
            fim = intervalo[1];
            if (ini < fim) {
                int r = ini + (int)(Math.random() * (fim - ini + 1));
                troca(v, r, fim);
                int p = particionar(v, ini, fim);
                pilha.push(new int[]{ini, p - 1});
                pilha.push(new int[]{p + 1, fim});
            }
        }
    }

    private static void troca(int[] v, int a, int b) {
        int tmp = v[a];
        v[a] = v[b];
        v[b] = tmp;
    }

    public static void main(String[] args) {
        int[] a = {7, 2, 1, 8, 6, 3, 5, 4};
        quickSort(a, 0, a.length - 1);
        System.out.println("Recursivo: " + Arrays.toString(a));

        int[] b = {7, 2, 1, 8, 6, 3, 5, 4};
        quickSortIterativo(b, 0, b.length - 1);
        System.out.println("Iterativo: " + Arrays.toString(b));
    }
}