package com.exactpromos.dto.response.ProdutoDTOs;

import com.exactpromos.Enum.PlataformaEnum;
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

    private String linkAfiliado;

    private String urlImagem;

    private PlataformaEnum plataforma;

    private Boolean emEstoque;
}
