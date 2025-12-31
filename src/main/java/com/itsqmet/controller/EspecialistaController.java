package com.itsqmet.controller;

import com.itsqmet.entity.Account;
import com.itsqmet.entity.Recomendacion;
import com.itsqmet.entity.TestAnsiedad;
import com.itsqmet.entity.Usuario;
import com.itsqmet.service.RecomendacionService;
import com.itsqmet.service.TestService;
import com.itsqmet.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

    // PANEL PRINCIPAL DEL ESPECIALISTA - CORREGIDO
    @GetMapping("/panel")
    public String panelEspecialista(Authentication authentication, Model model) {
        if (!verificarAutenticacionEspecialista(authentication)) {
            return "redirect:/login";
        }
        return "pages/panelEspecialista";
    }

    // LISTA DE TODOS LOS USUARIOS
    @GetMapping("/usuarios")
    public String listaUsuarios(Authentication authentication, Model model) {
        if (!verificarAutenticacionEspecialista(authentication)) {
            return "redirect:/login";
        }
        var usuarios = usuarioService.mostrarUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "pages/listaUsuarios";
    }

    // HISTORIAL DE TESTS DE UN USUARIO ESPECIFICO
    @GetMapping("/historial/{usuarioId}")
    public String verHistorialUsuario(
            @PathVariable Long usuarioId,
            Authentication authentication,
            Model model) {

        if (!verificarAutenticacionEspecialista(authentication)) {
            return "redirect:/login";
        }

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
            Authentication authentication,
            Model model) {

        if (!verificarAutenticacionEspecialista(authentication)) {
            return "redirect:/login";
        }

        TestAnsiedad test = testService.buscarTestById(testId);
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
            @ModelAttribute Recomendacion recomendacionForm,
            Authentication authentication) {

        if (!verificarAutenticacionEspecialista(authentication)) {
            return "redirect:/login";
        }

        TestAnsiedad test = testService.buscarTestById(testId);
        Recomendacion recomendacion = recomendacionService
                .buscarPorNivelAnsiedad(test.getNivelAnsiedad());

        if (recomendacion == null) {
            recomendacion = new Recomendacion();
            recomendacion.setNivelAnsiedad(test.getNivelAnsiedad());
        }

        recomendacion.setRecomendacion1(recomendacionForm.getRecomendacion1());
        recomendacion.setRecomendacion2(recomendacionForm.getRecomendacion2());
        recomendacion.setRecomendacion3(recomendacionForm.getRecomendacion3());
        recomendacion.setRecomendacion4(recomendacionForm.getRecomendacion4());
        recomendacion.setRecomendacion5(recomendacionForm.getRecomendacion5());

        Recomendacion recomendacionGuardada = recomendacionService
                .guardarRecomendacion(recomendacion);

        test.setRecomendacion(recomendacionGuardada);
        testService.guardarTest(test);

        return "redirect:/especialista/historial/" + test.getUsuario().getId();
    }

    // METODO PARA VERIFICAR AUTENTIFICACION
    private boolean verificarAutenticacionEspecialista(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_ESPECIALISTA"));
    }
}