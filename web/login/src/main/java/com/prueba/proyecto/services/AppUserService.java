package com.prueba.proyecto.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.prueba.proyecto.models.Administrador;
import com.prueba.proyecto.repositories.AdministradorRepository;

@Service
public class AppUserService implements UserDetailsService {

    private final AdministradorRepository repo;

    public AppUserService(AdministradorRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String usuario) throws UsernameNotFoundException {
        Administrador administrador = repo.findByUsuario(usuario);
        if (administrador == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + usuario);
        }
        return User.withUsername(administrador.getUsuario())
                .password(administrador.getContraseña())
                .build();
    }
}
