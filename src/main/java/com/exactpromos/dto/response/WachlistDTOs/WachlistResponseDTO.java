package com.exactpromos.dto.response.WachlistDTOs;

import com.exactpromos.dto.response.ProdutoDTOs.ProdutoSimplificadoDTO;
import com.exactpromos.entity.Produto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WachlistResponseDTO {

    private Long id;

    private ProdutoSimplificadoDTO produto;

    private BigDecimal precoAlerta;

    private BigDecimal precoAtual;

    private Boolean alertaAtivo;

    private LocalDateTime dataCriacao;


}
