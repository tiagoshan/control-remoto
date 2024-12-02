package com.prueba.proyecto.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.prueba.proyecto.models.Administrador;
import com.prueba.proyecto.repositories.AdministradorRepository;

@Service
public class AppUserService implements UserDetailsService {
    @Autowired
    private AdministradorRepository repo;

    @Override
    public UserDetails loadUserByUsername (String usuario) throws UsernameNotFoundException {
        Administrador administrador = repo.findByUsuario(usuario);
        
        if (administrador != null) {
            var springUser = User.withUsername(administrador.getUsuario())
                    .password(administrador.getContraseña())
                    .build();
            
            return springUser;
        }
        
        throw new UsernameNotFoundException("Usuario no encontrado: " + usuario);
    }
}
