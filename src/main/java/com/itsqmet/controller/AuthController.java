package com.itsqmet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {
//
//    @Autowired
//    private AccountService accountService;
//
//    // GET para mostrar el formulario de registro
//    @GetMapping("/registro")
//    public String mostrarRegistro() {
//        // Redirige al formulario de cuentas que YA existe
//        return "redirect:/cuentas/formCuenta";
//    }
//
//    // POST para procesar el registro
//    @PostMapping("/registro")
//    public String procesarRegistro(
//            @RequestParam String username,
//            @RequestParam String email,
//            @RequestParam String password,
//            @RequestParam String confirmPassword,
//            @RequestParam String nombre,
//            @RequestParam String apellido,
//            @RequestParam String cedula,
//            @RequestParam Integer edad,
//            @RequestParam String telefono,
//            @RequestParam String direccion,
//            Model model) {
//
//        // 1. Validar contraseñas
//        if (!password.equals(confirmPassword)) {
//            model.addAttribute("error", "Las contraseñas no coinciden");
//            return "cuentas/formCuenta"; // IMPORTANTE: Sin "pages/"
//        }
//
//        // 2. Validar que el usuario no exista
//        if (accountService.existsByUsername(username)) {
//            model.addAttribute("error", "El nombre de usuario ya está en uso");
//            return "cuentas/formCuenta";
//        }
//
//        // 3. Validar que el email no exista
//        if (accountService.existsByEmail(email)) {
//            model.addAttribute("error", "El correo electrónico ya está registrado");
//            return "cuentas/formCuenta";
//        }
//
//        // 4. Crear Account
//        Account account = new Account();
//        account.setUsername(username);
//        account.setEmail(email);
//        account.setPassword(password);
//        account.setRol("USUARIO"); // Rol por defecto para registros públicos
//
//        // 5. Crear Usuario
//        Usuario usuario = new Usuario();
//        usuario.setNombre(nombre);
//        usuario.setApellido(apellido);
//        usuario.setCedula(cedula);
//        usuario.setEdad(edad);
//        usuario.setTelefono(telefono);
//        usuario.setDireccion(direccion);
//
//        try {
//            // 6. Guardar ambos en una transacción
//            accountService.registrarCompleto(account, usuario);
//
//            // 7. Redirigir a login con mensaje de éxito
//            return "redirect:/login?registroExitoso=true";
//
//        } catch (Exception e) {
//            model.addAttribute("error", "Error en el registro: " + e.getMessage());
//            return "cuentas/formCuenta";
//        }
//    }
}