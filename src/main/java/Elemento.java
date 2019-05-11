import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class WorkThread extends Thread{
    int indice, numThreads;
    Integer menorDistancia;
    Centroide maisProx;
    List<Centroide> centroides;
    Elemento atual;

    public WorkThread(int indice, int numThreads, List<Centroide> centroides, Integer menorDistancia, Centroide maisProx, Elemento atual){
        this.indice= indice;
        this.numThreads= numThreads;
        this.centroides= centroides;
        this.menorDistancia= menorDistancia;
        this.maisProx= maisProx;
        this.atual= atual;
    }

    public void run(){
        for(int j=indice; j<centroides.size(); j+= numThreads) {
            Integer distancia;
            int soma = 0;
            int numDimensões = maisProx.numDimensões;
            for (int i = 0; i < numDimensões; i++) {
                soma += Math.pow((atual.atributos[i] - centroides.get(j).getAtributo(i)), 2);
            }
            distancia = (int) Math.sqrt(soma);
            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                maisProx = centroides.get(j);
            }
        }
    }
}

public class Elemento{
    public int numDimensões;
    protected int[] atributos;
    private Centroide associado;

    public Elemento(int numD, int[] attr){
        numDimensões= numD;
        atributos= attr.clone();
    }

    public int getAtributo(int i){
        return atributos[i];
    }

    public Centroide getAssociado() {
        return associado;
    }

    public void encontraCentroide(List<Centroide> centroides){
        Centroide maisProximo= centroides.get(0);
        Integer menorDist= Integer.MAX_VALUE;
        for(Centroide centroide : centroides){
            Integer distancia;
            int soma= 0;
            for(int i= 0; i<numDimensões; i++){
                soma+= Math.pow((atributos[i] - centroide.getAtributo(i)), 2);
            }
            distancia= (int) Math.sqrt(soma);
            if(distancia < menorDist){
                menorDist= distancia;
                maisProximo= centroide;
            }
        }

        //System.out.println(centroides.indexOf(maisProximo));
        associado= maisProximo;
    }

    public void encontraCentroidePar(List<Centroide> centroides){
        Centroide maisProximo= centroides.get(0);
        Integer menorDist= Integer.MAX_VALUE;

        //System.out.println(centroides.indexOf(maisProximo));
        associado= maisProximo;
    }
}
