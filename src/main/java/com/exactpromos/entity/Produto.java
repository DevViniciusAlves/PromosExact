package com.exactpromos.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)  //  importante pra salva como "SHOPEE" no banco
    private PlataformaEnum plataforma;

    @Column(nullable = false) @Size(min = 3, max = 150)
    private String nome;


    @Column(nullable = false) @Size(min = 15, max = 150)
    private String descricao;

    private String urlImagem;

    @Column(nullable = false)
    private String categoria;


    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private BigDecimal precoAtual;

    @Column(nullable = false)
    private BigDecimal precoOriginal;

    @Column(nullable = false)
    private Integer descontoPercentual;

    @Column(nullable = false)
    private Boolean emEstoque;

    private LocalDateTime ultimaAtualizacao;

}
