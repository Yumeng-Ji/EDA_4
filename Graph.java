import java.util.*;

public class Graph {

    HashMap<String, Integer> th;
    String[] keys;
    ArrayList<Integer>[] adjList;

    public void crearGrafo(ArrayList<Autor> lista) {
        // Post: crea el grafo desde la lista de autores
        //       Los nodos son nombres de autores
        if (lista == null || lista.isEmpty()) {
            throw new IllegalArgumentException("La lista de autores no puede ser nula o vacía");
        }

        // Paso 1: llenar th
        th = new HashMap<>();
        int index = 0;

        // Autores con nombre válido
        for (Autor autor : lista) {
            if (autor.getNombre() != null && !autor.getNombre().trim().isEmpty()) {
                if (!th.containsKey(autor.getNombre())) {
                    th.put(autor.getNombre(), index);
                    index++;
                }
            }
        }

        // Paso 2: llenar keys
        keys = new String[th.size()];
        for (String k : th.keySet()) {
            int idx = th.get(k);
            if (idx >= 0 && idx < keys.length) {
                keys[idx] = k;
            }
        }

        // Paso 3: llenar adjList
        adjList = (ArrayList<Integer>[]) new ArrayList[th.size()];
        for (int i = 0; i < adjList.length; i++) {
            adjList[i] = new ArrayList<>();
        }
        // Construir relaciones basadas en co-autoría
        for (Autor autor : lista) {
            int idAutor = th.get(autor.getNombre());
            // Obtener todas las publicaciones del autor
            HashSet<Publicacion> publicaciones = autor.getListaPublicaciones();
            // Para cada publicacion, obtener los co-autores
            for (Publicacion pub : publicaciones) {
                HashSet<Autor> autoresPub = pub.getListaAutores();
                // Crear conexiones entre todos los autores de esta publicacion
                for (Autor coAutor : autoresPub) {
                    if (!coAutor.getNombre().equals(autor.getNombre())) { // Verificar que no es el mismo autor
                        if ( th.containsKey(coAutor.getNombre())) { // Verificar que está en la lista
                            int idCoAutor = th.get(coAutor.getNombre());
                            // Añadir conexión bidireccional si no existe
                            if (!adjList[idAutor].contains(idCoAutor)){
                                adjList[idAutor].add(idCoAutor);
                            }
                            if (!adjList[idCoAutor].contains(idAutor)){
                                adjList[idCoAutor].add(idAutor);
                            }
                        }
                    }
                }
            }
        }
    }

    public void print(){
        for (int i = 0; i < keys.length; i++){
            System.out.print("Autor: " + keys[i] + " -> Conectado con: ");
            for (int saliente : adjList[i]) {
                System.out.print(keys[saliente] + ", ");
            }
            System.out.println();
        }
    }

    public boolean estanConectado(String a1, String a2){
        if (!th.containsKey(a1) || !th.containsKey(a2)) {
            return false;
        }
        Queue<Integer> porExaminar = new LinkedList<Integer>();

        int pos1 = th.get(a1);
        int pos2 = th.get(a2);
        boolean enc = false;
        boolean[] examinados = new boolean[th.size()];

        // Si son el mismo autor
        if (pos1 == pos2){
            return true;
        }

        porExaminar.add(pos1);
        examinados[pos1] = true;

        while (!porExaminar.isEmpty() && !enc){
            int actual = porExaminar.remove();
            for (int i : adjList[actual]) {
                if (i == pos2) {
                    enc = true; // Encontrado
                }
                if (!examinados[i]) {
                    examinados[i] = true;
                    porExaminar.add(i);
                }
            }
        }
        return enc;
    }

    public ArrayList<String> estanConectados(String a1, String a2){

        if (!th.containsKey(a1) || !th.containsKey(a2)){
            return null;
        }

        int pos1 = th.get(a1);
        int pos2 = th.get(a2);
        ArrayList<String> camino = new ArrayList<>();

        // Si son el mismo autor
        if (pos1 == pos2){
            camino.add(keys[pos1]);
            return camino;
        }

        Queue<Integer> porExaminar = new LinkedList<>();
        boolean[] examinados = new boolean[th.size()];
        int[] backPos = new int[th.size()];

        //Inicializar backPos
        for (int i = 0; i < backPos.length; i++) backPos[i] = -1;
        porExaminar.add(pos1);
        examinados[pos1] = true;
        boolean enc = false;

        while (!porExaminar.isEmpty() && !enc) {
            int actual = porExaminar.poll();

            for (int i : adjList[actual]) {
                if (i == pos2) {
                    backPos[i] = actual;
                    enc = true;
                }
                if (!examinados[i]) {
                    examinados[i] = true;
                    backPos[i] = actual;
                    porExaminar.add(i);
                }
            }
        }

        if (!enc) return null;// No hay camino

        Stack<String> pila = new Stack<>();
        int aux = pos2;
        while (aux != -1){
            pila.push(keys[aux]);
            aux = backPos[aux];
        }
        while (!pila.isEmpty()){
            camino.add(pila.pop());
        }
        return camino;
    }

