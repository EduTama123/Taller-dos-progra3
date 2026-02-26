package com.itsqmet.controller;

import com.itsqmet.entity.TestAnsiedad;
import com.itsqmet.entity.Usuario;
import com.itsqmet.service.TestService;
import com.itsqmet.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/api/tests")
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/ping")
    public String ping() {
        return "Test controller funcionando";
    }

    @PostMapping("/guardar")
    public ResponseEntity<?> guardarTest(@RequestBody Map<String, Object> request) {
        try {
            Long usuarioId = Long.parseLong(request.get("usuarioId").toString());
            Usuario usuario = usuarioService.buscarUsuarioPorId(usuarioId);

            TestAnsiedad test = new TestAnsiedad();
            test.setUsuario(usuario);
            test.setPregunta1(Integer.parseInt(request.get("pregunta1").toString()));
            test.setPregunta2(Integer.parseInt(request.get("pregunta2").toString()));
            test.setPregunta3(Integer.parseInt(request.get("pregunta3").toString()));
            test.setPregunta4(Integer.parseInt(request.get("pregunta4").toString()));
            test.setPregunta5(Integer.parseInt(request.get("pregunta5").toString()));
            test.setPregunta6(Integer.parseInt(request.get("pregunta6").toString()));
            test.setPregunta7(Integer.parseInt(request.get("pregunta7").toString()));
            test.setPregunta8(Integer.parseInt(request.get("pregunta8").toString()));
            test.setPregunta9(Integer.parseInt(request.get("pregunta9").toString()));
            test.setPregunta10(Integer.parseInt(request.get("pregunta10").toString()));

            // ASIGNAR LA FECHA ACTUAL ANTES DE CALCULAR RESULTADOS
            test.setFechaRealizacion(LocalDateTime.now());

            // Calcular resultados (puntuacionTotal y nivelAnsiedad)
            test.calcularResultados();

            TestAnsiedad guardado = testService.guardarTest(test);

            Map<String, Object> response = new HashMap<>();
            response.put("id", guardado.getId());
            response.put("puntuacionTotal", guardado.getPuntuacionTotal());
            response.put("nivelAnsiedad", guardado.getNivelAnsiedad());
            response.put("fechaRealizacion", guardado.getFechaRealizacion());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error al guardar test: " + e.getMessage()));
        }
    }

    @GetMapping("/historial-test/{usuarioId}")
    public ResponseEntity<?> obtenerHistorial(@PathVariable Long usuarioId) {
        try {
            List<TestAnsiedad> historial = testService.obtenerHistorialPorUsuario(usuarioId);
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerTest(@PathVariable Long id) {
        try {
            TestAnsiedad test = testService.obtenerTestPorId(id);
            return ResponseEntity.ok(test);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}