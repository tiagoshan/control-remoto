package com.server.models;

public class Administrador {
    private int id;
    private String nombre;
    private String usuario;
    private String constraseña;

    public Administrador(int id, String nombre, String usuario, String constraseña) {
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
        this.constraseña = constraseña;
    }
    public Administrador(String nombre, String usuario, String constraseña) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.constraseña = constraseña;
    }
    public Administrador() {
    }

    
    
}
