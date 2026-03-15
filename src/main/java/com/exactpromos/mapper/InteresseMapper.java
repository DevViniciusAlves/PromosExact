package com.exactpromos.mapper;

import com.exactpromos.dto.request.InteresseDTOs.InteresseCreateDTO;
import com.exactpromos.dto.response.InteresseDTOs.InteresseResponseDTO;
import com.exactpromos.dto.response.InteresseDTOs.InteresseSimplificadoDTO;
import com.exactpromos.entity.Interesse;
import com.exactpromos.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InteresseMapper {

    public InteresseResponseDTO toResponseDTO(Interesse interesse) {

        if (interesse == null) {
            return null;
        }
        InteresseResponseDTO dto = new InteresseResponseDTO();
        dto.setId(interesse.getId());
        dto.setCategoria(interesse.getCategoria());
        dto.setPrecoMinimo(interesse.getPrecoMinimo());
        dto.setPrecoMaximo(interesse.getPrecoMaximo());
        dto.setMarcasFavoritas(interesse.getMarcasFavoritas());
        dto.setAtivo(interesse.getAtivo());

        return dto;

    }

    public InteresseSimplificadoDTO toSimplificadoDTO(Interesse interesse) {

        if (interesse == null) {
            return null;
        }
        InteresseSimplificadoDTO dto = new InteresseSimplificadoDTO();
        dto.setId(interesse.getId());
        dto.setCategoria(interesse.getCategoria());

        return dto;
    }

    public Interesse toEntity(InteresseCreateDTO dto, Usuario usuario) {
        if (dto == null) {
            return null;
        }
        Interesse interesse = new Interesse();
        interesse.setCategoria(dto.getCategoria());
        interesse.setPrecoMinimo(dto.getPrecoMinimo());
        interesse.setPrecoMaximo(dto.getPrecoMaximo());
        interesse.setMarcasFavoritas(dto.getMarcasFavoritas());
        interesse.setPalavrasChaves(dto.getPalavrasChaves());

        interesse.setUsuario(usuario);
        interesse.setAtivo(true);

        return interesse;
    }
    public List<InteresseResponseDTO> toResponseDTOList(List<Interesse> interesses){
        if(interesses == null){
            return null;
        }
        return interesses.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
    public List<InteresseSimplificadoDTO> toSimplificadoDTOList(List<Interesse> interesses){
        if(interesses == null){
            return null;
        }
        return interesses.stream()
                .map(this::toSimplificadoDTO)
                .collect(Collectors.toList());
    }
}
