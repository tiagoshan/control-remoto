package com.prueba.proyecto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.proyecto.models.Reporte;

public interface ReporteRepository extends JpaRepository<Reporte, Integer> {
}
