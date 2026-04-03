package com.exactpromos.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wachlists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class Wachlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(precision = 10, scale = 2)
    private BigDecimal precoAlerta;

    @Column(nullable = false)
    private Boolean notificarEstoque = false;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    public void prePersist() {
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
        if (ativo == null) {
            ativo = true;
        }
        if (notificarEstoque == null) {
            notificarEstoque = false;
        }
    }
}
