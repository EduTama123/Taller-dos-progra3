package com.itsqmet.controller;

import com.itsqmet.entity.Account;
import com.itsqmet.entity.TestAnsiedad;
import com.itsqmet.entity.Usuario;
import com.itsqmet.service.AccountService;
import com.itsqmet.service.TestService;
import com.itsqmet.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuario")
public class UsuarioPanelController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TestService testService;

    // PANEL PRINCIPAL DEL USUARIO
    @GetMapping("/panel")
    public String panelUsuario(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        // Obtener username desde Authentication (como en tu código base)
        String username = authentication.getName();

        // Buscar cuenta por username
        Account account = accountService.buscarPorUsername(username)
                .orElseThrow(() -> new RuntimeException("CUENTA NO ENCONTRADA"));

        // Obtener usuario relacionado
        Usuario usuario = accountService.obtenerUsuarioDeCuenta(account);

        model.addAttribute("usuario", usuario);
        return "pages/panelUsuario";
    }

    // FORMULARIO PARA REALIZAR TEST
    @GetMapping("/test")
    public String realizarTest(HttpSession session, Model model) {
        Account account = obtenerCuentaDeSesion(session);
        if (account == null) return "redirect:/login";

        model.addAttribute("test", new TestAnsiedad());
        return "pages/testForm";
    }

    // GUARDAR TEST REALIZADO
    @PostMapping("/test")
    public String guardarTest(
            @ModelAttribute TestAnsiedad test,
            HttpSession session) {

        Account account = obtenerCuentaDeSesion(session);
        if (account == null) return "redirect:/login";

        // OBTENER USUARIO Y ASOCIAR AL TEST
        Usuario usuario = accountService.obtenerUsuarioDeCuenta(account);
        test.setUsuario(usuario);

        // GUARDAR TEST
        testService.guardarTest(test);

        return "redirect:/usuario/historial";
    }

    // VER HISTORIAL DE TESTS DEL USUARIO
    @GetMapping("/historial")
    public String verHistorial(HttpSession session, Model model) {
        Account account = obtenerCuentaDeSesion(session);
        if (account == null) return "redirect:/login";

        Usuario usuario = accountService.obtenerUsuarioDeCuenta(account);
        var tests = testService.obtenerTestsPorUsuario(usuario);

        model.addAttribute("tests", tests);
        return "pages/historialUsuario";
    }

    // VER DATOS PERSONALES
    @GetMapping("/datos")
    public String verDatosPersonales(HttpSession session, Model model) {
        Account account = obtenerCuentaDeSesion(session);
        if (account == null) return "redirect:/login";

        Usuario usuario = accountService.obtenerUsuarioDeCuenta(account);
        model.addAttribute("usuario", usuario);
        return "pages/usuarioForm";
    }

    // ACTUALIZAR DATOS PERSONALES
    @PostMapping("/datos")
    public String guardarDatosPersonales(
            @Valid @ModelAttribute Usuario usuarioForm,
            BindingResult result,
            HttpSession session,
            Model model) {

        Account account = obtenerCuentaDeSesion(session);
        if (account == null) return "redirect:/login";

        if (result.hasErrors()) {
            model.addAttribute("usuario", usuarioForm);
            return "pages/usuarioForm";
        }

        // OBTENER USUARIO ACTUAL Y ACTUALIZAR DATOS
        Usuario usuario = accountService.obtenerUsuarioDeCuenta(account);
        usuario.setNombre(usuarioForm.getNombre());
        usuario.setApellido(usuarioForm.getApellido());
        usuario.setCedula(usuarioForm.getCedula());
        usuario.setDireccion(usuarioForm.getDireccion());
        usuario.setTelefono(usuarioForm.getTelefono());
        usuario.setEdad(usuarioForm.getEdad());

        usuarioService.guardarUsuario(usuario);
        return "redirect:/usuario/panel";
    }

    // METODO AUXILIAR PARA OBTENER CUENTA DE SESION
    private Account obtenerCuentaDeSesion(HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        if (account == null || !account.getRol().name().equals("ROLE_USUARIO")) {
            return null;
        }
        return account;
    }
}