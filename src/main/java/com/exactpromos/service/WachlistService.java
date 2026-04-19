package com.exactpromos.service;

import com.exactpromos.dto.request.WachlistDTOs.WachlistCreateDTO;
import com.exactpromos.dto.response.WachlistDTOs.WachlistResponseDTO;
import com.exactpromos.entity.Produto;
import com.exactpromos.entity.Usuario;
import com.exactpromos.entity.Wachlist;
import com.exactpromos.mapper.WachlistMapper;
import com.exactpromos.repository.ProdutoRepository;
import com.exactpromos.repository.UsuarioRepository;
import com.exactpromos.repository.WachlistRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class WachlistService {

    private final WachlistRepository wachlistRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;
    private final WachlistMapper wachlistMapper;

    public WachlistService(WachlistRepository wachlistRepository,
                           ProdutoRepository produtoRepository,
                           UsuarioRepository usuarioRepository,
                           WachlistMapper wachlistMapper) {
        this.wachlistRepository = wachlistRepository;
        this.produtoRepository = produtoRepository;
        this.usuarioRepository = usuarioRepository;
        this.wachlistMapper = wachlistMapper;
    }

    public WachlistResponseDTO adicionarItemNaWachlist(WachlistCreateDTO dto) {
        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto nao encontrado"));
        Usuario usuario = usuarioRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cadastre um usuario antes da watchlist"));

        Wachlist wachlist = wachlistMapper.toEntity(dto, usuario, produto);
        return wachlistMapper.toResponse(wachlistRepository.save(wachlist));
    }

    public List<WachlistResponseDTO> listarWachlistDeUsuario(Long id) {
        return wachlistRepository.findByUsuarioId(id).stream()
                .map(wachlistMapper::toResponse)
                .toList();
    }

    public WachlistResponseDTO desativarItem(Long id) {
        Wachlist wachlist = wachlistRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item de watchlist nao encontrado"));
        wachlist.setAtivo(false);
        return wachlistMapper.toResponse(wachlistRepository.save(wachlist));
    }
}
