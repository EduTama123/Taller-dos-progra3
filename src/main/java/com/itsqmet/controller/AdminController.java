package com.itsqmet.controller;

import com.itsqmet.entity.Account;
import com.itsqmet.service.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AccountService accountService;

    // PANEL DE ADMINISTRACION
    @GetMapping("/panel")
    public String panelAdmin(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        if (account == null || !account.getRol().name().equals("ROLE_ADMIN")) {
            return "redirect:/login";
        }
        return "pages/panelAdmin";
    }

    // VER TODOS LOS USUARIOS
    @GetMapping("/usuarios")
    public String verUsuarios(Model model) {
        // AQUI LLAMARIAMOS AL SERVICIO DE USUARIOS
        return "redirect:/cuentas"; // O LA RUTA QUE MUESTRA USUARIOS
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