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

    // FORMULARIO PARA CREAR USUARIO
    @GetMapping("/formUsuario")
    public String crearUsuario(
            @RequestParam(value = "accountId", required = false) Long accountId,
            Model model) {

        Usuario usuario = new Usuario();

        // Si viene accountId, vincular con la cuenta recién creada
        if (accountId != null) {
            Account account = accountService.buscarUserById(accountId)
                    .orElseThrow(() -> new RuntimeException("CUENTA NO ENCONTRADA"));
            usuario.setAccount(account);
            model.addAttribute("esRegistroNuevo", true);
        }

        model.addAttribute("usuario", usuario);
        return "pages/usuarioForm";
    }

    // GUARDAR USUARIO
    @PostMapping("/registrarUsuario")
    public String guardarUsuario(
            @Valid @ModelAttribute Usuario usuario,
            BindingResult result,
            @RequestParam(value = "esRegistroNuevo", required = false) Boolean esRegistroNuevo,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "pages/usuarioForm";
        }

        // Guardar usuario
        usuarioService.guardarUsuario(usuario);

        // Si es nuevo registro, redirigir a login para que inicie sesión
        if (esRegistroNuevo != null && esRegistroNuevo) {
            return "redirect:/login?registro=completo";
        }

        // Si no es nuevo registro, redirigir a lista de usuarios
        return "redirect:/usuarios";
    }

    // RESTO DE MÉTODOS (mantener igual)
    @GetMapping
    public String listaUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.mostrarUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "pages/usuarioList";
    }

    @GetMapping("/editarUsuario/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarUsuarioById(id);
        model.addAttribute("usuario", usuario);
        return "pages/usuarioForm";
    }

    @PostMapping("/actualizarUsuario/{id}")
    public String procesarActualizacion(@PathVariable Long id, @Valid @ModelAttribute Usuario usuario) {
        usuarioService.actualizarUsuario(id, usuario);
        return "redirect:/usuarios";
    }

    @PostMapping("/eliminarUsuario/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return "redirect:/usuarios";
    }
}