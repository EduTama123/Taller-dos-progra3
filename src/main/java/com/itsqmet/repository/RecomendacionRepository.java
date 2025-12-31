package com.itsqmet.repository;

import com.itsqmet.entity.Recomendacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecomendacionRepository extends JpaRepository<Recomendacion, Long> {

    // BUSCAR RECOMENDACION POR NIVEL DE ANSIEDAD
    Recomendacion findByNivelAnsiedad(String nivelAnsiedad);
}