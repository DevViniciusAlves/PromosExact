package com.exactpromos.entity;

import com.exactpromos.Enum.PerfilEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @Enumerated(EnumType.STRING)
    private PerfilEnum perfil;

    private LocalDateTime dataCriacao;

    private Boolean premium;

    private LocalDateTime premiumAte;

    @Column(nullable = false)
    private String senha;
}
