package com.exactpromos.entity;

import com.exactpromos.Enum.PromocaoEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "promocoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class Promocao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Produto produto;

    @Column(nullable = false)
    private BigDecimal precoPromocional;

    @Column(nullable = false)
    private Integer descontoPercentual;

    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;

    @NotBlank
    private String linkAfiliado;

    @Column(nullable = false)
    private BigDecimal cashback;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PromocaoEnum status;

    @Column(nullable = false)
    private Integer visualizacoes;

    @Column(nullable = false)
    private Integer cliques;
}
