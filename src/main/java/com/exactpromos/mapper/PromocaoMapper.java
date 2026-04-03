package com.exactpromos.mapper;


import com.exactpromos.dto.response.PromocaoDTOs.PromocaoResponseDTO;
import com.exactpromos.entity.Promocao;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PromocaoMapper {
    public PromocaoResponseDTO toResponseDTO(Promocao promocao){

        if (promocao == null){
            return null;
        }
        PromocaoResponseDTO dto = new PromocaoResponseDTO();
        dto.setId(promocao.getId());
        dto.setPrecoPromocional(promocao.getPrecoPromocional());
        dto.setCashback(promocao.getCashback());
        dto.setScoreQualidade(promocao.getScoreQualidade());
        dto.setReputacaoComunitaria(promocao.getReputacaoComunitaria());
        dto.setLinkAfiliado(promocao.getLinkAfiliado());
        promocao.setDataFim(LocalDateTime.now());

        return dto;

    }

}
