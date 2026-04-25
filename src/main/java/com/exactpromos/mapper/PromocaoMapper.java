package com.exactpromos.mapper;

import com.exactpromos.dto.response.PromocaoDTOs.PromocaoResponseDTO;
import com.exactpromos.entity.Promocao;
import org.springframework.stereotype.Component;

@Component
public class PromocaoMapper {

    private final ProdutoMapper produtoMapper;

    public PromocaoMapper(ProdutoMapper produtoMapper) {
        this.produtoMapper = produtoMapper;
    }

    public PromocaoResponseDTO toResponseDTO(Promocao promocao) {
        if (promocao == null) {
            return null;
        }

        PromocaoResponseDTO dto = new PromocaoResponseDTO();
        dto.setId(promocao.getId());
        dto.setProduto(produtoMapper.toResponseDTO(promocao.getProduto()));
        dto.setPrecoPromocional(promocao.getPrecoPromocional());
        dto.setDescontoPercentual(promocao.getDescontoPercentual());
        dto.setScoreQualidade(null);
        dto.setReputacaoComunitaria(null);
        dto.setStatus(promocao.getStatus());
        dto.setLinkAfiliado(promocao.getLinkAfiliado());
        dto.setDataFim(promocao.getDataFim());
        return dto;
    }
}
