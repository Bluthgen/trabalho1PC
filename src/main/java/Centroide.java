import java.util.List;

public class Centroide extends Elemento{


    public Centroide(int numD, int[] attr) {
        super(numD, attr);
    }

    private boolean setAtributo(int i, int attr){
        boolean mudado= atributos[i] != attr;
        atributos[i]= attr;
        return mudado;
    }

    public boolean recalculaAtributos(List<Elemento> elementos){
        int num= 0;
        int soma;
        int i;
        boolean mudado= false;
        for(i= 0; i<numDimensões; i++){
            soma= 0;
            for (Elemento elemento : elementos) {
                if (elemento.getAssociado() == this) {
                    num++;
                    //System.out.println("A");
                    soma += elemento.getAtributo(i);
                }
            }
            boolean resultado;
            if(num > 0) {
                resultado = setAtributo(i, soma / num);
            }else{
                resultado= false;
            }
            if(!mudado){
                mudado= resultado;
            }
        }
        return mudado;
    }
}
