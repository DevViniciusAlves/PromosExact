package com.exactpromos.mapper;


import com.exactpromos.dto.request.WachlistDTOs.WachlistCreateDTO;
import com.exactpromos.dto.response.ProdutoDTOs.ProdutoSimplificadoDTO;
import com.exactpromos.dto.response.WachlistDTOs.WachlistResponseDTO;
import com.exactpromos.entity.Produto;
import com.exactpromos.entity.Usuario;
import com.exactpromos.entity.Wachlist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class WachlistMapper {

    @Autowired
    private ProdutoMapper produtoMapper;

    public WachlistResponseDTO toResponse(Wachlist wachlist) {
        if (wachlist == null) {
            return null;
        }
        WachlistResponseDTO dto = new WachlistResponseDTO();
        dto.setId(wachlist.getId());
        dto.setPrecoAlerta(wachlist.getPrecoAlerta());
        dto.setAlertaAtivo(wachlist.getAtivo());
        dto.setDataCriacao(wachlist.getDataCriacao());

        BigDecimal precoAtual = wachlist.getProduto().getPrecoAtual();
        dto.setPrecoAtual(precoAtual);
        ProdutoSimplificadoDTO produtoDTO = produtoMapper.toSimplificadoDTO(wachlist.getProduto());
        dto.setProduto(produtoDTO);

        return dto;

    }

    public Wachlist toEntity(WachlistCreateDTO dto, Usuario usuario, Produto produto) {

        if (dto == null) {
            return null;

        }
        // Copiar campos do DTO (usando getters!)
        Wachlist wachlist = new Wachlist();
        wachlist.setPrecoAlerta(dto.getPrecoAlerta());
        wachlist.setNotificarEstoque(dto.getNotificarEstoque());
        // Setar relacionamentos
        wachlist.setUsuario(usuario);
        wachlist.setProduto(produto);
        // Valor padrao
        wachlist.setAtivo(true);
        wachlist.setDataCriacao(LocalDateTime.now());

        return wachlist;

    }
}
