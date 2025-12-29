package com.itsqmet.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/inicio")
    public String inicio(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "index"; // Página pública
        }

        // Si está autenticado, redirigir según rol
        User usuario = (User) authentication.getPrincipal();
        String role = usuario.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .findFirst()
                .orElse("");

        if (role.equals("ROLE_ADMIN")) {
            return "redirect:/admin";
        } else if (role.equals("ROLE_ESPECIALISTA")) {
            return "redirect:/especialista";
        } else if (role.equals("ROLE_USUARIO")) {
            return "redirect:/usuario";
        }

        return "pages/index";
    }
}
