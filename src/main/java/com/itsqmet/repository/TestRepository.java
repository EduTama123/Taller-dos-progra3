package com.itsqmet.repository;

import com.itsqmet.entity.TestAnsiedad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<TestAnsiedad, Long> {

    // BUSCAR TESTS POR ID DE USUARIO (PARA HISTORIAL)
    List<TestAnsiedad> findByUsuarioId(Long usuarioId);

    // BUSCAR TESTS SIN RECOMENDACION (PARA ESPECIALISTA)
    List<TestAnsiedad> findByRecomendacionIsNull();
}