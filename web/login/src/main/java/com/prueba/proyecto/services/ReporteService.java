package com.prueba.proyecto.services;

import org.springframework.stereotype.Service;

import com.prueba.proyecto.models.Reporte;
import com.prueba.proyecto.repositories.ReporteRepository;

import java.util.List;

@Service
public class ReporteService {

    private final ReporteRepository reporteRepository;

    public ReporteService(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }

    public List<Reporte> obtenerTodosLosReportes() {
        return reporteRepository.findAll();
    }
}
