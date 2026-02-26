package com.exactpromos.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "interesses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Interesse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false)
    private String categoria;

    @Column(precision = 10, scale = 2) // Ex: 3000.00
    private BigDecimal precoMinimo;

    @Column(precision = 10, scale = 2)
    private BigDecimal precoMaximo;

    @ElementCollection
    @CollectionTable(name = "interesse_marcas")
    private List<String> marcasFavoritas;

    @ElementCollection
    @CollectionTable(name = "interesses_palavras")
    private List<String> palavrasChaves;

    @Column(nullable = false)
    private Boolean ativo = true;



}
