package com.prueba.proyecto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.prueba.proyecto.models.Administrador;

public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {
    Administrador findByUsuario(String usuario);
}
