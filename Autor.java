import java.util.*;
import java.io.*;

public class Autor {
    private String id;
    private String nombre;
    private HashSet<Publicacion> listaPublicaciones;

    public Autor(String pId, String pNombre) {
        this.id = pId;
        this.nombre = pNombre;
        this.listaPublicaciones = new HashSet<Publicacion>();
    }

    public String getId() {
        return this.id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public HashSet<Publicacion> getListaPublicaciones() {
        return this.listaPublicaciones;
    }

    public void addPublicacion(Publicacion publicacion) {
        listaPublicaciones.add(publicacion);
    }
}