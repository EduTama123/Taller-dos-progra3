package com.itsqmet.controller;

import com.itsqmet.entity.Account;
import com.itsqmet.entity.TestAnsiedad;
import com.itsqmet.entity.Usuario;
import com.itsqmet.service.AccountService;
import com.itsqmet.service.TestService;
import com.itsqmet.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/usuario")
public class UsuarioPanelController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TestService testService;

    @GetMapping("/panel")
    public String panelUsuario(Authentication authentication, Model model) {
        if (!verificarAutenticacionUsuario(authentication)) {
            return "redirect:/login?error=No autorizado";
        }

        String username = authentication.getName();
        Account account = accountService.buscarPorUsername(username)
                .orElseThrow(() -> new RuntimeException("CUENTA NO ENCONTRADA"));
        Usuario usuario = accountService.obtenerUsuarioDeCuenta(account);
        Long cantidadTests = testService.contarTestsPorUsuario(usuario.getId());

        model.addAttribute("usuario", usuario);
        model.addAttribute("cantidadTests", cantidadTests);

        return "pages/panelUsuario";
    }

    @GetMapping("/test")
    public String realizarTest(Authentication authentication, Model model) {
        if (!verificarAutenticacionUsuario(authentication)) {
            return "redirect:/login";
        }

        TestAnsiedad testAnsiedad = new TestAnsiedad();
        model.addAttribute("testAnsiedad", testAnsiedad);
        return "pages/testForm";
    }

    @PostMapping("/test")
    public String guardarTest(
            @ModelAttribute("testAnsiedad") TestAnsiedad testAnsiedad,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        if (!verificarAutenticacionUsuario(authentication)) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        Account account = accountService.buscarPorUsername(username)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        Usuario usuario = accountService.obtenerUsuarioDeCuenta(account);

        testAnsiedad.setUsuario(usuario);
        testAnsiedad.calcularResultados();
        testService.guardarTest(testAnsiedad);

        redirectAttributes.addFlashAttribute("mensajeExito", "Test completado exitosamente!");
        return "redirect:/usuario/historial";
    }

    @GetMapping("/historial")
    public String verHistorial(Authentication authentication, Model model) {
        if (!verificarAutenticacionUsuario(authentication)) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        Account account = accountService.buscarPorUsername(username)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        Usuario usuario = accountService.obtenerUsuarioDeCuenta(account);
        List<TestAnsiedad> tests = testService.obtenerTestsPorUsuario(usuario);

        model.addAttribute("usuario", usuario);
        model.addAttribute("tests", tests);

        return "pages/historialUsuario";
    }

    @GetMapping("/datos")
    public String verDatosPersonales(Authentication authentication, Model model) {
        if (!verificarAutenticacionUsuario(authentication)) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        Account account = accountService.buscarPorUsername(username)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        Usuario usuario = accountService.obtenerUsuarioDeCuenta(account);

        // Pasar la cuenta asociada al modelo
        model.addAttribute("usuario", usuario);
        model.addAttribute("cuentaAsociada", account); // Para mostrar en el formulario

        return "pages/usuarioForm"; // Mismo formulario pero con datos precargados
    }

    @PostMapping("/datos")
    public String guardarDatosPersonales(
            @Valid @ModelAttribute Usuario usuarioForm,
            BindingResult result,
            Authentication authentication,
            Model model) {

        if (!verificarAutenticacionUsuario(authentication)) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            model.addAttribute("usuario", usuarioForm);
            return "pages/usuarioForm";
        }

        String username = authentication.getName();
        Account account = accountService.buscarPorUsername(username)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
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

    private boolean verificarAutenticacionUsuario(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_USUARIO"));
    }
}