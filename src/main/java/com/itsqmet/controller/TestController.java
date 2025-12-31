package com.itsqmet.controller;

import com.itsqmet.entity.TestAnsiedad;
import com.itsqmet.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    // LEER TODOS LOS TESTS (PARA ADMIN)
    @GetMapping
    public String listaTests(Model model) {
        var tests = testService.mostrarTests();
        model.addAttribute("tests", tests);
        return "pages/testList";
    }

    // VER TESTS SIN RECOMENDACION (PARA ESPECIALISTA)
    @GetMapping("/sin-recomendacion")
    public String testsSinRecomendacion(Model model) {
        var tests = testService.obtenerTestsSinRecomendacion();
        model.addAttribute("tests", tests);
        return "pages/testList";
    }

    // FORMULARIO PARA CREAR TEST (VACIO)
    @GetMapping("/form")
    public String crearTest(Model model) {
        model.addAttribute("test", new TestAnsiedad());
        return "pages/testForm";
    }

    // GUARDAR TEST
    @PostMapping("/guardar")
    public String guardarTest(@ModelAttribute TestAnsiedad test) {
        testService.guardarTest(test);
        return "redirect:/test";
    }

    // FORMULARIO PARA EDITAR TEST
    @GetMapping("/editar/{id}")
    public String editarTest(@PathVariable Long id, Model model) {
        TestAnsiedad test = testService.buscarTestById(id);
        model.addAttribute("test", test);
        return "pages/testForm";
    }

    // ELIMINAR TEST
    @GetMapping("/eliminar/{id}")
    public String eliminarTest(@PathVariable Long id) {
        testService.eliminarTest(id);
        return "redirect:/test";
    }

    // VER DETALLE DE UN TEST
    @GetMapping("/detalle/{id}")
    public String verDetalleTest(@PathVariable Long id, Model model) {
        TestAnsiedad test = testService.buscarTestById(id);
        model.addAttribute("test", test);
        return "pages/resultadoTest";
    }
}