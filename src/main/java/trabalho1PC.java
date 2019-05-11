import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Thread extends java.lang.Thread {

    public List<Elemento> elementos;

    Thread(List<Elemento> elementos) {
        this.elementos = elementos;
    }

    @Override
    public void run() {
        for (Elemento elemento : elementos) {
            elemento.encontraCentroide(trabalho1PC.centroides);
        }
    }
}

public class trabalho1PC {

    final static Charset ENCODING = StandardCharsets.UTF_8;
    private static List<Elemento> elementos = new ArrayList<>();
    public static List<Centroide> centroides = new ArrayList<>();

    static void carregaElementos(List<Elemento> lista, int num) throws IOException {
        String fileName = Paths.get("").toAbsolutePath().toString() + "\\res\\int_base_" + num + ".data";
        Path path = Paths.get(fileName);
        String linha = "";
        String[] pedacos;
        int i = 0;
        try (Scanner scanner = new Scanner(path, ENCODING.name())) {
            while (scanner.hasNextLine()) {
                linha = scanner.nextLine();
                pedacos = linha.split(",");
                int[] atributos = new int[num];
                int j = 0;
                for (String pedaco : pedacos) {
                    atributos[j] = Integer.parseInt(pedaco);
                    j++;
                }
                lista.add(i, new Elemento(num, atributos));
                i++;
            }
        }
    }

    static void carregaCentroide(List<Centroide> lista, int num) throws IOException {
        String fileName = Paths.get("").toAbsolutePath().toString() + "\\res\\int_centroid_" + num + "_20.data";
        Path path = Paths.get(fileName);
        String linha = "";
        String[] pedacos;
        int i = 0;
        try (Scanner scanner = new Scanner(path, ENCODING.name())) {
            while (scanner.hasNextLine()) {
                linha = scanner.nextLine();
                pedacos = linha.split(",");
                int[] atributos = new int[num];
                int j = 0;
                for (String pedaco : pedacos) {
                    atributos[j] = Integer.parseInt(pedaco);
                    j++;
                }
                lista.add(i, new Centroide(num, atributos));
                i++;
            }
        }
    }

    static private void k_meansSeq() {
        boolean para = false;
        int numC;
        while (!para) {
            for (Elemento elemento : elementos) {
                elemento.encontraCentroide(centroides);
            }
            numC = 0;
            for (Centroide centroide : centroides) {
                boolean mudado = centroide.recalculaAtributos(elementos);
                if (!mudado) {
                    numC++;
                }
            }
            if (numC == 20) {
                para = true;
            }
        }
    }

    static private void k_meansPar() throws InterruptedException {
        int numC, i, quantThreads = 2, som=0;
        boolean para = false;
        List<Thread> threads = new ArrayList<>();
        List<ArrayList<Elemento>> elemetoParaThread = new ArrayList<>();

        for (i = 0; i < quantThreads; i++) {
            elemetoParaThread.add(new ArrayList<>());
        }

        for (i = 0; i < quantThreads; i++) {
            elemetoParaThread.get(i).addAll(elementos.subList(som,som+elementos.size()/quantThreads));
            som += elementos.size()/quantThreads;
        }
        elemetoParaThread.get(0).addAll(elementos.subList(som,elementos.size()));

        while (!para) {
            for (i = 0; i < quantThreads; i++) {
                threads.add(new Thread(elemetoParaThread.get(i)));
            }

            for (Thread thread : threads) {
                thread.start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            numC = 0;
            for (Centroide centroide : centroides) {
                boolean resultado = centroide.recalculaAtributosPar(elementos,quantThreads);
                if (!resultado) {
                    numC++;
                }
            }
            if (numC == 20) {
                para = true;
            }
            threads.clear();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        long startTempo = System.currentTimeMillis();
        String tamanhoDaBase = "161";
        String tipo = "0";
        switch (tamanhoDaBase) {
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
        if (tipo.equals("0")) {
            k_meansPar();
            System.out.println("Execução Paralela");
        } else {
            k_meansSeq();
            System.out.println("Execução Sequencial");
        }
        int i = 0;
        for (Elemento elemento : elementos) {

            System.out.println("Id: " + i + "\t" + "Classe: " + centroides.indexOf(elemento.getAssociado()));
            i++;
        }
        System.out.println(System.currentTimeMillis() - startTempo);
    }
}
