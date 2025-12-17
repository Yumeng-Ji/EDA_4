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
        repo.cargarAutor("/Users/nimilama/IdeaProjects/EDA_3/out/Datuak/authors-name-all.txt");
        repo.cargarPublicacion("/Users/nimilama/IdeaProjects/EDA_3/out/Datuak/publications-titles-all.txt");
        repo.cargarPublicacionesAutores("/Users/nimilama/IdeaProjects/EDA_3/out/Datuak/publications-authors-all-final.txt");

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

        // Probar con algunos autores reales
        if (listaAutores.size() >= 3) {
            String autor1 = listaAutores.get(0).getNombre();
            String autor2 = listaAutores.get(1).getNombre();
            String autor3 = listaAutores.get(2).getNombre();
            String autor4 = listaAutores.get(3).getNombre();
            String autor5 = listaAutores.get(10).getNombre();

            System.out.println("\nProbando conectividad:");
            System.out.println(autor1 + " <-> " + autor2 + ": " + grafo.estanConectado(autor1, autor2));
            System.out.println(autor1 + " <-> " + autor3 + ": " + grafo.estanConectado(autor1, autor3));


            ArrayList<String> camino = grafo.estanConectados(autor1, autor2);
            if (camino != null) {
                System.out.println("Camino encontrado: " + camino.size() + " autores");
                System.out.println("  Camino " + autor1 + " -> " + autor2 + ":"+camino);
            }
            ArrayList<String> camino1 = grafo.estanConectados(autor5, autor2);
            if (camino != null) {
                System.out.println("Camino encontrado: " + camino1.size() + " autores");
                System.out.println("  Camino " + autor5 + " -> " + autor2 + ":"+camino1);
            }
            ArrayList<String> camino2 = grafo.estanConectados(autor4, autor3);
            if (camino != null) {
                System.out.println("Camino encontrado: " + camino2.size() + " autores");
                System.out.println("  Camino " + autor4 + " -> " + autor3 + ":"+camino2);
            }

            // MEDIR RENDIMIENTO POR MINUTO
            System.out.println("\n--- MEDICIÓN DE RELACIONES POR MINUTO ---");

            ArrayList<String> nombres = new ArrayList<>(grafo.th.keySet());
            int consultas = 0;
            long start = System.currentTimeMillis();
            long end = start + 60000;

            System.out.println("Ejecutando consultas por 1 minuto...");

            while (System.currentTimeMillis() < end) {
                // Hacer consultas simples entre los primeros autores
                if (nombres.size() > 20) {
                    //Consultas booleanas
                    grafo.estanConectado(nombres.get(0), nombres.get(1));
                    consultas++;
                    grafo.estanConectado(nombres.get(0), nombres.get(5));
                    consultas++;
                    grafo.estanConectado(nombres.get(1), nombres.get(10));
                    consultas++;
                    grafo.estanConectado(nombres.get(20), nombres.get(4));
                    consultas++;
                    //Consultas de camino
                    grafo.estanConectados(nombres.get(2), nombres.get(9));
                    consultas++;
                    grafo.estanConectado(nombres.get(19), nombres.get(6));
                    consultas++;
                    grafo.estanConectado(nombres.get(3), nombres.get(8));
                    consultas++;
                }
            }

            long tiempoReal = System.currentTimeMillis() - start;

            System.out.println("\nRESULTADOS :");
            System.out.println("Tiempo real ejecutando: " + tiempoReal + " ms");
            System.out.println("Relaciones calculadas en 1 minuto: " + consultas);
            System.out.println("Relaciones por minuto: " + consultas);
            System.out.println("Relaciones por hora: " + (consultas * 60));
            System.out.println("Relaciones por segundo: " + (consultas / 60));
        }

        // Limpiar para la siguiente prueba
        repo.getListaAutores().clear();
        repo.getListaPublicaciones().clear();
    }

    private static void testConDatosPrueba() {
        Repositorio repo = Repositorio.getRepositorio();

        // Crear datos de prueba simples
        System.out.println("Creando datos de prueba...");

        // Autores
        repo.añadirAutor("A1", "Autor 1");
        repo.añadirAutor("A2", "Autor 2");
        repo.añadirAutor("A3", "Autor 3");
        repo.añadirAutor("A4", "Autor 4");
        repo.añadirAutor("A5", "Autor 5");
        repo.añadirAutor("A6", "Autor 6");
        repo.añadirAutor("A7", "Autor 7");

        // Publicaciones
        repo.añadirPublicacion("P1", "Publicacion 1");
        repo.añadirPublicacion("P2", "Publicacion 2");
        repo.añadirPublicacion("P3", "Publicacion 3");
        repo.añadirPublicacion("P4", "Publicacion 4");
        repo.añadirPublicacion("P5", "Publicacion 5");
        repo.añadirPublicacion("P6", "Publicacion 6");

        // Relaciones: A1-A2-A3-A4 y A1-A5-A6-A7
        repo.añadirAutorAPublicacion("A1", "P1");
        repo.añadirAutorAPublicacion("A2", "P1");  // A1 y A2 conectados

        repo.añadirAutorAPublicacion("A2", "P2");
        repo.añadirAutorAPublicacion("A3", "P2");  // A2 y A3 conectados

        repo.añadirAutorAPublicacion("A3", "P3");
        repo.añadirAutorAPublicacion("A4", "P3");  // A3 y A4 conectados

        repo.añadirAutorAPublicacion("A1", "P4");
        repo.añadirAutorAPublicacion("A5", "P4");  // A1 y A5 conectados

        repo.añadirAutorAPublicacion("A5", "P5");
        repo.añadirAutorAPublicacion("A6", "P5");  // A5 y A6 conectados

        repo.añadirAutorAPublicacion("A6", "P6");
        repo.añadirAutorAPublicacion("A7", "P6");  // A6 y A7 conectados


        ArrayList<Autor> listaAutores = new ArrayList<>(repo.getListaAutores().values());

        // Crear grafo
        Graph grafo = new Graph();
        System.out.println("Creando grafo con datos de prueba...");
        grafo.crearGrafo(listaAutores);
        System.out.println("Grafo creado exitosamente");

        // Pruebas de conectividad
        System.out.println("\n--- PRUEBAS DE CONECTIVIDAD ---");
        // Caso 1: Autores directamente conectados
        System.out.println("A1 <-> A2 (directo): " + grafo.estanConectado("Autor 1", "Autor 2") + " camino: " + grafo.estanConectados("Autor 1", "Autor 2"));
        System.out.println("A2 <-> A3 (directo): " + grafo.estanConectado("Autor 2", "Autor 3") + " camino: " + grafo.estanConectados("Autor 2", "Autor 3"));
        System.out.println("A3 <-> A4 (directo): " + grafo.estanConectado("Autor 3", "Autor 4") + " camino: " + grafo.estanConectados("Autor 3", "Autor 4"));
        System.out.println("A1 <-> A5 (directo): " + grafo.estanConectado("Autor 1", "Autor 5") + " camino: " + grafo.estanConectados("Autor 1", "Autor 5"));
        System.out.println("A5 <-> A6 (directo): " + grafo.estanConectado("Autor 5", "Autor 6") + " camino: " + grafo.estanConectados("Autor 5", "Autor 6"));
        System.out.println("A6 <-> A7 (directo): " + grafo.estanConectado("Autor 6", "Autor 7") + " camino: " + grafo.estanConectados("Autor 6", "Autor 7"));

        // Caso 2: Autores en misma cadena lineal
        System.out.println("A1 <-> A4 (cadena lineal): " + grafo.estanConectado("Autor 1", "Autor 4") + " camino: " + grafo.estanConectados("Autor 1", "Autor 4"));
        System.out.println("A1 <-> A3 (cadena lineal): " + grafo.estanConectado("Autor 1", "Autor 3") + " camino: " + grafo.estanConectados("Autor 1", "Autor 3"));
        System.out.println("A1 <-> A6 (cadena lineal): " + grafo.estanConectado("Autor 1", "Autor 6") + " camino: " + grafo.estanConectados("Autor 1", "Autor 6"));
        System.out.println("A1 <-> A7 (cadena lineal): " + grafo.estanConectado("Autor 1", "Autor 7") + " camino: " + grafo.estanConectados("Autor 1", "Autor 7"));

        // Caso 3: Autores en diferentes cadenas pero conectados
        System.out.println("A2 <-> A6 (cruzado): " + grafo.estanConectado("Autor 2", "Autor 6") + " camino: " + grafo.estanConectados("Autor 2", "Autor 6"));
        System.out.println("A3 <-> A6 (cruzado): " + grafo.estanConectado("Autor 3", "Autor 6") + " camino: " + grafo.estanConectados("Autor 3", "Autor 6"));
        System.out.println("A2 <-> A5 (cruzado): " + grafo.estanConectado("Autor 2", "Autor 5") + " camino: " + grafo.estanConectados("Autor 2", "Autor 5"));
        System.out.println("A3 <-> A5 (cruzado): " + grafo.estanConectado("Autor 3", "Autor 5") + " camino: " + grafo.estanConectados("Autor 3", "Autor 5"));

        // Caso 4: Mismo autor
        System.out.println("A1 <-> A1 (mismo): " + grafo.estanConectado("Autor 1", "Autor 1") + " camino: " + grafo.estanConectados("Autor 1", "Autor 1"));
        System.out.println("A2 <-> A2 (mismo): " + grafo.estanConectado("Autor 2", "Autor 2") + " camino: " + grafo.estanConectados("Autor 2", "Autor 2"));
        System.out.println("A3 <-> A3 (mismo): " + grafo.estanConectado("Autor 3", "Autor 3") + " camino: " + grafo.estanConectados("Autor 3", "Autor 3"));
        System.out.println("A4 <-> A4 (mismo): " + grafo.estanConectado("Autor 4", "Autor 4") + " camino: " + grafo.estanConectados("Autor 4", "Autor 4"));
        System.out.println("A5 <-> A5 (mismo): " + grafo.estanConectado("Autor 5", "Autor 5") + " camino: " + grafo.estanConectados("Autor 5", "Autor 5"));
        System.out.println("A6 <-> A6 (mismo): " + grafo.estanConectado("Autor 6", "Autor 6") + " camino: " + grafo.estanConectados("Autor 6", "Autor 6"));
        System.out.println("A7 <-> A7 (mismo): " + grafo.estanConectado("Autor 7", "Autor 7") + " camino: " + grafo.estanConectados("Autor 7", "Autor 7"));

        // Caso 5: Autores en extremos de diferentes cadenas
        System.out.println("A4 <-> A7 (extremos): " + grafo.estanConectado("Autor 4", "Autor 7") + " camino: " + grafo.estanConectados("Autor 4", "Autor 7"));


        // Mostrar estructura del grafo
        System.out.println("\n--- ESTRUCTURA DEL GRAFO ---");
        grafo.print();
    }

// CAPACIDAD DEL SISTEMA:
//- Consultas por segundo:27
//- Consultas por minuto: 1,666
//- Consultas por hora: 99,960
//- Tamaño máximo probado: 273,884 autores, 8,258,303 relaciones
//- Tiempo construcción grafo: 66,904 ms

// EJEMPLOS EJECUTADOS:
// estanConectado("Kevin Thiele", "Cristian Launes") -> true
// estanConectados("Kevin Thiele", "Bauke W Dijkstra") ->
//  [Kevin Thiele, Pauline Ladiges, Paul Irwin Forster,
//   Ronald J. Quinn, Alysha G. Elliott, David Craik,
//   Bernard Henrissat, Francine Govers, Ben Feringa,
//   Bauke W Dijkstra]
}