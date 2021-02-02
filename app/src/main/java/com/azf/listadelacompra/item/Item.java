package com.azf.listadelacompra.item;

import java.util.Comparator;

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

    public static Comparator<Item> ordenarPorTipo = new Comparator<Item>() {
        @Override
        public int compare(Item item1, Item item2) {
            return item1.getTipo().compareTo(item2.getTipo());
        }
    };
}
