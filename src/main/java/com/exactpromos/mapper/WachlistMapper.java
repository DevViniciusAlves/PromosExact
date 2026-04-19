package com.exactpromos.mapper;

import com.exactpromos.dto.request.WachlistDTOs.WachlistCreateDTO;
import com.exactpromos.dto.response.WachlistDTOs.WachlistResponseDTO;
import com.exactpromos.entity.Produto;
import com.exactpromos.entity.Usuario;
import com.exactpromos.entity.Wachlist;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class WachlistMapper {

    private final ProdutoMapper produtoMapper;

    public WachlistMapper(ProdutoMapper produtoMapper) {
        this.produtoMapper = produtoMapper;
    }

    public WachlistResponseDTO toResponse(Wachlist wachlist) {
        if (wachlist == null) {
            return null;
        }

        WachlistResponseDTO dto = new WachlistResponseDTO();
        dto.setId(wachlist.getId());
        dto.setPrecoAlerta(wachlist.getPrecoAlerta());
        dto.setAlertaAtivo(wachlist.getAtivo());
        dto.setDataCriacao(wachlist.getDataCriacao());
        dto.setPrecoAtual(wachlist.getProduto().getPrecoAtual());
        dto.setProduto(produtoMapper.toSimplificadoDTO(wachlist.getProduto()));
        return dto;
    }

    public Wachlist toEntity(WachlistCreateDTO dto, Usuario usuario, Produto produto) {
        if (dto == null) {
            return null;
        }

        Wachlist wachlist = new Wachlist();
        wachlist.setPrecoAlerta(dto.getPrecoAlerta());
        wachlist.setNotificarEstoque(dto.getNotificarEstoque());
        wachlist.setUsuario(usuario);
        wachlist.setProduto(produto);
        wachlist.setAtivo(true);
        wachlist.setDataCriacao(LocalDateTime.now());
        return wachlist;
    }
}
