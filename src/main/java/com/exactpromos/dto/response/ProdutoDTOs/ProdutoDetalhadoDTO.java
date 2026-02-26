package com.exactpromos.dto.response.ProdutoDTOs;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ProdutoDetalhadoDTO {

    private Long id;

    private String descricao;

    private String categoria;

    private String marca;

    private BigDecimal precoAtual;

    private BigDecimal precoOriginal;

    private String urlImagem;

    private PlataformaEnum plataforma;

    private Boolean emEstoque;

}




