package com.itsqmet.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "registro", required = false) String registro,
            Model model) {

        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
        }

        if (logout != null) {
            model.addAttribute("message", "Has cerrado sesión correctamente");
        }

        if (registro != null) {
            model.addAttribute("message", "Registro completado. Ahora puedes iniciar sesión.");
        }

        return "pages/login";
    }

    // Redirigir por roles (CORREGIDO - usando SecurityContextHolder)
    @GetMapping("/login/postLogin")
    public String dirigirPorRol() {
        // OBTENER Authentication DESDE SecurityContextHolder (CORRECTO)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // DEBUG: Ver qué hay en authentication
        System.out.println("DEBUG - Authentication: " + authentication);
        if (authentication != null) {
            System.out.println("DEBUG - Is authenticated: " + authentication.isAuthenticated());
            System.out.println("DEBUG - Principal: " + authentication.getPrincipal());
            System.out.println("DEBUG - Authorities: " + authentication.getAuthorities());
        }

        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("DEBUG - No autenticado, redirigiendo a login con error");
            return "redirect:/login?error";
        }

        // Verificar que el principal sea válido
        Object principal = authentication.getPrincipal();

        // Si principal es String "anonymousUser", no está autenticado
        if (principal instanceof String) {
            String principalStr = (String) principal;
            if (principalStr.equals("anonymousUser")) {
                System.out.println("DEBUG - Principal es anonymousUser");
                return "redirect:/login?error";
            }
        }

        // Si principal es User de Spring Security
        if (principal instanceof User) {
            User usuario = (User) principal;

            // Procesa la lista de roles
            String role = usuario.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .findFirst()
                    .orElse("");

            System.out.println("DEBUG - Rol detectado: " + role);

            // REDIRECCIÓN SEGÚN ROLES DE CURESTRESS
            if (role.equals("ROLE_ADMIN")) {
                System.out.println("DEBUG - Redirigiendo a /admin/panel");
                return "redirect:/admin/panel";
            } else if (role.equals("ROLE_ESPECIALISTA")) {
                System.out.println("DEBUG - Redirigiendo a /especialista/panel");
                return "redirect:/especialista/panel";
            } else if (role.equals("ROLE_USUARIO")) {
                System.out.println("DEBUG - Redirigiendo a /usuario/panel");
                return "redirect:/usuario/panel";
            }
        } else {
            System.out.println("DEBUG - Principal no es User, es: " + principal.getClass().getName());
        }

        System.out.println("DEBUG - Redirigiendo a login por error general");
        return "redirect:/login?error";
    }
}