package com.example.cdol3.model;

public class Articulos {
    private String id;
    private String colegio;
    private String categoria;
    private String talla;
    private int cantidad;

    // Constructor vacío para Firestore
    public Articulos() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Constructor con todos los parámetros
    public Articulos(String colegio, String categoria, String talla, int cantidad) {
        this.colegio = colegio;
        this.categoria = categoria;
        this.talla = talla;
        this.cantidad = cantidad;
    }

    // Getters y setters
    public String getColegio() {
        return colegio;
    }

    public void setColegio(String colegio) {
        this.colegio = colegio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
