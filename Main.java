import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) throws IOException, FileNotFoundException {
        long inicio = System.currentTimeMillis();

        //Cargar datos
        Repositorio.getRepositorio().cargarAutor("C:\\Users\\Yu Meng Ji\\IdeaProjects\\EDA\\src\\Datuak\\uthors-name-all.txt");
        System.out.println("Los datos se han cargado. Se han cargado " + Repositorio.getRepositorio().tamañoHashAutor() + " autores desde el archivo.");
        System.out.println("");

        Repositorio.getRepositorio().cargarPublicacion("C:\\Users\\Yu Meng Ji\\IdeaProjects\\EDA\\src\\Datuak\\publications-titles-all.txt");
        System.out.println("Los datos se han cargado. Se han cargado " + Repositorio.getRepositorio().tamañoHashPublicacion() + " publicaciones desde el archivo.");
        System.out.println("");

        Repositorio.getRepositorio().cargarPublicacionesAutores("C:\\Users\\Yu Meng Ji\\IdeaProjects\\EDA\\src\\Datuak\\publications-authors-all-final.txt");
        Repositorio.getRepositorio().cargarCitas("C:\\Users\\Yu Meng Ji\\IdeaProjects\\EDA\\src\\Datuak\\publications-citedPubs-all.txt");
        System.out.println("Se han cargado las relaciones de autores y citas.");

        //Buscar una publicación dado su id
        System.out.println(Repositorio.getRepositorio().buscarPublicacionTexto("Q34484783"));

        //Insertar una nueva publicacion
        Repositorio.getRepositorio().añadirPublicacion("Q12345678","Vlog de Viaje: Explorando la Costa Vasca");
        System.out.println("La publicación ha sido añadida. Nuevo número de publicaciones: " + Repositorio.getRepositorio().tamañoHashPublicacion());
        System.out.println("");

        //Añadir una cita a una publicacion
        String pubOrigen = "Q12345678";
        Repositorio.getRepositorio().añadirPublicacion("Q87654321","La Costa Vasca");
        Publicacion origen = Repositorio.getRepositorio().buscarPublicacionPorId(pubOrigen);
        Publicacion citada = Repositorio.getRepositorio().buscarPublicacionPorId("Q87654321");
        if (origen != null && citada != null) {
            origen.addCitada(citada);
            System.out.println("La publicación " + pubOrigen + " ahora cita a " + citada.getId() + ".\n");
        } else {
            System.out.println("No se pudo añadir la cita porque alguna publicación no existe.\n");
        }

        //Añadir un nuevo autor
        Repositorio.getRepositorio().añadirAutor("Q13579246", "Ana");
        System.out.println("Autor nuevo añadido: Q13579246 - Ana");
        System.out.println("Número total de autores: " + Repositorio.getRepositorio().tamañoHashAutor() + "\n");

        //Añadir un autor a una publicacion
        Repositorio.getRepositorio().añadirAutorAPublicacion("Q13579246", "Q12345678");
        System.out.println("El autor Q13579246 se ha asociado a la publicación Q12345678.");

        //Dada una publicacion, devolver una lista con las publicaciones que cita
        System.out.println(Repositorio.getRepositorio().ImprimirCitasDePublicacion("Q12345678"));

        //Dada una publicacion, devolver una lista con sus autores
        System.out.println(Repositorio.getRepositorio().ImprimirAutoresDePublicacion("Q12345678"));

        //Dado un autor, devolver una lista con sus publicaciones
        System.out.println(Repositorio.getRepositorio().ImprimirPublicacionesDeAutor("Q13579246"));

        //Borrar un autor
        System.out.println(Repositorio.getRepositorio().borrarAutorPorId("Q13579246"));

        //Borrar una publicacion
        System.out.println(Repositorio.getRepositorio().borrarPublicacionPorId("Q12345678"));
        System.out.println(Repositorio.getRepositorio().borrarPublicacionPorId("Q87654321"));

        //Ordenar la lista de publicaciones, sacando los 10 primeros elementos de la lista.
        System.out.println(Repositorio.getRepositorio().imprimirPublicacionesOrdenadas(10));

        //Escribir los datos en archivos
        System.out.println("Finalmente, escribiremos todos los datos en nuevos archivos.");
        Repositorio.getRepositorio().guardarAutores("C:\\Users\\Yu Meng Ji\\IdeaProjects\\EDA\\src\\Datuak\\salida-autores-all.txt");
        Repositorio.getRepositorio().guardarPublicaciones("C:\\Users\\Yu Meng Ji\\IdeaProjects\\EDA\\src\\Datuak\\salida-publicaciones-all.txt");
        System.out.println("Archivos de salida generados correctamente.");

        //Calcular tiempo
        long fin = System.currentTimeMillis();
        double tiempo = (double) ((fin - inicio) / 1000);
        System.out.println("Tiempo total de ejecución: " + tiempo + " segundos");
    }
}