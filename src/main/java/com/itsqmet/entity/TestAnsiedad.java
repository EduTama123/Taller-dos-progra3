package com.itsqmet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "test_ansiedades")
public class TestAnsiedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // CADA PREGUNTA DEBE ESTAR ENTRE 0 Y 6
    @NotNull(message = "La pregunta 1 es obligatoria")
    @Min(value = 0, message = "El valor mínimo es 0")
    @Max(value = 6, message = "El valor máximo es 6")
    @Column(nullable = false)
    private Integer pregunta1;

    @NotNull(message = "La pregunta 2 es obligatoria")
    @Min(value = 0, message = "El valor mínimo es 0")
    @Max(value = 6, message = "El valor máximo es 6")
    @Column(nullable = false)
    private Integer pregunta2;

    @NotNull(message = "La pregunta 3 es obligatoria")
    @Min(value = 0, message = "El valor mínimo es 0")
    @Max(value = 6, message = "El valor máximo es 6")
    @Column(nullable = false)
    private Integer pregunta3;

    @NotNull(message = "La pregunta 4 es obligatoria")
    @Min(value = 0, message = "El valor mínimo es 0")
    @Max(value = 6, message = "El valor máximo es 6")
    @Column(nullable = false)
    private Integer pregunta4;

    @NotNull(message = "La pregunta 5 es obligatoria")
    @Min(value = 0, message = "El valor mínimo es 0")
    @Max(value = 6, message = "El valor máximo es 6")
    @Column(nullable = false)
    private Integer pregunta5;

    @NotNull(message = "La pregunta 6 es obligatoria")
    @Min(value = 0, message = "El valor mínimo es 0")
    @Max(value = 6, message = "El valor máximo es 6")
    @Column(nullable = false)
    private Integer pregunta6;

    @NotNull(message = "La pregunta 7 es obligatoria")
    @Min(value = 0, message = "El valor mínimo es 0")
    @Max(value = 6, message = "El valor máximo es 6")
    @Column(nullable = false)
    private Integer pregunta7;

    @NotNull(message = "La pregunta 8 es obligatoria")
    @Min(value = 0, message = "El valor mínimo es 0")
    @Max(value = 6, message = "El valor máximo es 6")
    @Column(nullable = false)
    private Integer pregunta8;

    @NotNull(message = "La pregunta 9 es obligatoria")
    @Min(value = 0, message = "El valor mínimo es 0")
    @Max(value = 6, message = "El valor máximo es 6")
    @Column(nullable = false)
    private Integer pregunta9;

    @NotNull(message = "La pregunta 10 es obligatoria")
    @Min(value = 0, message = "El valor mínimo es 0")
    @Max(value = 6, message = "El valor máximo es 6")
    @Column(nullable = false)
    private Integer pregunta10;

    @Column(nullable = false)
    private Integer puntuacionTotal;

    @Column(nullable = false, length = 50)
    private String nivelAnsiedad;

    @Column(nullable = false)
    private LocalDateTime fechaRealizacion;

    // RELACIONES
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // La recomendación puede ser nula al principio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recomendacion_id")
    private Recomendacion recomendacion;

    // Constructor para facilitar la creación
    public TestAnsiedad(Usuario usuario, Integer p1, Integer p2, Integer p3, Integer p4, Integer p5,
                        Integer p6, Integer p7, Integer p8, Integer p9, Integer p10) {
        this.usuario = usuario;
        this.pregunta1 = p1;
        this.pregunta2 = p2;
        this.pregunta3 = p3;
        this.pregunta4 = p4;
        this.pregunta5 = p5;
        this.pregunta6 = p6;
        this.pregunta7 = p7;
        this.pregunta8 = p8;
        this.pregunta9 = p9;
        this.pregunta10 = p10;
        this.fechaRealizacion = LocalDateTime.now();
        calcularResultados();
    }

    // MÉTODO PARA CALCULAR RESULTADOS
    public void calcularResultados() {
        this.puntuacionTotal = pregunta1 + pregunta2 + pregunta3 + pregunta4 + pregunta5 +
                pregunta6 + pregunta7 + pregunta8 + pregunta9 + pregunta10;

        // Asignar nivel según puntuación
        if (this.puntuacionTotal <= 10) {
            this.nivelAnsiedad = "Mínima";
        } else if (this.puntuacionTotal <= 20) {
            this.nivelAnsiedad = "Leve";
        } else if (this.puntuacionTotal <= 30) {
            this.nivelAnsiedad = "Moderada";
        } else {
            this.nivelAnsiedad = "Severa";
        }

        // Si no tiene fecha, asignar la actual
        if (this.fechaRealizacion == null) {
            this.fechaRealizacion = LocalDateTime.now();
        }
    }
}