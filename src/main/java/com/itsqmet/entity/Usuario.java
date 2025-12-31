package com.itsqmet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //VALIDACIONES
    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúñÑ ]{2,30}$",
            message = "El nombre debe contener solo letras y tener entre 2-30 caracteres")
    @Column(nullable = false) //CAMPO OBLIGATORIO
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúñÑ ]{2,30}$",
            message = "El apellido debe contener solo letras y tener entre 2-30 caracteres")
    @Column(nullable = false)
    private String apellido;

    @NotBlank(message = "La cédula es obligatoria")
    @Pattern(regexp = "^[0-9]{10}$",
            message = "La cédula debe tener exactamente 10 dígitos")
    @Column(nullable = false, unique = true)
    private String cedula;

    @NotBlank(message = "La dirección es obligatoria")
    @Pattern(regexp = "^[A-Za-z0-9ÁÉÍÓÚáéíóúñÑ .,#-]{5,100}$",
            message = "La dirección debe tener entre 5-100 caracteres (letras, números, espacios, .,-,#)")
    @Column(length = 500)
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^09[0-9]{8}$",
            message = "El teléfono debe tener 10 dígitos y empezar con 09")
    private String telefono;

    @Min(value = 18, message = "La edad mínima es 18 años")
    @Max(value = 100, message = "La edad máxima es 100 años")
    private Integer edad;

    //RELACION CON ACCOUNT
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    //RELACION CON TESTANSIEDAD
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TestAnsiedad> tests = new ArrayList<>();

}
