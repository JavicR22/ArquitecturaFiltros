package org.example;

public class Filtro {
    private String nombre;
    private String tipo; // "A" o "B"
    private String origen;
    private String destino;
    private String palabra;

    public Filtro(String nombre, String tipo, String origen, String destino, String palabra) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.origen = origen;
        this.destino = destino;
        this.palabra = palabra;
    }


    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String getOrigen() { return origen; }
    public String getDestino() { return destino; }
    public String getPalabra() { return palabra; }

    @Override
    public String toString() {
        if (tipo.equals("A")) {
            return nombre + " (A) | Origen: " + origen;
        } else {
            return nombre + " (B) | Origen: " + origen +
                    (palabra != null && !palabra.isEmpty() ? " | Palabra: " + palabra : "") +
                    (destino != null && !destino.isEmpty() ? " | Destino: " + destino : "");
        }
    }
}
