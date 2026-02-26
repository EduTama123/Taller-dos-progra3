package com.itsqmet.controller;

import com.itsqmet.entity.Usuario;
import com.itsqmet.repository.UsuarioRepository;
import com.itsqmet.roles.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController  // Cambiado de @Controller a @RestController
@RequestMapping("/api/auth")  // Añadido para diferenciar de las vistas
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Endpoint para login desde Angular
    @PostMapping("/login")  // <-- CORREGIDO: antes era "/api/auth/login"
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                    .orElse(null);

            if (usuario != null && passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {
                // No enviamos la contraseña por seguridad
                usuario.setPassword(null);
                return ResponseEntity.ok(usuario);
            }

            return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error interno del servidor"));
        }
    }

    // Clase para la solicitud de login
    public static class LoginRequest {
        private String email;
        private String password;

        // Getters y Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}