package com.prueba.proyecto.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
@Entity
@Table(name = "Conexiones")
public class Conexion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "cliente_id")
    private int clienteId;

    @Column(name = "administrador_id")
    private int administradorId;

    @Column(name = "fecha_conexion")
    private LocalDateTime fechaConexion;

    @Column(name = "fecha_desconexion")
    private LocalDateTime fechaDesconexion;

    @Column(name = "duracion")
    private int duracion;

    @Column(name = "cantidad_clicks")
    private int cantidadClicks;

    // Getters y setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getAdministradorId() {
        return administradorId;
    }

    public void setAdministradorId(int administradorId) {
        this.administradorId = administradorId;
    }

    public LocalDateTime getFechaConexion() {
        return fechaConexion;
    }

    public void setFechaConexion(LocalDateTime fechaConexion) {
        this.fechaConexion = fechaConexion;
    }

    public LocalDateTime getFechaDesconexion() {
        return fechaDesconexion;
    }

    public void setFechaDesconexion(LocalDateTime fechaDesconexion) {
        this.fechaDesconexion = fechaDesconexion;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getCantidadClicks() {
        return cantidadClicks;
    }

    public void setCantidadClicks(int cantidadClicks) {
        this.cantidadClicks = cantidadClicks;
    }
}
