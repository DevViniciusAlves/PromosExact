package com.exactpromos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "statusPromocoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class StatusPromocao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "promocao_id", unique = true)
    private Promocao promocao;

    @Column(nullable = false)
    private Double scoreQualidade;

    @Column(nullable = false)
    private Double reputacaoComunitaria;

    @Column(nullable = false)
    private Integer votosPositivos;

    @Column(nullable = false)
    private Integer votosNegativos;

    @Column(length = 1000)
    private String analiseIA;

    @Column(nullable = false)
    private Boolean recomendada = false;

    @Column(nullable = false)
    private LocalDateTime dataAnalise;


}
