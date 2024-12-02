/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prueba.proyecto.models;

import java.util.Date;

import jakarta.persistence.*;


 import java.util.Date;
 
 @Entity
 @Table(name="Administradores")
 public class Administrador {
     
     @Id
     @GeneratedValue(strategy=GenerationType.IDENTITY)
     private int id;
 
     private String nombre;
     
     @Column(unique = true, nullable = false)
     private String usuario;
     
     @Column(nullable = false)
     private String contraseña;
     
     @Temporal(TemporalType.TIMESTAMP)
     private Date fechaRegistro;
     
     @Temporal(TemporalType.TIMESTAMP)
     private Date ultimaSesion;
 
     // Getters and Setters
     public int getId() {
         return id;
     }
 
     public void setId(int id) {
         this.id = id;
     }
 
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
 
     public Date getFechaRegistro() {
         return fechaRegistro;
     }
 
     public void setFechaRegistro(Date fechaRegistro) {
         this.fechaRegistro = fechaRegistro;
     }
 
     public Date getUltimaSesion() {
         return ultimaSesion;
     }
 
     public void setUltimaSesion(Date ultimaSesion) {
         this.ultimaSesion = ultimaSesion;
     }
 }
 
