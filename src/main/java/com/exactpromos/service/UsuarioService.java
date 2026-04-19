package com.exactpromos.service;

import com.exactpromos.dto.request.UsuarioDTOs.UsuarioCreateDTO;
import com.exactpromos.dto.request.UsuarioDTOs.UsuarioUpdateDTO;
import com.exactpromos.dto.response.UsuarioDTOs.UsuarioDetalhadoDTO;
import com.exactpromos.dto.response.UsuarioDTOs.UsuarioResponseDTO;
import com.exactpromos.entity.Usuario;
import com.exactpromos.mapper.UsuarioMapper;
import com.exactpromos.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioMapper.toResponseDTOList(usuarioRepository.findAll());
    }

    public UsuarioDetalhadoDTO buscarPorId(Long id) {
        return usuarioMapper.toDetalhadoDTO(buscarEntidade(id));
    }

    public UsuarioResponseDTO criarUsuario(UsuarioCreateDTO dto) {
        Usuario usuario = usuarioMapper.toEntity(dto);
        return usuarioMapper.toResponseDTO(usuarioRepository.save(usuario));
    }

    public UsuarioResponseDTO atualizarUsuario(Long id, UsuarioUpdateDTO dto) {
        Usuario usuario = buscarEntidade(id);
        usuarioMapper.updateEntity(dto, usuario);
        return usuarioMapper.toResponseDTO(usuarioRepository.save(usuario));
    }

    public UsuarioResponseDTO desativarUsuario(Long id) {
        Usuario usuario = buscarEntidade(id);
        usuario.setPremium(false);
        usuario.setPremiumAte(null);
        return usuarioMapper.toResponseDTO(usuarioRepository.save(usuario));
    }

    private Usuario buscarEntidade(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado"));
    }
}
