/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.prueba.proyecto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.proyecto.models.Administrador;

/**
 *
 * @author braju
 */
public interface AdministradorRepository extends JpaRepository<Administrador, Integer>{
    
    public Administrador findByUsuario(String usuario);
    
}
