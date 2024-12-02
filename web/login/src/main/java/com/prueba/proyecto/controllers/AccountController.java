/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prueba.proyecto.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;
import java.util.Date;


import com.prueba.proyecto.models.Administrador;
import com.prueba.proyecto.models.RegisterDto;
import com.prueba.proyecto.repositories.AdministradorRepository;



@Controller
public class AccountController {
    
    @Autowired
    private AdministradorRepository repo;
    
    @GetMapping("/register")
    public String register(Model model) {
        RegisterDto registerDto = new RegisterDto();
        model.addAttribute(registerDto);
        model.addAttribute("success", false);
        return "register";
    }
    
    @PostMapping("/register")
    public String register(
            Model model,
            @Valid @ModelAttribute RegisterDto registerDto,
            BindingResult result
            ) {
        if (!registerDto.getContraseña().equals(registerDto.getConfirmarContraseña())) {
            result.addError(
                    new FieldError("registerDto", "confirmPassword"
                            , "Las contraseñas no coinciden")
            );
        }    
        
        Administrador administrador = repo.findByUsuario(registerDto.getUsuario());
        if (administrador != null) {
            result.addError(
                    new FieldError("registerDto", "usuario"
                            , "El usuario ya está en uso")
            ) ;
        }
        
        if (result.hasErrors()) {
            return "register";
        }
        
        try {
            var bCryptEncoder = new BCryptPasswordEncoder();
            
            Administrador newUser = new Administrador();
            newUser.setNombre(registerDto.getNombre());
            newUser.setUsuario(registerDto.getUsuario());
            newUser.setFechaRegistro(new Date());
            newUser.setContraseña(bCryptEncoder.encode(registerDto.getContraseña()));
            
            repo.save(newUser);
            
            model.addAttribute("registerDto", new RegisterDto());
            model.addAttribute("success", true);
        }    
        catch(Exception ex) {
            result.addError(
                    new FieldError("registerDto", "Nombre"
                            , ex.getMessage ())
            ) ;
        }
           
        return "register";
    }
}
