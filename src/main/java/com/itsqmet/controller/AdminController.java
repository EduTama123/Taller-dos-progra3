package com.itsqmet.controller;

import com.itsqmet.entity.Account;
import com.itsqmet.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AccountService accountService;

    // PANEL DE ADMINISTRACIÓN - CORREGIDO
    @GetMapping("/panel")
    public String panelAdmin(Authentication authentication, Model model) {
        // 1. Verificar si está autenticado con Spring Security
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        // 2. Verificar si tiene rol ADMIN usando Spring Security
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return "redirect:/login?error=No autorizado";
        }

        // 3. Obtener cuenta para mostrar en el modelo
        String username = authentication.getName();
        try {
            Account account = accountService.buscarPorUsername(username)
                    .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
            model.addAttribute("account", account);
        } catch (Exception e) {
            // Si falla, continuamos sin la cuenta en el modelo
            System.out.println("DEBUG: No se pudo cargar cuenta: " + e.getMessage());
        }

        return "pages/panelAdmin";
    }

    // VER TODOS LOS USUARIOS
    @GetMapping("/usuarios")
    public String verUsuarios(Model model) {
        return "redirect:/cuentas";
    }

    // VER TODOS LOS TESTS
    @GetMapping("/tests")
    public String verTests(Model model) {
        return "redirect:/test";
    }

    // VER TODAS LAS RECOMENDACIONES
    @GetMapping("/recomendaciones")
    public String verRecomendaciones(Model model) {
        return "redirect:/recomendaciones";
    }
}