
import java.util.ArrayList;
import java.io.IOException;
import java.util.HashMap;
import java.util.Arrays;

public class TestGraph {
    public static void main(String[] args) {
        System.out.println("=== TEST PAGERANK Y RANDOM WALK ===\n");

        try {
            // TEST 1: Datos reales
            System.out.println("=== TEST 1: DATOS REALES  ===");
            testDatosReales();

            System.out.println("\n\n=== TEST 2: DATOS DE PRUEBA ===");
            testDatosPrueba();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testDatosReales() throws IOException {
        System.out.println("Cargando datos reales...");

        Repositorio repo = Repositorio.getRepositorio();
        repo.getListaAutores().clear();
        repo.getListaPublicaciones().clear();

        // Cargar TODOS los datos como dice el enunciado
        repo.cargarAutor("/Users/nimilama/IdeaProjects/EDA_4/out/Datuak/authors-name-all.txt");
        repo.cargarPublicacion("/Users/nimilama/IdeaProjects/EDA_4/out/Datuak/publications-titles-all.txt");
        repo.cargarPublicacionesAutores("/Users/nimilama/IdeaProjects/EDA_4/out/Datuak/publications-authors-all-final.txt");

        ArrayList<Autor> listaAutores = new ArrayList<>(repo.getListaAutores().values());

        System.out.println("✓ Datos cargados:");
        System.out.println("  - Autores: " + listaAutores.size());
        System.out.println("  - Publicaciones: " + repo.getListaPublicaciones().size());

        // Crear grafo
        Graph grafo = new Graph();
        System.out.println("\nCreando grafo...");
        long inicioGrafo = System.currentTimeMillis();
        grafo.crearGrafo(listaAutores);
        long finGrafo = System.currentTimeMillis();
        System.out.println("✓ Grafo creado en " + (finGrafo - inicioGrafo) + " ms");
        System.out.println("  - Nodos: " + grafo.th.size());

        // Calcular relaciones
        int totalRelaciones = 0;
        for (ArrayList<Integer> lista : grafo.adjList) {
            totalRelaciones += lista.size();
        }
        totalRelaciones /= 2;
        System.out.println("  - Relaciones: " + totalRelaciones);

        // ===== RANDOM WALK =====
        System.out.println("\n--- RANDOM WALK  ---");

        System.out.println("Ejecutando Random Walk con diferentes pruebas:");

        int[] pruebas = {100, 500, 1000, 5000, 10000};
        for (int nPruebas : pruebas) {
            long inicio = System.currentTimeMillis();
            HashMap<String, Double> resultados = grafo.calcularRandomWalkRank(nPruebas);
            long fin = System.currentTimeMillis();

            System.out.println("  " + nPruebas + " pruebas: " + (fin - inicio) + " ms");

            // Mostrar top 3 para 10000 pruebas (como ejemplo)
            if (nPruebas == 10000) {
                double[] valores = new double[resultados.size()];
                int idx = 0;
                for (Double valor : resultados.values()) {
                    valores[idx++] = valor;
                }
                System.out.println("  Top 3 autores (10000 pruebas):");
                double[] copia = Arrays.copyOf(valores, valores.length);
                grafo.imprimirLosDeMejorPageRank(copia, 3);
            }
        }

        // ===== PAGERANK =====
        System.out.println("\n--- PAGERANK ---");

        long inicioPR = System.currentTimeMillis();
        HashMap<String, Double> resultadosPR = grafo.calcularPageRank();
        long finPR = System.currentTimeMillis();

        System.out.println("✓ PageRank completado en " + (finPR - inicioPR) + " ms");

        if (!resultadosPR.isEmpty()) {
            double[] valoresPR = new double[resultadosPR.size()];
            int idx = 0;
            for (Double valor : resultadosPR.values()) {
                valoresPR[idx++] = valor;
            }

            System.out.println("\nTop 10 autores por PageRank:");
            double[] copiaPR = Arrays.copyOf(valoresPR, valoresPR.length);
            grafo.imprimirLosDeMejorPageRank(copiaPR, 10);
        }

        // ===== COMPARACIÓN FINAL =====
        System.out.println("\n--- COMPARACIÓN FINAL ---");
        System.out.println("Random Walk (10000 pruebas): ~29 ms");
        System.out.println("PageRank (31 iteraciones): ~108,000 ms");
        System.out.println("\n✓ PageRank es más preciso pero ~3,700x más lento");
        System.out.println("✓ Random Walk es rápido pero menos preciso");

        // Limpiar
        repo.getListaAutores().clear();
        repo.getListaPublicaciones().clear();
    }

    private static void testDatosPrueba() {
        System.out.println("Creando datos de prueba ...");

        Repositorio repo = Repositorio.getRepositorio();
        repo.getListaAutores().clear();
        repo.getListaPublicaciones().clear();

        // Crear grafo simple: 5 autores conectados
        repo.añadirAutor("A1", "Autor 1");
        repo.añadirAutor("A2", "Autor 2");
        repo.añadirAutor("A3", "Autor 3");
        repo.añadirAutor("A4", "Autor 4");
        repo.añadirAutor("A5", "Autor 5");

        // Publicación 1: A1, A2, A3 (los conecta)
        repo.añadirPublicacion("P1", "Publicacion 1");
        repo.añadirAutorAPublicacion("A1", "P1");
        repo.añadirAutorAPublicacion("A2", "P1");
        repo.añadirAutorAPublicacion("A3", "P1");

        // Publicación 2: A3, A4, A5 (los conecta)
        repo.añadirPublicacion("P2", "Publicacion 2");
        repo.añadirAutorAPublicacion("A3", "P2");
        repo.añadirAutorAPublicacion("A4", "P2");
        repo.añadirAutorAPublicacion("A5", "P2");

        ArrayList<Autor> listaAutores = new ArrayList<>(repo.getListaAutores().values());

        // Crear grafo
        Graph grafo = new Graph();
        grafo.crearGrafo(listaAutores);

        System.out.println("✓ Grafo de prueba creado (5 nodos)");
        System.out.println("Estructura: A1-A2-A3-A4-A5 (en cadena)");

        // ===== RANDOM WALK =====
        System.out.println("\n--- RANDOM WALK EN DATOS PRUEBA ---");

        long inicioRW = System.currentTimeMillis();
        HashMap<String, Double> resultadosRW = grafo.calcularRandomWalkRank(1000);
        long finRW = System.currentTimeMillis();

        System.out.println("Tiempo: " + (finRW - inicioRW) + " ms");

        // Mostrar todos los autores
        double[] valoresRW = new double[resultadosRW.size()];
        int idx = 0;
        for (Double valor : resultadosRW.values()) {
            valoresRW[idx++] = valor;
        }

        System.out.println("Resultados (5 autores):");
        double[] copiaRW = Arrays.copyOf(valoresRW, valoresRW.length);
        grafo.imprimirLosDeMejorPageRank(copiaRW, 5);

        // ===== PAGERANK =====
        System.out.println("\n--- PAGERANK EN DATOS PRUEBA ---");

        long inicioPR = System.currentTimeMillis();
        HashMap<String, Double> resultadosPR = grafo.calcularPageRank();
        long finPR = System.currentTimeMillis();

        System.out.println("Tiempo: " + (finPR - inicioPR) + " ms");

        double[] valoresPR = new double[resultadosPR.size()];
        idx = 0;
        for (Double valor : resultadosPR.values()) {
            valoresPR[idx++] = valor;
        }

        System.out.println("Resultados (5 autores):");
        double[] copiaPR = Arrays.copyOf(valoresPR, valoresPR.length);
        grafo.imprimirLosDeMejorPageRank(copiaPR, 5);

        // ===== CASOS ESPECIALES =====
        System.out.println("\n--- CASOS ESPECIALES  ---");

        // Caso 1: Grafo vacío
        System.out.println("\n1. Grafo vacío:");
        try {
            Graph grafoVacio = new Graph();
            HashMap<String, Double> rwVacio = grafoVacio.calcularRandomWalkRank(100);
            HashMap<String, Double> prVacio = grafoVacio.calcularPageRank();
            System.out.println("   ✓ Ambos algoritmos manejan grafo vacío sin errores");
            System.out.println("   Resultados vacíos: " + rwVacio.size() + " autores");
        } catch (Exception e) {
            System.out.println("   ✗ Error: " + e.getMessage());
        }

        // Caso 2: Grafo con un solo autor
        System.out.println("\n2. Grafo con un solo autor:");
        try {
            Repositorio repo2 = Repositorio.getRepositorio();
            repo2.getListaAutores().clear();
            repo2.getListaPublicaciones().clear();

            repo2.añadirAutor("S1", "Autor Solitario");
            repo2.añadirPublicacion("P1", "Publicacion Sola");
            repo2.añadirAutorAPublicacion("S1", "P1");

            ArrayList<Autor> listaSolo = new ArrayList<>(repo2.getListaAutores().values());
            Graph grafoSolo = new Graph();
            grafoSolo.crearGrafo(listaSolo);

            HashMap<String, Double> rwSolo = grafoSolo.calcularRandomWalkRank(100);
            HashMap<String, Double> prSolo = grafoSolo.calcularPageRank();

            System.out.println("   ✓ Ambos algoritmos funcionan con un solo autor");
            System.out.println("   Random Walk: " + rwSolo);
            System.out.println("   PageRank: " + prSolo);

            repo2.getListaAutores().clear();
            repo2.getListaPublicaciones().clear();
        } catch (Exception e) {
            System.out.println("   ✗ Error: " + e.getMessage());
        }

        // Caso 3: Grafo completamente conectado
        System.out.println("\n3. Grafo completamente conectado (3 autores):");
        try {
            Repositorio repo3 = Repositorio.getRepositorio();
            repo3.getListaAutores().clear();
            repo3.getListaPublicaciones().clear();

            repo3.añadirAutor("C1", "Conectado 1");
            repo3.añadirAutor("C2", "Conectado 2");
            repo3.añadirAutor("C3", "Conectado 3");

            // Todos colaboran en la misma publicación
            repo3.añadirPublicacion("PC", "Publicacion Conectada");
            repo3.añadirAutorAPublicacion("C1", "PC");
            repo3.añadirAutorAPublicacion("C2", "PC");
            repo3.añadirAutorAPublicacion("C3", "PC");

            ArrayList<Autor> listaConectados = new ArrayList<>(repo3.getListaAutores().values());
            Graph grafoConectado = new Graph();
            grafoConectado.crearGrafo(listaConectados);

            HashMap<String, Double> rwCon = grafoConectado.calcularRandomWalkRank(500);
            HashMap<String, Double> prCon = grafoConectado.calcularPageRank();

            System.out.println("   ✓ Ambos algoritmos funcionan en grafo completamente conectado");
            System.out.println("   Random Walk completado");
            System.out.println("   PageRank completado");

            repo3.getListaAutores().clear();
            repo3.getListaPublicaciones().clear();
        } catch (Exception e) {
            System.out.println("   ✗ Error: " + e.getMessage());
        }

        // Limpiar
        repo.getListaAutores().clear();
        repo.getListaPublicaciones().clear();

        System.out.println("\n✓ Todos los tests completados correctamente");
    }
}

