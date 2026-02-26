package com.itsqmet.repository;

import com.itsqmet.entity.Usuario;
import com.itsqmet.roles.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Búsquedas por campos únicos
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);

    // Verificaciones de existencia
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Búsquedas por otros campos
    List<Usuario> findByRol(Rol rol);


    // Búsqueda por nombre (contiene, ignorando mayúsculas)
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);

    // Ordenamientos
    List<Usuario> findAllByOrderByNombreAsc();
}