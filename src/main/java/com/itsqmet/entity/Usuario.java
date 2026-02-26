package com.itsqmet.entity;

import com.itsqmet.roles.Rol;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Datos personales
    @NotBlank(message = "Nombre es obligatorio")
    @Size(min = 2, max = 60, message = "El nombre debe tener entre 2 y 60 caracteres")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "Teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener 10 dígitos")
    @Column(nullable = false)
    private String telefono;

    // Datos de cuenta
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 30, message = "El nombre de usuario debe tener entre 3 y 30 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Solo letras, números y guión bajo")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Correo electrónico es obligatorio")
    @Email(message = "Debe ser un correo electrónico válido")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @Enumerated(EnumType.STRING)
    private Rol rol;


}