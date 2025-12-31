package com.itsqmet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RedirectController {

    @GetMapping("/cuentas/formCuenta")
    public String redirectToRegistro() {
        return "redirect:/auth/registro";
    }
}