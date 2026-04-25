package com.exactpromos.dto.response.PromocaoDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PromocaoLinkResultadoDTO {

    private String url;
    private boolean sucesso;
    private String mensagem;
    private PromocaoResponseDTO promocao;
}
