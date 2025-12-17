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
        for (String web : enlaces.keySet()){
            System.out.print("Element: " + web + " >>> ");
            for (String saliente : enlaces.get(web))  System.out.print(saliente + " ### ");

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

        // COMPLETAR CÓDIGO

    }


    public HashMap<String, Double> calcularPageRank() {
        boolean trace = false; // tracing the pagerank algorithm
        boolean damping = true; // tracing the pagerank algorithm

        // COMPLETAR CÓDIGO

    }

    public void imprimirLosDeMejorPageRank(double[] pr, int n) { // inefficient but valid!
        // Post: imprime los n elementos de mayor valor.
        //       Es ineficiente porque calcula los máximos consecutivamente, y borra el máximo anterior, es decir, borra los valores de entrada
        //       Puede ser útil para visualizar los resultados
        for (int x = 1; x <= n; x++) {
            double max = -1;
            int iMax = -1;
            for (int j = 0; j < pr.length; j++)
                if (pr[j] > max) {
                    max = pr[j];
                    iMax = j;
                }
            System.out.println("The " + x + "th best element is "
                    + id2String[iMax] + " with value " + max);
            pr[iMax] = -1; // delete the maximum
        }
    }

}


}