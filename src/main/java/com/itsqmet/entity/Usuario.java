package com.itsqmet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "Apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "Cédula es obligatoria")
    @Column(unique = true)
    private String cedula;

    @NotNull(message = "Edad es obligatoria")
    private Integer edad;

    @NotBlank(message = "Teléfono es obligatorio")
    private String telefono;

    @NotBlank(message = "Dirección es obligatoria")
    private String direccion;

    // Relación 1:1 con Account
    @OneToOne
    @JoinColumn(name = "account_id", unique = true)
    private Account account;
}