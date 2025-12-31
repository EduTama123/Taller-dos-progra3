package com.itsqmet.service;

import com.itsqmet.entity.TestAnsiedad;
import com.itsqmet.entity.Usuario;
import com.itsqmet.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    // LEER TODOS LOS TESTS
    public List<TestAnsiedad> mostrarTests() {
        return testRepository.findAll();
    }

    // BUSCAR TEST POR ID
    public TestAnsiedad buscarTestById(Long id) {
        return testRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TEST NO ENCONTRADO"));
    }

    // GUARDAR TEST
    public TestAnsiedad guardarTest(TestAnsiedad test) {
        // CALCULAR RESULTADOS ANTES DE GUARDAR
        test.calcularResultados();
        return testRepository.save(test);
    }

    // ELIMINAR TEST
    public void eliminarTest(Long id) {
        testRepository.deleteById(id);
    }

    // OBTENER TESTS DE UN USUARIO ESPECIFICO
    public List<TestAnsiedad> obtenerTestsPorUsuario(Long usuarioId) {
        return testRepository.findByUsuarioId(usuarioId);
    }

    // OBTENER TESTS DE UN USUARIO (CON OBJETO USUARIO)
    public List<TestAnsiedad> obtenerTestsPorUsuario(Usuario usuario) {
        return testRepository.findByUsuarioId(usuario.getId());
    }

    // OBTENER TESTS SIN RECOMENDACION
    public List<TestAnsiedad> obtenerTestsSinRecomendacion() {
        return testRepository.findByRecomendacionIsNull();
    }

    // CONTAR TESTS DE UN USUARIO
    public Long contarTestsPorUsuario(Long usuarioId) {
        List<TestAnsiedad> tests = testRepository.findByUsuarioId(usuarioId);
        return (long) tests.size();
    }
}