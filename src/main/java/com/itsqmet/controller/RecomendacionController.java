package com.itsqmet.controller;

import com.itsqmet.entity.Recomendacion;
import com.itsqmet.service.RecomendacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/recomendaciones")
public class RecomendacionController {

    @Autowired
    private RecomendacionService recomendacionService;

    // LEER TODAS LAS RECOMENDACIONES
    @GetMapping
    public String listaRecomendaciones(Model model) {
        var recomendaciones = recomendacionService.mostrarRecomendaciones();
        model.addAttribute("recomendaciones", recomendaciones);
        return "pages/recomendacion";
    }

    // FORMULARIO PARA CREAR RECOMENDACION
    @GetMapping("/form")
    public String crearRecomendacion(Model model) {
        model.addAttribute("recomendacion", new Recomendacion());
        return "pages/recomendacionForm";
    }

    // GUARDAR RECOMENDACION
    @PostMapping("/guardar")
    public String guardarRecomendacion(@ModelAttribute Recomendacion recomendacion) {
        recomendacionService.guardarRecomendacion(recomendacion);
        return "redirect:/recomendaciones";
    }

    // FORMULARIO PARA EDITAR RECOMENDACION
    @GetMapping("/editar/{id}")
    public String editarRecomendacion(@PathVariable Long id, Model model) {
        Recomendacion recomendacion = recomendacionService.buscarRecomendacionById(id);
        model.addAttribute("recomendacion", recomendacion);
        return "pages/recomendacionForm";
    }

    // ELIMINAR RECOMENDACION
    @GetMapping("/eliminar/{id}")
    public String eliminarRecomendacion(@PathVariable Long id) {
        recomendacionService.eliminarRecomendacion(id);
        return "redirect:/recomendaciones";
    }

    // VER DETALLE DE RECOMENDACION
    @GetMapping("/detalle/{id}")
    public String verDetalleRecomendacion(@PathVariable Long id, Model model) {
        Recomendacion recomendacion = recomendacionService.buscarRecomendacionById(id);
        model.addAttribute("recomendacion", recomendacion);
        return "pages/detalleRecomendacion";
    }
}