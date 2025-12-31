package com.itsqmet.service;

import com.itsqmet.entity.Account;
import com.itsqmet.entity.Usuario;
import com.itsqmet.repository.AccountRepository;
import com.itsqmet.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Método para verificar si el username existe
    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    // Método para verificar si el email existe
    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    // Método para registrar completo (con transacción)
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
}