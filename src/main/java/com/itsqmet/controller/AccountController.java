package com.itsqmet.service;

import com.itsqmet.entity.Account;
import com.itsqmet.entity.Usuario;
import com.itsqmet.repository.AccountRepository;
import com.itsqmet.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ========== MÉTODOS PARA AccountController ==========

    // 1. MOSTRAR TODAS LAS CUENTAS
    public List<Account> mostrarCuentas() {
        return accountRepository.findAll();
    }

    // 2. BUSCAR CUENTA POR ID
    public Optional<Account> buscarUserById(Long id) {
        return accountRepository.findById(id);
    }

    // 3. GUARDAR CUENTA (para CRUD)
    @Transactional
    public Account guardarCuenta(Account account) {
        // Verificar si es nueva cuenta o actualización
        if (account.getId() == null) {
            // Es nueva: encriptar contraseña
            if (account.getPassword() != null && !account.getPassword().isEmpty()) {
                String passwordEncriptada = passwordEncoder.encode(account.getPassword());
                account.setPassword(passwordEncriptada);
            }
        } else {
            // Es actualización: mantener la contraseña actual si no se proporciona nueva
            Optional<Account> cuentaExistente = accountRepository.findById(account.getId());
            if (cuentaExistente.isPresent() &&
                    (account.getPassword() == null || account.getPassword().isEmpty())) {
                account.setPassword(cuentaExistente.get().getPassword());
            } else if (account.getPassword() != null && !account.getPassword().isEmpty()) {
                // Si se proporciona nueva contraseña, encriptarla
                String passwordEncriptada = passwordEncoder.encode(account.getPassword());
                account.setPassword(passwordEncriptada);
            }
        }
        return accountRepository.save(account);
    }

    // 4. ELIMINAR CUENTA
    @Transactional
    public void eliminarCuenta(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        if (account.isPresent()) {
            // Primero eliminar usuario asociado (si existe)
            Usuario usuario = account.get().getUsuario();
            if (usuario != null) {
                usuarioRepository.delete(usuario);
            }
            // Luego eliminar la cuenta
            accountRepository.deleteById(id);
        }
    }

    // ========== MÉTODOS EXISTENTES ==========

    // 5. VERIFICAR SI EL USERNAME EXISTE
    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    // 6. VERIFICAR SI EL EMAIL EXISTE
    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    // 7. REGISTRAR COMPLETO (con transacción)
    @Transactional
    public void registrarCompleto(Account account, Usuario usuario) {
        // 1. Encriptar la contraseña
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        // 2. Guardar la cuenta primero
        Account savedAccount = accountRepository.save(account);

        // 3. Asociar el usuario con la cuenta
        usuario.setAccount(savedAccount);

        // 4. Guardar el usuario
        usuarioRepository.save(usuario);
    }

    // ========== MÉTODOS ADICIONALES ÚTILES ==========

    // 8. BUSCAR CUENTA POR USERNAME
    public Optional<Account> buscarPorUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    // 9. ACTUALIZAR CONTRASEÑA
    @Transactional
    public void actualizarPassword(Long accountId, String nuevaPassword) {
        Optional<Account> accountOpt = accountRepository.findById(accountId);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            account.setPassword(passwordEncoder.encode(nuevaPassword));
            accountRepository.save(account);
        }
    }

    // 10. CONTAR TOTAL DE CUENTAS
    public long contarCuentas() {
        return accountRepository.count();
    }

    // 11. BUSCAR CUENTAS POR ROL
    public List<Account> buscarPorRol(String rol) {
        return accountRepository.findByRol(rol);
    }
}