    public HashMap<String, Double> calcularRandomWalkRank(int nTests) {
        double d = 0.85; // damping factor
        Random r = new Random();
        HashMap<String, Double> result = new HashMap<String, Double>();

        if (keys == null || keys.length == 0) {
            System.out.println("El grafo está vacío, no se puede ejecutar Random Walk");
            return result; // HashMap vacío
        }

        int[] contador = new int[keys.length]; // array para contar cuántas veces se visita cada nodo

        for (int test = 0; test < nTests; test++) {
            int actual = r.nextInt(keys.length); // seleccionar un nodo inicial aleatorio
            ArrayList<Integer> examinados = new ArrayList<Integer>(); // lista para nodos visitados en este recorrido

            boolean fin = false;

            while (!fin) {
                contador[actual]++;
                examinados.add(actual);

                if (r.nextDouble() > d) {
                    fin = true;
                    continue;
                }
                // obtener la lista de vecinos del nodo actual
                ArrayList<Integer> vecinos = adjList[actual];

                if (vecinos.isEmpty()) {
                    fin = true;
                    continue;
                }
                // seleccionar un vecino aleatorio
                int siguiente = vecinos.get(r.nextInt(vecinos.size()));

                if (examinados.contains(siguiente)) {
                    fin = true;
                    continue;
                }

                actual = siguiente;
            }
        }
        int totalVisitas = 0;
        for (int i : contador) {
            totalVisitas = totalVisitas + i;
        }
        // calcular el total de visitas a todos los nodos
        for (int i = 0; i < keys.length; i++) {
            if (totalVisitas > 0) {
                result.put(keys[i], (double) contador[i] / totalVisitas);
            } else {
                result.put(keys[i], 0.0);
            }
        }
        return result;
    }


    
    public HashMap<String, Double> calcularPageRank() {
        HashMap<String, Double> result = new HashMap<>();

        if (keys == null || keys.length == 0) {
            System.out.println("El grafo está vacío, no se puede ejecutar PageRank");
            return result;
        }

        int n = keys.length;
        double d = 0.85;
        double umbral = 0.0001;
        int maxIteraciones = 100;

        // Inicializar PageRank uniformemente
        double[] pr = new double[n];
        double[] prNuevo = new double[n];
        double valorInicial = 1.0 / n;

        Arrays.fill(pr, valorInicial);

        // Precomputar grados de salida
        int[] grados = new int[n];
        for (int i = 0; i < n; i++) {
            grados[i] = adjList[i].size();
        }

        int iteracion = 0;
        double diferencia = Double.MAX_VALUE;

        while (diferencia > umbral && iteracion < maxIteraciones) {
            // Reiniciar nuevos valores con el término (1-d)/N
            double base = (1 - d) / n;
            Arrays.fill(prNuevo, base);

            // Distribuir PageRank a lo largo de los enlaces
            for (int i = 0; i < n; i++) {
                if (grados[i] > 0) {
                    double contribucion = d * pr[i] / grados[i];
                    for (int vecino : adjList[i]) {
                        prNuevo[vecino] += contribucion;
                    }
                } else {
                    // Nodo sin enlaces salientes: distribuir a todos
                    double contribucion = d * pr[i] / n;
                    for (int j = 0; j < n; j++) {
                        prNuevo[j] += contribucion;
                    }
                }
            }

            // Calcular diferencia total
            diferencia = 0.0;
            for (int i = 0; i < n; i++) {
                diferencia += Math.abs(prNuevo[i] - pr[i]);
            }

            // Preparar siguiente iteración
            double[] temp = pr;
            pr = prNuevo;
            prNuevo = temp;

            iteracion++;

            if (iteracion % 10 == 0) {
                System.out.println("Iteración " + iteracion + ", diferencia: " + diferencia);
            }
        }

        System.out.println("PageRank convergió en " + iteracion + " iteraciones");

        // Normalizar y guardar resultados
        for (int i = 0; i < n; i++) {
            result.put(keys[i], pr[i]);
        }

        return result;
    }

    public void imprimirLosDeMejorPageRank(double[] pr, int n) { // inefficient but valid!
        // Post: imprime los n elementos de mayor valor.
        //       Es ineficiente porque calcula los máximos consecutivamente, y borra el máximo anterior, es decir, borra los valores de entrada
        //       Puede ser útil para visualizar los resultados
        for (int x = 1; x <= n; x++) {
            double max = -1.0;
            int iMax = -1;
            for (int j = 0; j < pr.length; j++)
                if (pr[j] > max) {
                    max = pr[j];
                    iMax = j;
                }
            System.out.println("The " + x + "th best element is "
                    + keys[iMax] + " with value " + max);
            pr[iMax] = -1; // delete the maximum
        }
    }
}

