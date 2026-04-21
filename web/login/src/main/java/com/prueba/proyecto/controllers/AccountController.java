package com.prueba.proyecto.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;
import java.time.LocalDateTime;

import com.prueba.proyecto.models.Administrador;
import com.prueba.proyecto.models.RegisterDto;
import com.prueba.proyecto.repositories.AdministradorRepository;

@Controller
public class AccountController {

    private final AdministradorRepository repo;
    private final PasswordEncoder passwordEncoder;

    public AccountController(AdministradorRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        model.addAttribute("success", false);
        return "register";
    }

    @PostMapping("/register")
    public String register(
            Model model,
            @Valid @ModelAttribute RegisterDto registerDto,
            BindingResult result) {

        if (!registerDto.getContraseña().equals(registerDto.getConfirmarContraseña())) {
            result.addError(new FieldError("registerDto", "confirmarContraseña", "Las contraseñas no coinciden"));
        }

        if (repo.findByUsuario(registerDto.getUsuario()) != null) {
            result.addError(new FieldError("registerDto", "usuario", "El usuario ya está en uso"));
        }

        if (result.hasErrors()) {
            return "register";
        }

        try {
            Administrador nuevoAdmin = new Administrador();
            nuevoAdmin.setNombre(registerDto.getNombre());
            nuevoAdmin.setUsuario(registerDto.getUsuario());
            nuevoAdmin.setFechaRegistro(LocalDateTime.now());
            nuevoAdmin.setContraseña(passwordEncoder.encode(registerDto.getContraseña()));
            repo.save(nuevoAdmin);

            model.addAttribute("registerDto", new RegisterDto());
            model.addAttribute("success", true);
        } catch (Exception ex) {
            result.addError(new FieldError("registerDto", "nombre", ex.getMessage()));
        }

        return "register";
    }
}
