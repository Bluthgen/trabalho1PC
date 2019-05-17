import java.util.ArrayList;
import java.util.List;

class Centroide extends Elemento {

    class Thread extends java.lang.Thread {

        volatile List<Elemento> elementos;
        int dimensoes;
        boolean mudado = false;
        Centroide centroide;

        Thread(List<Elemento> elementos, int dimensoes, Centroide centroide) {
            this.elementos = elementos;
            this.dimensoes = dimensoes;
            this.centroide = centroide;
        }

        @Override
        public void run() {
            int soma, num, i;
            for (i = dimensoes - numDimensoes / trabalho1PC.quantThreads; i < dimensoes; i++) {
                soma = 0;
                num = 0;
                for (Elemento elemento : elementos) {
                    if (elemento.getAssociado() == centroide) {
                        num++;
                        soma += elemento.getAtributo(i);
                    }
                }
                if (num > 0) {
                    if (setAtributo(i, soma / num)) {
                        mudado = true;
                    }
                }
            }
        }
    }


    Centroide(int numD, int[] attr) {
        super(numD, attr);
    }

    private boolean setAtributo(int i, int attr) {
        boolean mudado = atributos[i] != attr;
        atributos[i] = attr;
        return mudado;
    }

    boolean recalculaAtributos(List<Elemento> elementos) {
        int num;
        int soma;
        int i;
        boolean mudado = false;
        for (i = 0; i < numDimensoes; i++) {
            soma = 0;
            num = 0;
            for (Elemento elemento : elementos) {
                if (elemento.getAssociado() == this) {
                    num++;
                    soma += elemento.getAtributo(i);
                }
            }
            if (num > 0) {
                if (setAtributo(i, soma / num)) {
                    mudado = true;
                }
            }
        }
        return mudado;
    }

    boolean recalculaAtributosPar(List<Elemento> elementos) throws InterruptedException {
        int i, soma = 0;
        List<Thread> threads = new ArrayList<>();
        List<Integer> dimensoesParaThread = new ArrayList<>();

        for (i = 0; i < trabalho1PC.quantThreads - 1; i++) {
            dimensoesParaThread.add(i, soma + numDimensoes / trabalho1PC.quantThreads);
            soma += numDimensoes / trabalho1PC.quantThreads;
        }
        dimensoesParaThread.add(trabalho1PC.quantThreads - 1, numDimensoes);

        for (i = 0; i < trabalho1PC.quantThreads; i++) {
            threads.add(new Thread(elementos, dimensoesParaThread.get(i), this));
            threads.get(i).start();
        }

        for (Thread thread : threads) {
            thread.join();
            if (thread.mudado) {
                threads.clear();
                return true;
            }
        }
        threads.clear();
        return false;
    }
}