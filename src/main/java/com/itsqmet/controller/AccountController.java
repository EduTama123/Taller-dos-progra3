package com.itsqmet.controller;

import com.itsqmet.entity.Account;
import com.itsqmet.service.AccountService;
import com.itsqmet.roles.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
@RequestMapping("/cuentas")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // LEER TODAS LAS CUENTAS
    @GetMapping
    public String listaCuentas(Model model) {
        model.addAttribute("accounts", accountService.mostrarCuentas());
        return "pages/listaCuentas";
    }

    // FORMULARIO PARA CREAR CUENTA (PASO 1)
    @GetMapping("/formCuenta")
    public String crearCuenta(Model model) {
        model.addAttribute("account", new Account());
        return "pages/userRegister";
    }

    // GUARDAR CUENTA (PASO 1) y REDIRIGIR A DATOS PERSONALES
    @PostMapping("/registrarCuenta")
    public String guardarCuenta(@ModelAttribute Account account, Model model) {
        // 1. Encriptar contraseña
        String passwordEncriptada = passwordEncoder.encode(account.getPassword());
        account.setPassword(passwordEncriptada);

        // 2. Asignar rol por defecto
        account.setRol(Rol.ROLE_USUARIO);

        // 3. Guardar cuenta
        Account cuentaGuardada = accountService.guardarCuenta(account);

        // 4. Pasar ID de cuenta al formulario de datos personales
        model.addAttribute("accountId", cuentaGuardada.getId());

        // 5. Redirigir a formulario de datos personales
        return "redirect:/usuarios/formUsuario?accountId=" + cuentaGuardada.getId();
    }

    // FORMULARIO PARA EDITAR CUENTA
    @GetMapping("/editarCuenta/{id}")
    public String actualizarCuenta(@PathVariable Long id, Model model) {
        Optional<Account> account = accountService.buscarUserById(id);
        model.addAttribute("account", account.orElse(new Account()));
        return "pages/userRegister";
    }

    // ELIMINAR CUENTA
    @GetMapping("/eliminarCuenta/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        accountService.eliminarCuenta(id);
        return "redirect:/cuentas";
    }
}