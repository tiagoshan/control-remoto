package com.prueba.proyecto.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.prueba.proyecto.models.Reporte;
import com.prueba.proyecto.services.ReporteService;


import java.util.List;

@Controller
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping("/reportes")
    public String verReportes(Model model) {
        List<Reporte> reportes = reporteService.obtenerTodosLosReportes();
        System.out.println("Número de reportes obtenidos: " + reportes.size()); // Mensaje de depuración
        model.addAttribute("reportes", reportes);
        return "reportes";
    }
}
