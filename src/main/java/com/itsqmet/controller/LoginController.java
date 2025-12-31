package com.itsqmet.controller;

import org.springframework.security.core.Authentication;
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

    // Redirigir por roles
    @GetMapping("/login/postLogin")
    public String dirigirPorRol(Authentication authentication) {
        // Obtener el objeto usuario que acaba de iniciar sesion
        User usuario = (User) authentication.getPrincipal();

        // Procesa la lista de roles o permisos que tiene el usuario
        String role = usuario.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .findFirst()
                .orElse("");

        // REDIRECCIÓN SEGÚN ROLES DE CURESTRESS
        if (role.equals("ROLE_ADMIN")) {
            return "redirect:/admin/panel";
        } else if (role.equals("ROLE_ESPECIALISTA")) {
            return "redirect:/especialista/panel";
        } else if (role.equals("ROLE_USUARIO")) {
            return "redirect:/usuario/panel";
        }

        return "redirect:/login?error";
    }
}