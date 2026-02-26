package com.itsqmet.service;

import com.itsqmet.entity.TestAnsiedad;
import com.itsqmet.entity.Usuario;
import com.itsqmet.repository.TestRepository;
import com.itsqmet.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public TestAnsiedad guardarTest(TestAnsiedad test) {
        // El test ya viene con todos los datos seteados desde el controller
        return testRepository.save(test);
    }

    public List<TestAnsiedad> obtenerHistorialPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return testRepository.findByUsuarioOrderByFechaRealizacionDesc(usuario);
    }

    public TestAnsiedad obtenerTestPorId(Long id) {
        return testRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test no encontrado"));
    }
}