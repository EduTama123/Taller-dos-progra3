package com.itsqmet.controller;

import com.itsqmet.entity.Usuario;
import com.itsqmet.repository.UsuarioRepository;
import com.itsqmet.roles.Rol;
import com.itsqmet.service.UsuarioService;
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

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //endpoint para login desde angular
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Usuario loginUsuario = new Usuario();
            loginUsuario.setEmail(loginRequest.getEmail());
            loginUsuario.setPassword(loginRequest.getPassword());

            Map<String, String> resultado = usuarioService.autenticarConToken(loginUsuario);

            if (resultado != null) {
                return ResponseEntity.ok(resultado);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Credenciales inválidas"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error en el servidor"));
        }
    }

    //clase para la solicitud de login
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