import java.util.ArrayList;
import java.io.IOException;

public class TestGraph {
    public static void main(String[] args) {
        System.out.println("=== PRUEBAS DEL GRAFO ===");

        try {
            // PRIMERA PARTE: Prueba con datos reales
            System.out.println("\n--- PRUEBA CON DATOS REALES ---");
            testConDatosReales();

            // SEGUNDA PARTE: Prueba con datos de prueba
            System.out.println("\n--- PRUEBA CON DATOS DE PRUEBA ---");
            testConDatosPrueba();

        } catch (Exception e) {
            System.out.println("Error en las pruebas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testConDatosReales() throws IOException {
        Repositorio repo = Repositorio.getRepositorio();

        // Cargar datos reales
        System.out.println("Cargando datos ...");
        repo.cargarAutor("C:\\Users\\Yu Meng Ji\\IdeaProjects\\EDA\\src\\Datuak\\authors-name-all.txt");
        repo.cargarPublicacion("C:\\Users\\Yu Meng Ji\\IdeaProjects\\EDA\\src\\Datuak\\publications-titles-all.txt");
        repo.cargarPublicacionesAutores("C:\\Users\\Yu Meng Ji\\IdeaProjects\\EDA\\src\\Datuak\\publications-authors-all-final.txt");

        ArrayList<Autor> listaAutores = new ArrayList<>(repo.getListaAutores().values());

        System.out.println("Autores cargados: " + listaAutores.size());
        System.out.println("Publicaciones cargadas: " + repo.getListaPublicaciones().size());

        // Crear grafo
        Graph grafo = new Graph();
        System.out.println("Creando grafo ...");
        long startTime= System.currentTimeMillis();
        grafo.crearGrafo(listaAutores);
        long endTime= System.currentTimeMillis();
        System.out.println("Grafo creado exitosamente en "+(endTime-startTime)+" ms");

        // Estadísticas básicas del grafo
        System.out.println("Nodos: " + grafo.th.size());
        int totalRelaciones = 0;
        for (ArrayList<Integer> lista : grafo.adjList) {
            totalRelaciones += lista.size();
        }
        totalRelaciones /= 2;
        System.out.println("Relaciones: " + totalRelaciones);


        System.out.println("\n--- PRUEBA CALCULAR RANDOM WALK RANK ---");

        // Caso 1: Prueba con pocas iteraciones
        System.out.println("Caso 1: Random Walk con 100 pruebas");
        startTime = System.currentTimeMillis();
        grafo.calcularRandomWalkRank(100);
        endTime = System.currentTimeMillis();
        System.out.println("Tiempo ejecución: " + (endTime - startTime) + " ms");

        // Caso 2: Prueba con número intermedio de iteraciones
        System.out.println("Caso 2: Random Walk con 1000 pruebas");
        startTime = System.currentTimeMillis();
        grafo.calcularRandomWalkRank(1000);
        endTime = System.currentTimeMillis();
        System.out.println("Tiempo ejecución: " + (endTime - startTime) + " ms");

        // Caso 3: Prueba con muchas iteraciones
        System.out.println("Caso 3: Random Walk con 5000 pruebas");
        startTime = System.currentTimeMillis();
        grafo.calcularRandomWalkRank(5000);
        endTime = System.currentTimeMillis();
        System.out.println("Tiempo ejecución: " + (endTime - startTime) + " ms");


        System.out.println("\n--- PRUEBA CALCULAR PAGERANK ---");

        // Caso 1: Primera ejecución de PageRank
        System.out.println("Caso 1: Ejecución básica de PageRank");
        startTime = System.currentTimeMillis();
        grafo.calcularPageRank();
        endTime = System.currentTimeMillis();
        System.out.println("Tiempo ejecución: " + (endTime - startTime) + " ms");

        // Caso 2: Segunda ejecución (para ver consistencia)
        System.out.println("Caso 2: Segunda ejecución de PageRank");
        startTime = System.currentTimeMillis();
        grafo.calcularPageRank();
        endTime = System.currentTimeMillis();
        System.out.println("Tiempo ejecución: " + (endTime - startTime) + " ms");


        System.out.println("\n--- COMPARACIÓN DE RESULTADOS ---");

        // Ejecutar ambos algoritmos y comparar tiempos
        System.out.println("Ejecutando ambos algoritmos para comparar...");

        System.out.println("\n1. Ejecutando Random Walk (2000 pruebas):");
        startTime = System.currentTimeMillis();
        grafo.calcularRandomWalkRank(2000);
        endTime = System.currentTimeMillis();
        long tiempoRW = endTime - startTime;
        System.out.println("   Tiempo Random Walk: " + tiempoRW + " ms");

        System.out.println("\n2. Ejecutando PageRank:");
        startTime = System.currentTimeMillis();
        grafo.calcularPageRank();
        endTime = System.currentTimeMillis();
        long tiempoPR = endTime - startTime;
        System.out.println("   Tiempo PageRank: " + tiempoPR + " ms");

        System.out.println("\n3. Comparación de tiempos:");
        System.out.println("   Random Walk: " + tiempoRW + " ms");
        System.out.println("   PageRank: " + tiempoPR + " ms");
        System.out.println("   Diferencia: " + Math.abs(tiempoRW - tiempoPR) + " ms");


        System.out.println("\n--- PRUEBA IMPRIMIR LOS DE MEJOR PAGERANK ---");

        // Ejecutar PageRank para obtener resultados
        System.out.println("Ejecutando PageRank para obtener resultados...");
        startTime = System.currentTimeMillis();
        grafo.calcularPageRank();
        endTime = System.currentTimeMillis();
        System.out.println("PageRank completado en " + (endTime - startTime) + " ms");

        // Crear array con valores de PageRank para usar imprimirLosDeMejorPageRank
        // Primero necesitamos obtener los resultados
        System.out.println("\nProbando imprimirLosDeMejorPageRank con datos reales:");
        System.out.println("Nota: Para esta prueba necesitaríamos modificar el código para obtener los valores");
        System.out.println("de PageRank y convertirlos a un array double[]");

        // Limpiar para la siguiente prueba
        repo.getListaAutores().clear();
        repo.getListaPublicaciones().clear();
    }

    private static void testConDatosPrueba() {
        Repositorio repo = Repositorio.getRepositorio();

        // Caso 1: Grafo vacío
        System.out.println("\n--- Caso 1: Grafo vacío ---");
        try {
            ArrayList<Autor> listaVacia = new ArrayList<>();
            Graph grafoVacio = new Graph();
            grafoVacio.crearGrafo(listaVacia);
            System.out.println("Grafo vacío creado");

            // Intentar ejecutar Random Walk en grafo vacío
            System.out.println("Probando Random Walk en grafo vacío...");
            try {
                grafoVacio.calcularRandomWalkRank(100);
                System.out.println("✗ Random Walk no debería funcionar en grafo vacío");
            } catch (Exception e) {
                System.out.println("✓ Random Walk lanza excepción en grafo vacío");
            }

            // Intentar ejecutar PageRank en grafo vacío
            System.out.println("Probando PageRank en grafo vacío...");
            try {
                grafoVacio.calcularPageRank();
                System.out.println("✗ PageRank no debería funcionar en grafo vacío");
            } catch (Exception e) {
                System.out.println("✓ PageRank lanza excepción en grafo vacío");
            }

            // Probando imprimirLosDeMejorPageRank en grafo vacío
            System.out.println("Probando imprimirLosDeMejorPageRank en grafo vacío...");
            try {
                double[] valoresVacios = new double[0];
                grafoVacio.imprimirLosDeMejorPageRank(valoresVacios, 5);
                System.out.println("✓ imprimirLosDeMejorPageRank ejecutado en grafo vacío (array vacío)");
            } catch (Exception e) {
                System.out.println("✗ imprimirLosDeMejorPageRank lanzó excepción: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Excepción esperada: " + e.getMessage());
        }

        // Caso 2: Grafo con un solo autor
        System.out.println("\n--- Caso 2: Grafo con un solo autor ---");
        repo.getListaAutores().clear();
        repo.getListaPublicaciones().clear();

        repo.añadirAutor("S1", "Autor Solitario");
        repo.añadirPublicacion("PS1", "Publicación Solitaria");
        repo.añadirAutorAPublicacion("S1", "PS1");

        ArrayList<Autor> listaSolitario = new ArrayList<>(repo.getListaAutores().values());
        Graph grafoSolitario = new Graph();
        grafoSolitario.crearGrafo(listaSolitario);
        System.out.println("Grafo con un solo autor creado (1 nodo)");

        // Probando Random Walk
        System.out.println("Probando Random Walk con un solo autor...");
        grafoSolitario.calcularRandomWalkRank(100);
        System.out.println("✓ Random Walk ejecutado con un solo autor");

        // Probando PageRank
        System.out.println("Probando PageRank con un solo autor...");
        grafoSolitario.calcularPageRank();
        System.out.println("✓ PageRank ejecutado con un solo autor");

        // Probando imprimirLosDeMejorPageRank con un solo autor
        System.out.println("Probando imprimirLosDeMejorPageRank con un solo autor...");
        double[] valoresSolitario = {0.5}; // Valor de ejemplo
        grafoSolitario.imprimirLosDeMejorPageRank(valoresSolitario, 3);
        System.out.println("✓ imprimirLosDeMejorPageRank ejecutado con un solo autor");

        // Caso 3: Grafo completamente conectado
        System.out.println("\n--- Caso 3: Grafo completamente conectado ---");
        repo.getListaAutores().clear();
        repo.getListaPublicaciones().clear();

        // Crear 3 autores
        repo.añadirAutor("C1", "Autor Conectado 1");
        repo.añadirAutor("C2", "Autor Conectado 2");
        repo.añadirAutor("C3", "Autor Conectado 3");

        // Crear una publicación con los 3 autores (completamente conectados)
        repo.añadirPublicacion("PC1", "Publicación Conectada");
        repo.añadirAutorAPublicacion("C1", "PC1");
        repo.añadirAutorAPublicacion("C2", "PC1");
        repo.añadirAutorAPublicacion("C3", "PC1");

        ArrayList<Autor> listaConectada = new ArrayList<>(repo.getListaAutores().values());
        Graph grafoConectado = new Graph();
        grafoConectado.crearGrafo(listaConectada);
        System.out.println("Grafo completamente conectado creado");

        // Probando Random Walk
        System.out.println("Probando Random Walk en grafo completamente conectado...");
        long inicio = System.currentTimeMillis();
        grafoConectado.calcularRandomWalkRank(500);
        long fin = System.currentTimeMillis();
        System.out.println("✓ Random Walk ejecutado en " + (fin - inicio) + " ms");

        // Probando PageRank
        System.out.println("Probando PageRank en grafo completamente conectado...");
        inicio = System.currentTimeMillis();
        grafoConectado.calcularPageRank();
        fin = System.currentTimeMillis();
        System.out.println("✓ PageRank ejecutado en " + (fin - inicio) + " ms");

        // Probando imprimirLosDeMejorPageRank con grafo conectado
        System.out.println("Probando imprimirLosDeMejorPageRank en grafo completamente conectado...");
        double[] valoresConectados = {0.3, 0.3, 0.3}; // Valores similares para autores conectados
        grafoConectado.imprimirLosDeMejorPageRank(valoresConectados, 2);
        System.out.println("✓ imprimirLosDeMejorPageRank ejecutado en grafo completamente conectado");

        // Caso 4: Grafo en estrella (un autor central conectado a muchos)
        System.out.println("\n--- CASO 4: Grafo en estrella ---");
        repo.getListaAutores().clear();
        repo.getListaPublicaciones().clear();

        // Crear autores
        repo.añadirAutor("E1", "Autor Centro");
        repo.añadirAutor("E2", "Autor Periferia 1");
        repo.añadirAutor("E3", "Autor Periferia 2");
        repo.añadirAutor("E4", "Autor Periferia 3");
        repo.añadirAutor("E5", "Autor Periferia 4");

        // Crear publicaciones que conecten el centro con cada periferia
        repo.añadirPublicacion("PE1", "Pub Centro-Periferia 1");
        repo.añadirAutorAPublicacion("E1", "PE1");
        repo.añadirAutorAPublicacion("E2", "PE1");

        repo.añadirPublicacion("PE2", "Pub Centro-Periferia 2");
        repo.añadirAutorAPublicacion("E1", "PE2");
        repo.añadirAutorAPublicacion("E3", "PE2");

        repo.añadirPublicacion("PE3", "Pub Centro-Periferia 3");
        repo.añadirAutorAPublicacion("E1", "PE3");
        repo.añadirAutorAPublicacion("E4", "PE3");

        repo.añadirPublicacion("PE4", "Pub Centro-Periferia 4");
        repo.añadirAutorAPublicacion("E1", "PE4");
        repo.añadirAutorAPublicacion("E5", "PE4");

        ArrayList<Autor> listaEstrella = new ArrayList<>(repo.getListaAutores().values());
        Graph grafoEstrella = new Graph();
        grafoEstrella.crearGrafo(listaEstrella);
        System.out.println("Grafo en estrella creado");

        // Probando Random Walk
        System.out.println("Probando Random Walk en grafo estrella...");
        inicio = System.currentTimeMillis();
        grafoEstrella.calcularRandomWalkRank(1000);
        fin = System.currentTimeMillis();
        System.out.println("✓ Random Walk ejecutado en " + (fin - inicio) + " ms");

        // Probando PageRank
        System.out.println("Probando PageRank en grafo estrella...");
        inicio = System.currentTimeMillis();
        grafoEstrella.calcularPageRank();
        fin = System.currentTimeMillis();
        System.out.println("✓ PageRank ejecutado en " + (fin - inicio) + " ms");

        // Probando imprimirLosDeMejorPageRank con grafo estrella
        System.out.println("Probando imprimirLosDeMejorPageRank en grafo estrella...");
        double[] valoresEstrella = {0.5, 0.125, 0.125, 0.125, 0.125}; // Centro alto, periferias bajas
        grafoEstrella.imprimirLosDeMejorPageRank(valoresEstrella, 3);
        System.out.println("✓ imprimirLosDeMejorPageRank ejecutado en grafo estrella");

        // Caso 5: Grafo lineal
        System.out.println("\n--- Caso 5: Grafo lineal ---");
        repo.getListaAutores().clear();
        repo.getListaPublicaciones().clear();

        // Crear autores
        repo.añadirAutor("L1", "Autor Lineal 1");
        repo.añadirAutor("L2", "Autor Lineal 2");
        repo.añadirAutor("L3", "Autor Lineal 3");
        repo.añadirAutor("L4", "Autor Lineal 4");

        // Crear publicaciones que formen una cadena lineal
        repo.añadirPublicacion("PL1", "Pub Lineal 1-2");
        repo.añadirAutorAPublicacion("L1", "PL1");
        repo.añadirAutorAPublicacion("L2", "PL1");

        repo.añadirPublicacion("PL2", "Pub Lineal 2-3");
        repo.añadirAutorAPublicacion("L2", "PL2");
        repo.añadirAutorAPublicacion("L3", "PL2");

        repo.añadirPublicacion("PL3", "Pub Lineal 3-4");
        repo.añadirAutorAPublicacion("L3", "PL3");
        repo.añadirAutorAPublicacion("L4", "PL3");

        ArrayList<Autor> listaLineal = new ArrayList<>(repo.getListaAutores().values());
        Graph grafoLineal = new Graph();
        grafoLineal.crearGrafo(listaLineal);
        System.out.println("Grafo lineal creado (4 nodos en cadena)");

        // Probando Random Walk
        System.out.println("Probando Random Walk en grafo lineal...");
        inicio = System.currentTimeMillis();
        grafoLineal.calcularRandomWalkRank(800);
        fin = System.currentTimeMillis();
        System.out.println("✓ Random Walk ejecutado en " + (fin - inicio) + " ms");

        // Probando PageRank
        System.out.println("Probando PageRank en grafo lineal...");
        inicio = System.currentTimeMillis();
        grafoLineal.calcularPageRank();
        fin = System.currentTimeMillis();
        System.out.println("✓ PageRank ejecutado en " + (fin - inicio) + " ms");

        // Probando imprimirLosDeMejorPageRank con grafo lineal
        System.out.println("Probando imprimirLosDeMejorPageRank en grafo lineal...");
        double[] valoresLineal = {0.3, 0.25, 0.25, 0.2}; // Valores decrecientes
        grafoLineal.imprimirLosDeMejorPageRank(valoresLineal, 2);
        System.out.println("✓ imprimirLosDeMejorPageRank ejecutado en grafo lineal");

        // CASO 6: GRAFO COMPLEJO (como el de los datos de prueba originales)
        System.out.println("\n--- CASO 6: GRAFO COMPLEJO (7 autores) ---");
        repo.getListaAutores().clear();
        repo.getListaPublicaciones().clear();

        // Crear datos de prueba originales (copiado de tu código)
        repo.añadirAutor("A1", "Autor 1");
        repo.añadirAutor("A2", "Autor 2");
        repo.añadirAutor("A3", "Autor 3");
        repo.añadirAutor("A4", "Autor 4");
        repo.añadirAutor("A5", "Autor 5");
        repo.añadirAutor("A6", "Autor 6");
        repo.añadirAutor("A7", "Autor 7");

        repo.añadirPublicacion("P1", "Publicacion 1");
        repo.añadirPublicacion("P2", "Publicacion 2");
        repo.añadirPublicacion("P3", "Publicacion 3");
        repo.añadirPublicacion("P4", "Publicacion 4");
        repo.añadirPublicacion("P5", "Publicacion 5");
        repo.añadirPublicacion("P6", "Publicacion 6");

        repo.añadirAutorAPublicacion("A1", "P1");
        repo.añadirAutorAPublicacion("A2", "P1");

        repo.añadirAutorAPublicacion("A2", "P2");
        repo.añadirAutorAPublicacion("A3", "P2");

        repo.añadirAutorAPublicacion("A3", "P3");
        repo.añadirAutorAPublicacion("A4", "P3");

        repo.añadirAutorAPublicacion("A1", "P4");
        repo.añadirAutorAPublicacion("A5", "P4");

        repo.añadirAutorAPublicacion("A5", "P5");
        repo.añadirAutorAPublicacion("A6", "P5");

        repo.añadirAutorAPublicacion("A6", "P6");
        repo.añadirAutorAPublicacion("A7", "P6");

        ArrayList<Autor> listaCompleja = new ArrayList<>(repo.getListaAutores().values());
        Graph grafoComplejo = new Graph();
        grafoComplejo.crearGrafo(listaCompleja);
        System.out.println("Grafo complejo creado");

        // Probando Random Walk con diferentes configuraciones
        System.out.println("\nProbando Random Walk con diferentes configuraciones:");

        System.out.println("1. Random Walk con 500 pruebas:");
        inicio = System.currentTimeMillis();
        grafoComplejo.calcularRandomWalkRank(500);
        fin = System.currentTimeMillis();
        System.out.println("   Tiempo: " + (fin - inicio) + " ms");

        System.out.println("2. Random Walk con 2000 pruebas:");
        inicio = System.currentTimeMillis();
        grafoComplejo.calcularRandomWalkRank(2000);
        fin = System.currentTimeMillis();
        System.out.println("   Tiempo: " + (fin - inicio) + " ms");

        System.out.println("3. Random Walk con 5000 pruebas:");
        inicio = System.currentTimeMillis();
        grafoComplejo.calcularRandomWalkRank(5000);
        fin = System.currentTimeMillis();
        System.out.println("   Tiempo: " + (fin - inicio) + " ms");

        // Probando PageRank
        System.out.println("\nProbando PageRank:");
        inicio = System.currentTimeMillis();
        grafoComplejo.calcularPageRank();
        fin = System.currentTimeMillis();
        System.out.println("✓ PageRank ejecutado en " + (fin - inicio) + " ms");

        // Probando imprimirLosDeMejorPageRank con grafo complejo
        System.out.println("\nProbando imprimirLosDeMejorPageRank en grafo complejo...");
        double[] valoresComplejo = {0.25, 0.2, 0.15, 0.12, 0.10, 0.09, 0.09}; // Valores decrecientes
        System.out.println("Mostrando los 3 mejores autores:");
        grafoComplejo.imprimirLosDeMejorPageRank(valoresComplejo, 3);
        System.out.println("Mostrando los 5 mejores autores:");
        // Reseteamos los valores (pues el método modifica el array)
        double[] valoresComplejo2 = {0.25, 0.2, 0.15, 0.12, 0.10, 0.09, 0.09};
        grafoComplejo.imprimirLosDeMejorPageRank(valoresComplejo2, 5);
        System.out.println("✓ imprimirLosDeMejorPageRank ejecutado en grafo complejo");
    }
}
