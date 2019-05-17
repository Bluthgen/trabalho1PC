import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class trabalho1PC {

    static class Thread extends java.lang.Thread {

        volatile List<Elemento> elementos;

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

    private final static Charset ENCODING = StandardCharsets.UTF_8;
    private static List<Elemento> elementos = new ArrayList<>();
    private static List<Centroide> centroides = new ArrayList<>();
    static int quantThreads;

    private static int[] centro(int num, Scanner scanner) {
        String linha = scanner.nextLine();
        String[] pedacos = linha.split(",");
        int[] atributos = new int[num];
        int j = 0;
        for (String pedaco : pedacos) {
            atributos[j] = Integer.parseInt(pedaco);
            j++;
        }
        return atributos;
    }

    private static void carregaElementos(List<Elemento> lista, int num) throws IOException {
        String fileName = Paths.get("").toAbsolutePath().toString() + "\\res\\int_base_" + num + ".data";
        Path path = Paths.get(fileName);
        int i = 0;
        try (Scanner scanner = new Scanner(path, ENCODING.name())) {
            while (scanner.hasNextLine()) {
                int[] atributos = centro(num,scanner);
                lista.add(i, new Elemento(num, atributos));
                i++;
            }
        }
    }

    private static void carregaCentroide(List<Centroide> lista, int num) throws IOException {
        String fileName = Paths.get("").toAbsolutePath().toString() + "\\res\\int_centroid_" + num + "_20.data";
        Path path = Paths.get(fileName);
        int i = 0;
        try (Scanner scanner = new Scanner(path, ENCODING.name())) {
            while (scanner.hasNextLine()) {
                int[] atributos = centro(num,scanner);
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
        int numC, i, soma = 0;
        boolean para = false;
        List<Thread> threads = new ArrayList<>();
        List<ArrayList<Elemento>> elemetoParaThread = new ArrayList<>();

        for (i = 0; i < quantThreads; i++) {
            elemetoParaThread.add(new ArrayList<>());
        }

        for (i = 0; i < quantThreads; i++) {
            elemetoParaThread.get(i).addAll(elementos.subList(soma, soma + elementos.size() / quantThreads));
            soma += elementos.size() / quantThreads;
        }
        elemetoParaThread.get(0).addAll(elementos.subList(soma, elementos.size()));

        while (!para) {
            for (i = 0; i < quantThreads; i++) {
                threads.add(new Thread(elemetoParaThread.get(i)));
                threads.get(i).start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            numC = 0;
            for (Centroide centroide : centroides) {
                boolean resultado = centroide.recalculaAtributosPar(elementos);
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
        switch (args[0]) {
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

        if (args[1].equals("1")) {
            k_meansPar();
            try{
                quantThreads = Integer.parseInt(args[2]);
            }catch (Exception e){
                System.out.println("O valor passado para a quantidade de threads não é um número");
                System.exit(0);
            }
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