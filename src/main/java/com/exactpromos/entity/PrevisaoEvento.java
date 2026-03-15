package com.exactpromos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "previsaoEventos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrevisaoEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeEvento;

    @Column(nullable = false)
    private LocalDate dataEvento;

    @ElementCollection
    @CollectionTable(name = "evento_categorias")
    private List<String> categorias;

    @Column(length = 500)
    private String recomendacao;

    @Column(nullable = false)
    private Boolean ativo = true;


}
