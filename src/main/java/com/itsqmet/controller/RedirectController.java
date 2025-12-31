package com.itsqmet.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

    @GetMapping("/redirectByRole")
    public String redirectByRole(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        // VERIFICAR ROLES
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin/panel";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ESPECIALISTA"))) {
            return "redirect:/especialista/panel";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USUARIO"))) {
            return "redirect:/usuario/panel";
        }

        return "redirect:/";
    }
}