package com.itsqmet.controller;

import com.itsqmet.entity.Usuario;
import com.itsqmet.service.UsuarioService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
                String usernameGenerado = usuario.getNombre()
                        .toLowerCase()
                        .replaceAll("\\s+", "")
                        .replaceAll("[^a-zA-Z0-9]", "");
                usuario.setUsername(usernameGenerado + System.currentTimeMillis());
            }

            Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);

        } catch (ConstraintViolationException e) {
            Map<String, String> errores = new HashMap<>();
            e.getConstraintViolations().forEach(violation -> {
                String field = violation.getPropertyPath().toString();
                String message = violation.getMessage();
                errores.put(field, message);
            });
            return ResponseEntity.badRequest().body(errores);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/test")
    public String testConexion() {
        return "Conexión exitosa";
    }

    @GetMapping
    public List<Usuario> getUsuarios() {
        return usuarioService.mostrarUsuarios();
    }

    //AHORA FUNCIONA
    @GetMapping("/{id}")
    public Usuario getUsuarioById(@PathVariable Long id) {
        return usuarioService.buscarUsuarioPorId(id);
    }

    //PARA CREAR USUARIO (POST SIN /register)
    @PostMapping
    public Usuario postUsuario(@RequestBody Usuario usuario) {
        return usuarioService.registrarUsuario(usuario);
    }

    //PARA ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<?> putUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuario);
            usuarioActualizado.setPassword(null); // No enviar contraseña en respuesta
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error al actualizar: " + e.getMessage()));
        }
    }

    // ✅ DESCOMENTADO - PARA ELIMINAR
    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
    }
}