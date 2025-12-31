package com.itsqmet.repository;

import com.itsqmet.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // BUSCAR USUARIO POR CEDULA EXACTA
    Usuario findByCedula(String cedula);

    // BUSCAR USUARIO POR ID DE CUENTA ASOCIADA
    Usuario findByAccountId(Long accountId);
}