package com.exactpromos.mapper;

import com.exactpromos.dto.request.UsuarioDTOs.UsuarioCreateDTO;
import com.exactpromos.dto.request.UsuarioDTOs.UsuarioUpdateDTO;
import com.exactpromos.dto.response.InteresseDTOs.InteresseSimplificadoDTO;
import com.exactpromos.dto.response.UsuarioDTOs.UsuarioDetalhadoDTO;
import com.exactpromos.dto.response.UsuarioDTOs.UsuarioResponseDTO;
import com.exactpromos.dto.response.UsuarioDTOs.UsuarioSimplificadoDTO;
import com.exactpromos.entity.Usuario;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component // Componente gerenciado
public class UsuarioMapper {

    // Entity > Response simples
    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {

        // Verifica se o usuario é null ou n
        if (usuario == null) {
            return null;
        }
        // Dto novo e vazio
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        // Copia os campo da entity
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setNomeDeUsuario(usuario.getNomeDeUsuario());
        dto.setPerfil(usuario.getPerfil());
        dto.setPremium(usuario.getPremium());

        return dto;

    }

    // DTO  > Response detalhado
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
        dto.setDataCriancao(usuario.getDataCriacao());

        return dto;

        // Converter lista entity para interrese dto

        if (usuario.getInteresses() != null) {
            List<InteresseSimplicadoDTO> interreseDTO = usuario.getInteresses()
                    .stream() // tranformando em stream para processo
                    .map(interesse -> { // Para cada Interesse
                        InteresseSimplificadoDTO interessesDTO = new InteresseSimplificadoDTO();
                        interreseDTO.setId(interesse.getId());
                        interesseDTO.setCategoria(interesse.getCategoria());

                        return interreseDTO;
                    })
                    .collect(Collectors.toList()); // Volta para lista dnv

            dto.setInteresse(interessesDTOs);
        }
        // Totais calculados (conta quantos tem)
        dto.setTotalWachlist(
                usuario.getWatchlist() != null ? usuario.getWachlist().size() : 0
        );
        dto.setTotalAvaliacoes(
                usuario.getTotalAvaliacoes() != null ? usuario.getAvaliacoes().size() : 0
        );
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

    public Usuario toEntityDTO(UsuarioCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setTelegramId(dto.getTelegramId());
        usuario.setNome(dto.getNome());
        usuario.setNomeDeUsuario(dto.getNomeDeUsuario());
        usuario.setPerfil(dto.getPerfil());

        usuario.setDataCriancao(LocalDateTime.now()); // Agora
        usuario.setPremium(false); // Padrao é gratuito
        usuario.setPremiumAte(null); // Inicia sem premium


        // Relacionamentos começam vazios (serão adicionados depois)
        // usuario.setInteresses(new ArrayList<>());
        // usuario.setWatchlist(new ArrayList<>());

        return usuario;


    }

    public void updateEntity(UsuarioUpdateDTO dto, Usuario usuario) {
        if (dto == null || usuario == null) {
            return;  //  Não faz nada se for null
        }
        if (dto.getNome() != null) {
            usuario.setNome(dto.getNome());
        }
        if (dto.getPerfil() != null) {
            usuario.setPerfil(dto.getPerfil());
        }

    }

    // Converte list entity para Usuario responseDTO
    public List<UsuarioResponseDTO> toResponseDTOList(List<Usuario> usuarios) {
        if (usuarios == null) {
            return null;
        }
        return usuarios.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Converte lista de Usuario para lista de UsuarioDetalhadoDTO
    public List<UsuarioDetalhadoDTO> toDetalhadoDTOList(List<Usuario> usuarios) {

        if (usuarios == null) {
            return null;
        }

        return usuarios.stream()
                .map(this::toDetalhadoDTO)
                .collect(Collectors.toList());
    }

    public List<UsuarioSimplificadoDTO> toSimplificadoDTOList(List<Usuario> usuarios) {

        if (usuarios == null) {
            return null;
        }

        return usuarios.stream()
                .map(this::toSimplificadoDTO)
                .collect(Collectors.toList());
    }
}
