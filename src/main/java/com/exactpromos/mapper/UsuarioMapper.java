package com.exactpromos.mapper;

import com.exactpromos.dto.request.UsuarioDTOs.UsuarioCreateDTO;
import com.exactpromos.dto.request.UsuarioDTOs.UsuarioUpdateDTO;
import com.exactpromos.dto.response.UsuarioDTOs.UsuarioDetalhadoDTO;
import com.exactpromos.dto.response.UsuarioDTOs.UsuarioResponseDTO;
import com.exactpromos.dto.response.UsuarioDTOs.UsuarioSimplificadoDTO;
import com.exactpromos.entity.Usuario;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioMapper {

    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setNomeDeUsuario(usuario.getNomeDeUsuario());
        dto.setPerfil(usuario.getPerfil());
        dto.setPremium(usuario.getPremium());
        return dto;
    }

    public UsuarioDetalhadoDTO toDetalhadoDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioDetalhadoDTO dto = new UsuarioDetalhadoDTO();
        dto.setId(usuario.getId());
        dto.setNomeDeUsuario(usuario.getNomeDeUsuario());
        dto.setPerfil(usuario.getPerfil());
        dto.setPremium(usuario.getPremium());
        dto.setPremiumAte(usuario.getPremiumAte());
        dto.setDataCriacao(usuario.getDataCriacao());
        return dto;
    }

    public UsuarioSimplificadoDTO toSimplificadoDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioSimplificadoDTO dto = new UsuarioSimplificadoDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setNomeDeUsuario(usuario.getNomeDeUsuario());
        return dto;
    }

    public Usuario toEntity(UsuarioCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setTelegramId(dto.getTelegramId());
        usuario.setNome(dto.getNome());
        usuario.setNomeDeUsuario(dto.getNomeDeUsuario());
        usuario.setPerfil(dto.getPerfil());
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setPremium(false);
        usuario.setPremiumAte(null);
        usuario.setSenha("sem-senha");
        return usuario;
    }

    public Usuario toEntityDTO(UsuarioCreateDTO dto) {
        return toEntity(dto);
    }

    public void updateEntity(UsuarioUpdateDTO dto, Usuario usuario) {
        if (dto == null || usuario == null) {
            return;
        }
        if (dto.getPerfil() != null) {
            usuario.setPerfil(dto.getPerfil());
        }
    }

    public List<UsuarioResponseDTO> toResponseDTOList(List<Usuario> usuarios) {
        if (usuarios == null) {
            return null;
        }
        return usuarios.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public List<UsuarioDetalhadoDTO> toDetalhadoDTOList(List<Usuario> usuarios) {
        if (usuarios == null) {
            return null;
        }
        return usuarios.stream().map(this::toDetalhadoDTO).collect(Collectors.toList());
    }

    public List<UsuarioSimplificadoDTO> toSimplificadoDTOList(List<Usuario> usuarios) {
        if (usuarios == null) {
            return null;
        }
        return usuarios.stream().map(this::toSimplificadoDTO).collect(Collectors.toList());
    }
}
