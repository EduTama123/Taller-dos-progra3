package com.itsqmet.service;

import com.itsqmet.entity.TestAnsiedad;
import com.itsqmet.entity.Usuario;
import com.itsqmet.repository.TestRepository;
import com.itsqmet.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public TestAnsiedad guardarTest(TestAnsiedad test) {
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

    @Transactional(readOnly = true)
    public Map<String, Object> obtenerResumenConProcedimiento(Long usuarioId) {
        Map<String, Object> respuesta = new HashMap<>();

        try {
            usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

            try {
                StoredProcedureQuery query = entityManager
                        .createStoredProcedureQuery("sp_historial_tests_usuario")
                        .registerStoredProcedureParameter("p_usuario_id", Long.class, ParameterMode.IN)
                        .registerStoredProcedureParameter("p_total_tests", Integer.class, ParameterMode.OUT)
                        .registerStoredProcedureParameter("p_promedio_puntuacion", BigDecimal.class, ParameterMode.OUT)
                        .registerStoredProcedureParameter("p_ultimo_nivel", String.class, ParameterMode.OUT)
                        .setParameter("p_usuario_id", usuarioId);

                query.execute();

                Integer totalTests = (Integer) query.getOutputParameterValue("p_total_tests");
                BigDecimal promedioBigDecimal = (BigDecimal) query.getOutputParameterValue("p_promedio_puntuacion");
                Double promedio = promedioBigDecimal != null ? promedioBigDecimal.doubleValue() : 0.0;
                String ultimoNivel = (String) query.getOutputParameterValue("p_ultimo_nivel");

                respuesta.put("totalTests", totalTests != null ? totalTests : 0);
                respuesta.put("promedioPuntuacion", promedio != null ? promedio : 0.0);
                respuesta.put("ultimoNivel", ultimoNivel != null ? ultimoNivel : "Sin tests");
                respuesta.put("mensaje", generarMensajeSegunPromedio(
                        totalTests != null ? totalTests : 0,
                        promedio != null ? promedio : 0.0
                ));

            } catch (Exception e) {
                Long totalTests = testRepository.countByUsuarioId(usuarioId);
                Double promedio = testRepository.getAveragePuntuacionByUsuarioId(usuarioId);
                String ultimoNivel = testRepository.getUltimoNivelByUsuarioId(usuarioId);

                respuesta.put("totalTests", totalTests != null ? totalTests.intValue() : 0);
                respuesta.put("promedioPuntuacion", promedio != null ? promedio : 0.0);
                respuesta.put("ultimoNivel", ultimoNivel != null ? ultimoNivel : "Sin tests");
                respuesta.put("mensaje", generarMensajeSegunPromedio(
                        totalTests != null ? totalTests.intValue() : 0,
                        promedio != null ? promedio : 0.0
                ));
            }

        } catch (Exception e) {
            respuesta.put("totalTests", 0);
            respuesta.put("promedioPuntuacion", 0.0);
            respuesta.put("ultimoNivel", "Sin tests");
            respuesta.put("mensaje", "No se pudieron cargar las estadísticas.");
        }

        return respuesta;
    }

    private String generarMensajeSegunPromedio(Integer totalTests, Double promedio) {
        if (totalTests == 0) {
            return "Aún no has realizado ningún test. ¡Realiza tu primera evaluación para conocer tu nivel de ansiedad!";
        }
        if (promedio <= 10) {
            return "¡Excelente! Tu nivel de ansiedad es LEVE. Continúa con tus hábitos saludables.";
        } else if (promedio <= 20) {
            return "Tu nivel de ansiedad es MODERADO. Te recomendamos practicar técnicas de respiración y relajación diariamente.";
        } else if (promedio <= 30) {
            return "Tu nivel de ansiedad es SEVERO. Considera buscar apoyo profesional para mejorar tu bienestar.";
        } else {
            return "Tu nivel de ansiedad es SEVERO. Es importante que busques ayuda profesional lo antes posible.";
        }
    }
}