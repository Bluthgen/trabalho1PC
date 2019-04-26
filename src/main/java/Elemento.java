import java.util.List;

public class Elemento {
    protected int numDimensões;
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
                //System.out.println(""+ distancia + " " + centroides.indexOf(centroide));
                menorDist= distancia;
                maisProximo= centroide;
            }
        }
        associado= maisProximo;
    }
}
