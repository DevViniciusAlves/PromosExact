package com.exactpromos.dto.response.PromocaoDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PromocaoLinkResponseDTO {

    private int total;
    private int sucessos;
    private int falhas;
    private List<PromocaoLinkResultadoDTO> resultados;
}
