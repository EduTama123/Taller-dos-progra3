package com.itsqmet.controller;

import com.itsqmet.entity.Account;
import com.itsqmet.entity.Recomendacion;
import com.itsqmet.entity.TestAnsiedad;
import com.itsqmet.entity.Usuario;
import com.itsqmet.service.RecomendacionService;
import com.itsqmet.service.TestService;
import com.itsqmet.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/especialista")
public class EspecialistaController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TestService testService;

    @Autowired
    private RecomendacionService recomendacionService;

    // PANEL PRINCIPAL DEL ESPECIALISTA
    @GetMapping("/panel")
    public String panelEspecialista(HttpSession session, Model model) {
        if (!verificarSesionEspecialista(session)) {
            return "redirect:/login";
        }
        return "pages/panelEspecialista";
    }

    // LISTA DE TODOS LOS USUARIOS
    @GetMapping("/usuarios")
    public String listaUsuarios(Model model) {
        var usuarios = usuarioService.mostrarUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "pages/listaUsuarios";
    }

    // HISTORIAL DE TESTS DE UN USUARIO ESPECIFICO
    @GetMapping("/historial/{usuarioId}")
    public String verHistorialUsuario(
            @PathVariable Long usuarioId,
            Model model) {

        Usuario usuario = usuarioService.buscarUsuarioById(usuarioId);
        var tests = testService.obtenerTestsPorUsuario(usuarioId);

        model.addAttribute("usuario", usuario);
        model.addAttribute("tests", tests);
        return "pages/historialEspecialista";
    }

    // FORMULARIO PARA AÑADIR RECOMENDACION A UN TEST
    @GetMapping("/recomendacion/{testId}")
    public String formRecomendacion(
            @PathVariable Long testId,
            Model model) {

        TestAnsiedad test = testService.buscarTestById(testId);

        // BUSCAR SI YA EXISTE RECOMENDACION PARA ESE NIVEL
        Recomendacion recomendacion = recomendacionService
                .buscarPorNivelAnsiedad(test.getNivelAnsiedad());

        if (recomendacion == null) {
            recomendacion = new Recomendacion();
            recomendacion.setNivelAnsiedad(test.getNivelAnsiedad());
        }

        model.addAttribute("test", test);
        model.addAttribute("recomendacion", recomendacion);
        return "pages/recomendacionForm";
    }

    // GUARDAR RECOMENDACION
    @PostMapping("/recomendacion/{testId}")
    public String guardarRecomendacion(
            @PathVariable Long testId,
            @ModelAttribute Recomendacion recomendacionForm) {

        TestAnsiedad test = testService.buscarTestById(testId);

        // BUSCAR O CREAR RECOMENDACION
        Recomendacion recomendacion = recomendacionService
                .buscarPorNivelAnsiedad(test.getNivelAnsiedad());

        if (recomendacion == null) {
            recomendacion = new Recomendacion();
            recomendacion.setNivelAnsiedad(test.getNivelAnsiedad());
        }

        // ACTUALIZAR RECOMENDACIONES
        recomendacion.setRecomendacion1(recomendacionForm.getRecomendacion1());
        recomendacion.setRecomendacion2(recomendacionForm.getRecomendacion2());
        recomendacion.setRecomendacion3(recomendacionForm.getRecomendacion3());
        recomendacion.setRecomendacion4(recomendacionForm.getRecomendacion4());
        recomendacion.setRecomendacion5(recomendacionForm.getRecomendacion5());

        Recomendacion recomendacionGuardada = recomendacionService
                .guardarRecomendacion(recomendacion);

        // ASIGNAR RECOMENDACION AL TEST
        test.setRecomendacion(recomendacionGuardada);
        testService.guardarTest(test);

        return "redirect:/especialista/historial/" + test.getUsuario().getId();
    }

    // METODO AUXILIAR PARA VERIFICAR SESION
    private boolean verificarSesionEspecialista(HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        return account != null && account.getRol().name().equals("ROLE_ESPECIALISTA");
    }
}