package com.prueba.proyecto.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prueba.proyecto.models.Reporte;
import com.prueba.proyecto.repositories.ReporteRepository;

import java.util.List;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    public List<Reporte> obtenerTodosLosReportes() {
        return reporteRepository.findAll();
    }
}

