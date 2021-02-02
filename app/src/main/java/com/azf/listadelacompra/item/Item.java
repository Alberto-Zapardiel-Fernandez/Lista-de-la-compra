package com.azf.listadelacompra.item;

public class Item {

    private String nombre, tipo, key;

    public Item(String nombre, String tipo, String key) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.key = key;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
