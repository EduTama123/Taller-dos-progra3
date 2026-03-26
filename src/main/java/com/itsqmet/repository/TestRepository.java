package com.itsqmet.repository;

import com.itsqmet.entity.TestAnsiedad;
import com.itsqmet.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<TestAnsiedad, Long> {

    List<TestAnsiedad> findByUsuarioOrderByFechaRealizacionDesc(Usuario usuario);
    @Procedure(procedureName = "sp_historial_tests_usuario")
    void obtenerHistorialUsuario(
            @Param("p_usuario_id") Long usuarioId,
            @Param("p_total_tests") Integer[] totalTests,
            @Param("p_promedio_puntuacion") Double[] promedio,
            @Param("p_ultimo_nivel") String[] ultimoNivel
    );
    // Métodos para cálculos manuales (fallback)
    @Query("SELECT COUNT(t) FROM TestAnsiedad t WHERE t.usuario.id = :usuarioId")
    Long countByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT AVG(t.puntuacionTotal) FROM TestAnsiedad t WHERE t.usuario.id = :usuarioId")
    Double getAveragePuntuacionByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT t.nivelAnsiedad FROM TestAnsiedad t WHERE t.usuario.id = :usuarioId ORDER BY t.fechaRealizacion DESC LIMIT 1")
    String getUltimoNivelByUsuarioId(@Param("usuarioId") Long usuarioId);
}
