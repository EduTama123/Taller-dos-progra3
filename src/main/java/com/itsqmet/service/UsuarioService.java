package com.itsqmet.service;

import com.itsqmet.entity.Usuario;
import com.itsqmet.repository.UsuarioRepository;
import com.itsqmet.roles.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ========== MÉTODOS DE VERIFICACIÓN ==========

    public boolean existsByUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    // ========== MÉTODOS CRUD BÁSICOS ==========

    // LEER TODOS LOS USUARIOS
    public List<Usuario> mostrarUsuarios() {
        return usuarioRepository.findAll();
    }

    // BUSCAR USUARIO POR ID
    public Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("USUARIO NO ENCONTRADO CON ID: " + id));
    }

    // GUARDAR USUARIO (con contraseña encriptada)
    @Transactional
    public Usuario guardarUsuario(Usuario usuario) {
        // Encriptar contraseña antes de guardar
        if (usuario.getPassword() != null && !usuario.getPassword().startsWith("$2a$")) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        return usuarioRepository.save(usuario);
    }

    // ACTUALIZAR USUARIO
    @Transactional
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = buscarUsuarioPorId(id);

        // Actualizar campos personales
        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setTelefono(usuarioActualizado.getTelefono());

        // Actualizar campos de cuenta (si se proporcionan)
        if (usuarioActualizado.getUsername() != null) {
            // Verificar que el nuevo username no esté en uso por otro usuario
            if (!usuarioExistente.getUsername().equals(usuarioActualizado.getUsername()) &&
                    existsByUsername(usuarioActualizado.getUsername())) {
                throw new RuntimeException("EL NOMBRE DE USUARIO YA ESTÁ EN USO");
            }
            usuarioExistente.setUsername(usuarioActualizado.getUsername());
        }

        if (usuarioActualizado.getEmail() != null) {
            // Verificar que el nuevo email no esté en uso por otro usuario
            if (!usuarioExistente.getEmail().equals(usuarioActualizado.getEmail()) &&
                    existsByEmail(usuarioActualizado.getEmail())) {
                throw new RuntimeException("EL EMAIL YA ESTÁ EN USO");
            }
            usuarioExistente.setEmail(usuarioActualizado.getEmail());
        }

        // Actualizar contraseña solo si se proporciona una nueva
        if (usuarioActualizado.getPassword() != null &&
                !usuarioActualizado.getPassword().isEmpty() &&
                !usuarioActualizado.getPassword().startsWith("$2a$")) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
        }

        return usuarioRepository.save(usuarioExistente);
    }

    // ELIMINAR USUARIO
    @Transactional
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("USUARIO NO ENCONTRADO CON ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    // ========== MÉTODOS ESPECÍFICOS PARA REGISTRO ==========

    @Transactional
    public Usuario registrarUsuario(Usuario usuario) {
        // Validaciones previas
        if (existsByUsername(usuario.getUsername())) {
            throw new RuntimeException("EL NOMBRE DE USUARIO YA ESTÁ EN USO");
        }

        if (existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("EL EMAIL YA ESTÁ REGISTRADO");
        }

        // Establecer rol por defecto si no viene
        if (usuario.getRol() == null) {
            usuario.setRol(Rol.ROLE_USUARIO);
        }

        // Guardar usuario (la contraseña se encripta en guardarUsuario)
        return guardarUsuario(usuario);
    }

    // ========== MÉTODOS DE AUTENTICACIÓN ==========

    public Usuario autenticarUsuario(String usernameOrEmail, String password) {
        // Buscar por username o email
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(usernameOrEmail);

        if (usuarioOpt.isEmpty()) {
            usuarioOpt = usuarioRepository.findByEmail(usernameOrEmail);
        }

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // Verificar contraseña
            if (passwordEncoder.matches(password, usuario.getPassword())) {
                return usuario;
            }
        }

        throw new RuntimeException("CREDENCIALES INVÁLIDAS");
    }

    // ========== MÉTODOS ADICIONALES ==========

    public long contarUsuarios() {
        return usuarioRepository.count();
    }

    public List<Usuario> buscarPorRol(Rol rol) {
        return usuarioRepository.findByRol(rol);
    }

}