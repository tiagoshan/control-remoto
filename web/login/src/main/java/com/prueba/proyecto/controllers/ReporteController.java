package com.prueba.proyecto.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.prueba.proyecto.services.ReporteService;

@Controller
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/reportes")
    public String verReportes(Model model) {
        model.addAttribute("reportes", reporteService.obtenerTodosLosReportes());
        return "reportes";
    }
}
