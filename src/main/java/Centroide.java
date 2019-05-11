import java.util.ArrayList;
import java.util.List;

class ThreadC extends java.lang.Thread {

    public List<Elemento> elementos;
    public int num = 0, soma = 0, dimensao;
    public Centroide centroide;

    ThreadC(List<Elemento> elementos, Centroide centroide, int dimensao) {
        this.elementos = elementos;
        this.centroide = centroide;
        this.dimensao = dimensao;
    }

    @Override
    public void run() {
        for (Elemento elemento : elementos) {
            if (elemento.getAssociado() == centroide) {
                num++;
                soma += elemento.getAtributo(dimensao);
            }
        }
    }
}

public class Centroide extends Elemento {


    public Centroide(int numD, int[] attr) {
        super(numD, attr);
    }

    private boolean setAtributo(int i, int attr) {
        boolean mudado = atributos[i] != attr;
        atributos[i] = attr;
        return mudado;
    }

    public boolean recalculaAtributos(List<Elemento> elementos) {
        int num;
        int soma;
        int i;
        boolean mudado = false;
        for (i = 0; i < numDimensões; i++) {
            soma = 0;
            num = 0;
            for (Elemento elemento : elementos) {
                if (elemento.getAssociado() == this) {
                    num++;
                    soma += elemento.getAtributo(i);
                }
            }
            boolean resultado;
            if (num > 0) {
                resultado = setAtributo(i, soma / num);
            } else {
                resultado = false;
            }
            if (!mudado && resultado) {
                mudado = true;
            }
        }
        return mudado;
    }

    public boolean recalculaAtributosPar(List<Elemento> elementos, int quantThreads) throws InterruptedException {
        int num;
        int soma, som = 0;
        int i;
        boolean mudado = false;
        List<ThreadC> threads = new ArrayList<>();
        List<ArrayList<Elemento>> elemetoParaThread = new ArrayList<>();

        for (i = 0; i < quantThreads; i++) {
            elemetoParaThread.add(new ArrayList<>());
        }

        for (i = 0; i < quantThreads; i++) {
            elemetoParaThread.get(i).addAll(elementos.subList(som, som + elementos.size() / quantThreads));
            som += elementos.size() / quantThreads;
        }
        elemetoParaThread.get(0).addAll(elementos.subList(som, elementos.size()));

        for (i = 0; i < numDimensões; i++) {
            soma = 0;
            num = 0;

            for (i = 0; i < quantThreads; i++) {
                threads.add(new ThreadC(elemetoParaThread.get(i), this, i));
            }

            for (ThreadC thread : threads) {
                thread.start();
            }

            for (ThreadC thread : threads) {
                thread.join();
            }

            for (ThreadC thread : threads) {
                num += thread.num;
                soma += thread.soma;
            }

            threads.clear();
            boolean resultado;
            if (num > 0) {
                resultado = setAtributo(i, soma / num);
            } else {
                resultado = false;
            }
            if (!mudado && resultado) {
                mudado = true;
            }
        }
        System.out.println("sai");
        return mudado;
    }
}
