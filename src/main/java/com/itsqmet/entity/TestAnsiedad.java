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
    //VALIDACIONES
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

    //RELACIONES
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recomendacion_id")
    private Recomendacion recomendacion;

    // METODO PARA CALCULAR RESULTADOS
    public void calcularResultados() {
        this.puntuacionTotal = pregunta1 + pregunta2 + pregunta3 + pregunta4 + pregunta5 +
                pregunta6 + pregunta7 + pregunta8 + pregunta9 + pregunta10;

        if (this.puntuacionTotal <= 15) {
            this.nivelAnsiedad = "Mínima";
        } else if (this.puntuacionTotal <= 30) {
            this.nivelAnsiedad = "Leve";
        } else if (this.puntuacionTotal <= 45) {
            this.nivelAnsiedad = "Moderada";
        } else if (this.puntuacionTotal <= 60) {
            this.nivelAnsiedad = "Grave";
        } else {
            this.nivelAnsiedad = "Extrema";
        }

        if (this.fechaRealizacion == null) {
            this.fechaRealizacion = LocalDateTime.now();
        }
    }
}