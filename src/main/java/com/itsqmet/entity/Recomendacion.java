package com.itsqmet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recomendacion")
public class Recomendacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //VALIDACIONES
    @NotBlank(message = "El nivel de ansiedad es obligatorio")
    @Size(max = 50, message = "El nivel no puede exceder 50 caracteres")
    @Column(nullable = false, unique = true, length = 50)
    private String nivelAnsiedad;

    @Size(max = 1000, message = "La recomendación no puede exceder 1000 caracteres")
    @Column(length = 1000)
    private String recomendacion1;

    @Size(max = 1000, message = "La recomendación no puede exceder 1000 caracteres")
    @Column(length = 1000)
    private String recomendacion2;

    @Size(max = 1000, message = "La recomendación no puede exceder 1000 caracteres")
    @Column(length = 1000)
    private String recomendacion3;

    @Size(max = 1000, message = "La recomendación no puede exceder 1000 caracteres")
    @Column(length = 1000)
    private String recomendacion4;

    @Size(max = 1000, message = "La recomendación no puede exceder 1000 caracteres")
    @Column(length = 1000)
    private String recomendacion5;

    //RELACIONES
    @OneToMany(mappedBy = "recomendacion", fetch = FetchType.LAZY)
    private List<TestAnsiedad> tests = new ArrayList<>();

    // METODO PERSONALIZADO
    public List<String> getRecomendacionesList() {
        List<String> recomendaciones = new ArrayList<>();
        if (recomendacion1 != null && !recomendacion1.trim().isEmpty()) {
            recomendaciones.add(recomendacion1);
        }
        if (recomendacion2 != null && !recomendacion2.trim().isEmpty()) {
            recomendaciones.add(recomendacion2);
        }
        if (recomendacion3 != null && !recomendacion3.trim().isEmpty()) {
            recomendaciones.add(recomendacion3);
        }
        if (recomendacion4 != null && !recomendacion4.trim().isEmpty()) {
            recomendaciones.add(recomendacion4);
        }
        if (recomendacion5 != null && !recomendacion5.trim().isEmpty()) {
            recomendaciones.add(recomendacion5);
        }
        return recomendaciones;
    }
}