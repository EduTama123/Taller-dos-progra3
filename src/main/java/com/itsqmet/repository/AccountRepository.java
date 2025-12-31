package com.itsqmet.repository;

import com.itsqmet.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    // Métodos existentes
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<Account> findByUsername(String username);

    // Métodos nuevos necesarios
    List<Account> findByRol(String rol);

    // Buscar por nombre (opcional)
    @Query("SELECT a FROM Account a WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Account> buscarPorNombre(@Param("nombre") String nombre);

    // Buscar cuenta con usuario (join)
    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.usuario WHERE a.id = :id")
    Optional<Account> findByIdWithUsuario(@Param("id") Long id);
}