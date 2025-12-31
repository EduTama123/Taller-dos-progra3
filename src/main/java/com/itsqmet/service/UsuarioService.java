package com.itsqmet.service;

import com.itsqmet.entity.Usuario;
import com.itsqmet.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // LEER TODOS LOS USUARIOS
    public List<Usuario> mostrarUsuarios() {
        return usuarioRepository.findAll();
    }

    // BUSCAR USUARIO POR ID
    public Usuario buscarUsuarioById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("USUARIO NO ENCONTRADO"));
    }

    // BUSCAR USUARIO POR CEDULA
    public Usuario buscarPorCedula(String cedula) {
        return usuarioRepository.findByCedula(cedula);
    }

    // GUARDAR USUARIO
    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // ACTUALIZAR USUARIO
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = buscarUsuarioById(id);

        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setApellido(usuarioActualizado.getApellido());
        usuarioExistente.setCedula(usuarioActualizado.getCedula());
        usuarioExistente.setDireccion(usuarioActualizado.getDireccion());
        usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
        usuarioExistente.setEdad(usuarioActualizado.getEdad());

        return usuarioRepository.save(usuarioExistente);
    }

    // ELIMINAR USUARIO
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    // OBTENER USUARIO POR CUENTA
    public Usuario obtenerPorCuentaId(Long accountId) {
        return usuarioRepository.findByAccountId(accountId);
    }
}