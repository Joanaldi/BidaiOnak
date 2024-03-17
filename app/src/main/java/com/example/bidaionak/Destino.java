package com.example.bidaionak;

public class Destino {
    private long id;
    private String nombre;
    private String costeTotal;
    private String transporte;

    public Destino(long id, String nombre, String costeTotal, String transporte) {
        this.id = id;
        this.nombre = nombre;
        this.costeTotal = costeTotal;
        this.transporte = transporte;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCosteTotal() {
        return costeTotal;
    }

    public void setCosteTotal(String costeTotal) {
        this.costeTotal = costeTotal;
    }

    public String getTransporte() {
        return transporte;
    }

    public void setTransporte(String transporte) {
        this.transporte = transporte;
    }
}
