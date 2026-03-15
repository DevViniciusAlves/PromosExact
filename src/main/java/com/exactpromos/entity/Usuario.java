package com.exactpromos.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String telegramId;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String nomeDeUsuario;

    @Column(nullable = false)
    private perfilEnum perfil;

    private LocalDateTime dataCriacao;

    private Boolean premium;

    private LocalDateTime premiumAte;

    @Column(nullable = false)
    private String senha;

}
