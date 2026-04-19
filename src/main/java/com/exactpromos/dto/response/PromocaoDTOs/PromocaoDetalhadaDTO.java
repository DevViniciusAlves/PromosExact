package com.exactpromos.dto.response.PromocaoDTOs;

import com.exactpromos.dto.response.AvaliacaoDTOs.AvaliacaoResponseDTO;
import com.exactpromos.dto.response.ProdutoDTOs.ProdutoDetalhadoDTO;
import com.exactpromos.dto.response.StatusPromocaoDTOs.StatusPromocaoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PromocaoDetalhadaDTO {

    private Long id;

    private ProdutoDetalhadoDTO produto;

    private BigDecimal precoPromocional;

    private Integer descontoPercentual;

    private BigDecimal cashback;

    private String linkAfiliado;

    private StatusPromocaoDTO status;

    private List<AvaliacaoResponseDTO> avaliacoes;

    private Integer totalVisualizacoes;

    private Integer totalCliques;

    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;
}
