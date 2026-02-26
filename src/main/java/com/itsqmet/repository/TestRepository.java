package com.itsqmet.repository;

import com.itsqmet.entity.TestAnsiedad;
import com.itsqmet.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<TestAnsiedad, Long> {

    List<TestAnsiedad> findByUsuarioOrderByFechaRealizacionDesc(Usuario usuario);
}