package com.itsqmet.service;

import com.itsqmet.entity.Account;
import com.itsqmet.entity.Usuario;
import com.itsqmet.repository.AccountRepository;
import com.itsqmet.repository.UsuarioRepository;
import com.itsqmet.roles.Rol;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // LEER TODAS LAS CUENTAS
    public List<Account> mostrarCuentas() {
        return accountRepository.findAll();
    }

    // BUSCAR CUENTA POR ID
    public Optional<Account> buscarUserById(Long id) {
        return accountRepository.findById(id);
    }

    // BUSCAR CUENTA POR USERNAME
    public Optional<Account> buscarPorUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    // GUARDAR CUENTA CON USUARIO (PARA REGISTRO)
    @Transactional
    public Account guardarCuentaConUsuario(Account account, Usuario usuario) {
        // ENCRIPTAR CONTRASENA
        String passwordEncriptada = passwordEncoder.encode(account.getPassword());
        account.setPassword(passwordEncriptada);

        // ASIGNAR ROL POR DEFECTO SI NO TIENE
        if (account.getRol() == null) {
            account.setRol(Rol.ROLE_USUARIO);
        }

        // GUARDAR CUENTA
        Account cuentaGuardada = accountRepository.save(account);

        // VINCULAR USUARIO CON CUENTA
        usuario.setAccount(cuentaGuardada);
        usuarioRepository.save(usuario);

        // VINCULAR CUENTA CON USUARIO (RELACION BIDIRECCIONAL)
        cuentaGuardada.setUsuario(usuario);
        accountRepository.save(cuentaGuardada);

        return cuentaGuardada;
    }

    // ACTUALIZAR CUENTA
    public Account actualizarCuenta(Long id, Account account) {
        Account cuentaExistente = buscarUserById(id)
                .orElseThrow(() -> new RuntimeException("CUENTA NO ENCONTRADA"));

        cuentaExistente.setNombre(account.getNombre());
        cuentaExistente.setUsername(account.getUsername());

        // ACTUALIZAR PASSWORD SOLO SI SE PROVEE UNA NUEVA
        if (account.getPassword() != null && !account.getPassword().isBlank()) {
            cuentaExistente.setPassword(passwordEncoder.encode(account.getPassword()));
        }

        // ACTUALIZAR ROL (SOLO ADMIN PUEDE)
        if (account.getRol() != null) {
            cuentaExistente.setRol(account.getRol());
        }

        return accountRepository.save(cuentaExistente);
    }

    // ELIMINAR CUENTA
    @Transactional
    public void eliminarCuenta(Long id) {
        Account account = buscarUserById(id)
                .orElseThrow(() -> new RuntimeException("CUENTA NO EXISTE"));
        accountRepository.delete(account);
    }

    // LOGIN
    public Account login(String username, String password) {
        return accountRepository.findByUsernameAndPassword(username, password);
    }

    // OBTENER USUARIO ASOCIADO A UNA CUENTA
    public Usuario obtenerUsuarioDeCuenta(Account account) {
        return usuarioRepository.findByAccountId(account.getId());
    }

    // GUARDAR CUENTA
    public Account guardarCuenta(Account account) {
        // Encriptar contraseña si no está encriptada
        if (!account.getPassword().startsWith("$2a$")) {
            String passwordEncriptada = passwordEncoder.encode(account.getPassword());
            account.setPassword(passwordEncriptada);
        }

        return accountRepository.save(account);
    }

    // SPRING SECURITY - CARGA USUARIO POR USERNAME
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("USUARIO NO EXISTE: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .authorities(account.getRol().name())
                .build();
    }
}