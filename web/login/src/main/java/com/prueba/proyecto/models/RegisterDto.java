/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prueba.proyecto.models;

import jakarta.validation.constraints.*;

/**
 *
 * @author braju
 */
public class RegisterDto {
    
    @NotEmpty
    private String nombre;
    
    @NotEmpty
    private String usuario;
    
    @Size(min = 6, message = "Mínimo de contraseña es de 6 caracteres")
    private String contraseña;
    
    private String confirmarContraseña;
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getConfirmarContraseña() {
        return confirmarContraseña;
    }

    public void setConfirmarContraseña(String confirmContraseña) {
        this.confirmarContraseña = confirmContraseña;
    }

    
}
