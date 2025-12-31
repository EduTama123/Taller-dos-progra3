package com.itsqmet.controller;

import com.itsqmet.entity.Account;
import com.itsqmet.entity.Usuario;
import com.itsqmet.service.AccountService;
import com.itsqmet.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/formUsuario")
    public String mostrarFormularioUsuario(
            @RequestParam(value = "accountId", required = false) Long accountId,
            Model model) {

        List<Account> cuentas = accountService.findAll();
        Usuario usuario = new Usuario();

        if (accountId != null) {
            Account account = accountService.findById(accountId)
                    .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
            usuario.setAccount(account);
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("cuentas", cuentas);
        model.addAttribute("accountId", accountId);

        return "pages/usuarioForm";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String cedula,
            @RequestParam Integer edad,
            @RequestParam String telefono,
            @RequestParam String direccion,
            @RequestParam(value = "accountId", required = false) Long accountId,
            Model model) {

        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCedula(cedula);
        usuario.setEdad(edad);
        usuario.setTelefono(telefono);
        usuario.setDireccion(direccion);

        // Asociar cuenta si viene accountId
        if (accountId != null) {
            Account account = accountService.findById(accountId)
                    .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
            usuario.setAccount(account);
        }

        // Validar que tenga cuenta asociada
        if (usuario.getAccount() == null) {
            List<Account> cuentas = accountService.findAll();
            model.addAttribute("cuentas", cuentas);
            model.addAttribute("usuario", usuario);
            model.addAttribute("error", "Debe seleccionar una cuenta asociada");
            return "pages/usuarioForm";
        }

        usuarioService.guardarUsuario(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/formUsuario/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarUsuarioById(id);
        List<Account> cuentas = accountService.findAll();
        model.addAttribute("usuario", usuario);
        model.addAttribute("cuentas", cuentas);
        return "pages/usuarioForm";
    }
}