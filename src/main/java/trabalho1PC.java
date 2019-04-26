import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class trabalho1PC {


    final static Charset ENCODING = StandardCharsets.UTF_8;

    private static void log(Object msg){
        System.out.println(String.valueOf(msg));
    }

    static void carregaElementos(List<Elemento> lista, int num) throws IOException {
        String fileName= Paths.get("").toAbsolutePath().toString() + "\\int_base_" + num + ".data";
        Path path = Paths.get(fileName);
        String linha= "";
        String[] pedacos;
        int i= 0;
        try (Scanner scanner =  new Scanner(path, ENCODING.name())){
            while (scanner.hasNextLine()){
                linha= scanner.nextLine();
                pedacos= linha.split(",");
                int[] atributos= new int[num];
                int j= 0;
                for(String pedaco : pedacos){
                    atributos[j]= Integer.parseInt(pedaco);
                    j++;
                }
                lista.add(i, new Elemento(num, atributos));
                i++;
            }
        }
    }

    static void carregaCentroide(List<Centroide> lista, int num) throws IOException {
        String fileName= Paths.get("").toAbsolutePath().toString() + "\\int_centroid_" + num + "_20.data";
        Path path = Paths.get(fileName);
        String linha= "";
        String[] pedacos;
        int i= 0;
        try (Scanner scanner =  new Scanner(path, ENCODING.name())){
            while (scanner.hasNextLine()){
                linha= scanner.nextLine();
                pedacos= linha.split(",");
                int[] atributos= new int[num];
                int j= 0;
                for(String pedaco : pedacos){
                    atributos[j]= Integer.parseInt(pedaco);
                    j++;
                }
                lista.add(i, new Centroide(num, atributos));
                i++;
            }
        }
    }

    public static void main(String[] args) throws IOException{
        //readLargerTextFile(Paths.get("").toAbsolutePath().toString() + "\\grafosPratica1\\"+args[0], grafo);
        //System.out.println(Paths.get("").toAbsolutePath().toString() + "\\int_base_" + 59 + ".data");

        List<Elemento> elementos= new ArrayList<>();
        List<Centroide> centroides= new ArrayList<>();

        switch(args[0]){
            case "161":
                carregaElementos(elementos, 161);
                carregaCentroide(centroides, 161);
                break;

            case "256":
                carregaElementos(elementos, 256);
                carregaCentroide(centroides, 256);
                break;

            case "1380":
                carregaElementos(elementos, 1380);
                carregaCentroide(centroides, 1380);
                break;

            case "1601":
                carregaElementos(elementos, 1601);
                carregaCentroide(centroides, 1601);
                break;

            default:
                carregaElementos(elementos, 59);
                carregaCentroide(centroides, 59);
                break;
        }

        boolean para= false;
        int cont= 0, numC;
        while(!para){
            for(Elemento elemento : elementos){
                elemento.encontraCentroide(centroides);
            }
            numC= 0;
            for(Centroide centroide : centroides){
                boolean resultado= centroide.recalculaAtributos(elementos);
                if(!resultado){
                    numC++;
                }
            }
            if(numC == 20){
                para= true;
            }
            cont++;
            System.out.println(cont);
        }
        int i= 0;

        /*
        for(Elemento elemento : elementos){

            System.out.println("Id: "+i+"\t"+"Classe: "+centroides.indexOf(elemento.getAssociado()));
            i++;
        }
        */
    }
}
