package com.itsqmet.service;

import com.itsqmet.entity.Recomendacion;
import com.itsqmet.repository.RecomendacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RecomendacionService {

    @Autowired
    private RecomendacionRepository recomendacionRepository;

    // LEER TODAS LAS RECOMENDACIONES
    public List<Recomendacion> mostrarRecomendaciones() {
        return recomendacionRepository.findAll();
    }

    // BUSCAR RECOMENDACION POR ID
    public Recomendacion buscarRecomendacionById(Long id) {
        return recomendacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RECOMENDACION NO ENCONTRADA"));
    }

    // BUSCAR RECOMENDACION POR NIVEL DE ANSIEDAD
    public Recomendacion buscarPorNivelAnsiedad(String nivel) {
        return recomendacionRepository.findByNivelAnsiedad(nivel);
    }

    // GUARDAR RECOMENDACION
    public Recomendacion guardarRecomendacion(Recomendacion recomendacion) {
        return recomendacionRepository.save(recomendacion);
    }

    // ACTUALIZAR RECOMENDACION
    public Recomendacion actualizarRecomendacion(Long id, Recomendacion recomendacionActualizada) {
        Recomendacion recomendacionExistente = buscarRecomendacionById(id);

        recomendacionExistente.setNivelAnsiedad(recomendacionActualizada.getNivelAnsiedad());
        recomendacionExistente.setRecomendacion1(recomendacionActualizada.getRecomendacion1());
        recomendacionExistente.setRecomendacion2(recomendacionActualizada.getRecomendacion2());
        recomendacionExistente.setRecomendacion3(recomendacionActualizada.getRecomendacion3());
        recomendacionExistente.setRecomendacion4(recomendacionActualizada.getRecomendacion4());
        recomendacionExistente.setRecomendacion5(recomendacionActualizada.getRecomendacion5());

        return recomendacionRepository.save(recomendacionExistente);
    }

    // ELIMINAR RECOMENDACION
    public void eliminarRecomendacion(Long id) {
        recomendacionRepository.deleteById(id);
    }

    // VERIFICAR SI EXISTE RECOMENDACION PARA UN NIVEL
    public boolean existeRecomendacionParaNivel(String nivel) {
        return recomendacionRepository.findByNivelAnsiedad(nivel) != null;
    }
}