import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

public class Repositorio {
    private static Repositorio miRepositorio = null;
    private HashMap<String, Autor> listaAutores;
    private HashMap<String, Publicacion> listaPublicaciones;

    private Repositorio() {
        this.listaAutores = new HashMap<String, Autor>();
        this.listaPublicaciones = new HashMap<String, Publicacion>();
    }

    public static Repositorio getRepositorio() {
        if (miRepositorio == null) {
            miRepositorio = new Repositorio();
        }
        return miRepositorio;
    }

    public HashMap<String, Autor> getListaAutores() {
        return listaAutores;
    }

    public HashMap<String, Publicacion> getListaPublicaciones() {
        return listaPublicaciones;
    }

    public void cargarAutor(String pFichero) throws IOException, FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(pFichero));
        String line;
        while ((line = br.readLine()) != null) {
            String[] lineSplited = line.split("\\s+#\\s+");
            añadirAutor(lineSplited[0], lineSplited[1]);
        }
        br.close();
    }

    public ArrayList<String> cargarPublicacion(String pFichero) throws IOException, FileNotFoundException {
        ArrayList<String> titulos = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(pFichero));
        String line;
        while ((line = br.readLine()) != null) {
            String[] lineSplited = line.split("\\s+#\\s+");
            añadirPublicacion(lineSplited[0], lineSplited[1]);
            titulos.add(lineSplited[1]);
        }
        br.close();
        return titulos;
    }

    public void cargarPublicacionesAutores(String pFichero) throws IOException, FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(pFichero));
        String line;
        while ((line = br.readLine()) != null) {
            String[] lineSplited = line.split("\\s+#\\s+");
            String idPub = lineSplited[0];
            String idAutor = lineSplited[1];
            añadirAutorAPublicacion(idAutor, idPub);
        }
        br.close();
    }

    public void cargarCitas(String pFichero) throws IOException, FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(pFichero));
        String line;
        while ((line=br.readLine())!=null) {
            String[] lineSplited = line.split("\\s+#\\s+");
            String idPub = lineSplited[0];
            String idCitada = lineSplited[1];
            Publicacion pub = buscarPublicacionPorId(idPub);
            Publicacion citada = buscarPublicacionPorId(idCitada);
            if (pub!=null && idCitada!=null){
                pub.addCitada(citada);
            }

            /*Publicacion pub = buscarPublicacionPorId(idPub);
            if (pub == null) {
                pub = new Publicacion(idPub, "");
                listaPublicaciones.put(idPub, pub);
            }

            Publicacion citada = buscarPublicacionPorId(idCitada);
            if(citada == null){
                citada = new Publicacion(idCitada, "");
                listaPublicaciones.put(idCitada, citada);
            }*/
            //pub.addCitada(citada);
        }
        br.close();
    }

    public int tamañoHashAutor() {
        return this.listaAutores.size();
    }

    public int tamañoHashPublicacion() {
        return this.listaPublicaciones.size();
    }

    public Publicacion buscarPublicacionPorId(String pId) {
        if (listaPublicaciones.containsKey(pId)) {
            return listaPublicaciones.get(pId);
        }
        return null;
    }

    public String buscarPublicacionTexto(String idPub) {
        Publicacion pub = listaPublicaciones.get(idPub);

        if (pub == null) {
            return " No se encontró la publicación con id " + idPub;
        }

        return "Publicación encontrada: " + pub.getTitulo();
    }

    public void añadirPublicacion(String pId, String pTitulo) {
        if (!listaPublicaciones.containsKey(pId)) {
            Publicacion publicacion = new Publicacion(pId, pTitulo);
            listaPublicaciones.put(pId, publicacion);
        }
    }

    public void añadirAutor(String pId, String pNombre) {
        if (!listaAutores.containsKey(pId)) {
            Autor autor = new Autor(pId, pNombre);
            listaAutores.put(pId, autor);
        }
    }

    public void añadirCitaAPublicacion(String idPubOrigen, String idPubCitada) {
        // Asegurar que existen ambas publicaciones
        if (!listaPublicaciones.containsKey(idPubOrigen)) {
            listaPublicaciones.put(idPubOrigen, new Publicacion(idPubOrigen, ""));
        }
        if (!listaPublicaciones.containsKey(idPubCitada)) {
            listaPublicaciones.put(idPubCitada, new Publicacion(idPubCitada, ""));
        }
        // Establecer cita
        listaPublicaciones.get(idPubOrigen).añadirCitaPorId(idPubCitada);
    }

    public void añadirAutorAPublicacion(String idAutor, String idPub){
        Autor autor = listaAutores.get(idAutor);
        if (autor == null) {
            autor = new Autor(idAutor, "");
            listaAutores.put(idAutor, autor);
        }
        Publicacion pub = buscarPublicacionPorId(idPub);
        if (pub == null) {
            pub = new Publicacion(idPub, "");
            listaPublicaciones.put(idPub, pub);
        }
        pub.addAutor(autor);
        autor.addPublicacion(pub);
    }

    //Dada una publicación (identificador), devolver una lista con las publicaciones que cita
    public HashSet<Publicacion> getCitasDePublicacion(String idPublicacion){
        Publicacion pub = buscarPublicacionPorId(idPublicacion);
        if(pub == null){
            return new HashSet<>();
        }
        return pub.getListaCitadas();
    }

    public String ImprimirCitasDePublicacion(String idPublicacion) {
        Publicacion pub = buscarPublicacionPorId(idPublicacion);

        if (pub == null) {
            return "No existe la publicación con id " + idPublicacion;
        }
        HashSet<Publicacion> citas = pub.getListaCitadas();
        if (citas.isEmpty()) {
            return "(No cita a ninguna publicación)";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Publicaciones citadas por ").append(idPublicacion).append(":\n");
        for (Publicacion p : citas) {
            sb.append(" - ").append(p.getId()).append(" # ").append(p.getTitulo()).append("\n");
        }
        return sb.toString();
    }

    //Dada una publicación, devolver una lista con sus autores
    public HashSet<Autor> getAutoresDePublicacion(String idPublicacion){
        Publicacion pub = buscarPublicacionPorId(idPublicacion);
        if(pub == null){  //si no existe la publicación, devolvemos la lista vacía
            return new HashSet<>();
        }
        return pub.getListaAutores();
    }

    //Dado un autor, devolver una lista con sus publicaciones
    public HashSet<Publicacion> getPublicacionesDeAutor(String idAutor){
        Autor autor = listaAutores.get(idAutor);
        if(autor == null){
            return new HashSet<>();
        }
        return autor.getListaPublicaciones();
    }

    public String ImprimirAutoresDePublicacion(String idPublicacion) {
        Publicacion pub = buscarPublicacionPorId(idPublicacion);

        if (pub == null) {
            return "No existe la publicación con id " + idPublicacion;
        }

        HashSet<Autor> autores = pub.getListaAutores();
        if (autores.isEmpty()) {
            return "(No tiene autores asociados)";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Autores de la publicación ").append(idPublicacion).append(":\n");
        for (Autor a : autores) {
            sb.append(" - ").append(a.getId()).append(" # ").append(a.getNombre()).append("\n");
        }

        return sb.toString();
    }

    public String ImprimirPublicacionesDeAutor(String idAutor) {
        Autor autor = listaAutores.get(idAutor);

        if (autor == null) {
            return "El autor " + idAutor + " no existe.";
        }

        HashSet<Publicacion> pubs = autor.getListaPublicaciones();
        if (pubs.isEmpty()) {
            return "(El autor no tiene publicaciones)";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Publicaciones del autor ").append(idAutor).append(":\n");
        for (Publicacion p : pubs) {
            sb.append(" - ").append(p.getId()).append(" # ").append(p.getTitulo()).append("\n");
        }

        return sb.toString();
    }

    private void borrarPublicacion(Publicacion pub){
        if(pub == null){
            return;
        }
        //Borrarla de los autores que la referencian
        for(Autor autor: pub.getListaAutores()){
            autor.getListaPublicaciones().remove(pub);
        }
        //Borrarla de las citas de otras publicaciones
        for(Publicacion cita: listaPublicaciones.values()) {
            cita.getListaCitadas().remove(pub);
        }
        //Borrarla del repositorio
        listaPublicaciones.remove(pub.getId());
    }

    public String borrarPublicacionPorId(String idPub) {
        Publicacion pub = listaPublicaciones.get(idPub);
        if (pub == null) {
            return "La publicación " + idPub + " no existe.";
        }else {
            borrarPublicacion(pub);
            return "Publicación " + idPub + " eliminada. Total publicaciones: " + tamañoHashPublicacion();
        }
    }

    public void borrarAutor(Autor autor){
        if(autor == null){
            return;
        }
        //Borrarlo de las publicaciones en las que aparezca
        for(Publicacion pub: autor.getListaPublicaciones()){
            pub.getListaAutores().remove(autor);
        }
        //Borrarlo del repositorio
        listaAutores.remove(autor.getId());
    }

    public String borrarAutorPorId(String idAutor) {
        Autor autor = listaAutores.get(idAutor);
        if (autor == null) {
            return "El autor " + idAutor + " no existe.";
        }
        borrarAutor(autor);
        return "Autor " + idAutor + " eliminado. Total de autores: " + tamañoHashAutor();
    }

    public void guardarPublicaciones(String path) throws IOException{
        FileWriter fw = new FileWriter(path);
        for(Publicacion pub: listaPublicaciones.values()){
            fw.write(pub.getId() + "#" + pub.getTitulo() + "\n");
        }
        fw.close();
    }

    public void guardarAutores(String path) throws IOException{
        FileWriter fw = new FileWriter(path);
        for(Autor autor: listaAutores.values()){
            fw.write(autor.getId() + "#" + autor.getNombre() + "\n");
        }
        fw.close();
    }

    public String[] publicacionesOrdenadas() {
        // 1. Sacar los títulos directamente del HashMap de publicaciones
        ArrayList<String> listaTitulos = new ArrayList<>();
        System.out.println(this.tamañoHashPublicacion());
        for (Publicacion p : this.listaPublicaciones.values()) {
            //System.out.println("ID: " + p.getId()  + "  Título: [" + p.getTitulo() + "]");
            listaTitulos.add(p.getTitulo());
        }
        // 2. Convertir a array
        String[] lista = listaTitulos.toArray(new String[0]);
        // 3. Ordenar con quickSort
        quickSort(lista, 0, lista.length - 1);
        // 4. Devolver
        return lista;
    }

    public String imprimirPublicacionesOrdenadas(int limite) {
        String[] lista = publicacionesOrdenadas();
        StringBuilder sb = new StringBuilder();
        sb.append("Lista ordenada: ").append(lista.length).append("\n");
        sb.append("Primeras ").append(Math.min(limite, lista.length)).append(" publicaciones ordenadas:\n");

        for (int i = 0; i < limite && i < lista.length; i++) {
            sb.append(i).append(": ").append(lista[i]).append("\n");
        }
        return sb.toString();
    }

    private void quickSort(String[] lista, int inicio, int fin) {
        if (inicio < fin) {
            int indiceParticion = particion(lista, inicio, fin);
            quickSort(lista, inicio, indiceParticion - 1);
            quickSort(lista, indiceParticion, fin);
        }
    }

    private int particion(String[] lista, int inicio, int fin) {
        String pivote = lista[(inicio + fin) / 2];
        int izq = inicio;
        int der = fin;
        while (izq <= der) {
            while (lista[izq].compareTo(pivote) < 0) {
                izq++;
            }
            while (lista[der].compareTo(pivote) > 0) {
                der--;
            }
            if (izq <= der) {
                swap(lista, izq, der);
                izq++;
                der--;
            }
        }
        return izq;
    }

    private void swap(String[] lista, int izq, int der) {
        // intercambia dos elementos del array usando una variable temporal
        String temp = lista[izq];
        lista[izq] = lista[der];
        lista[der] = temp;
    }
}
