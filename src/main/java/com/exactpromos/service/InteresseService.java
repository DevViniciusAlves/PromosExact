package com.exactpromos.service;

import com.exactpromos.dto.request.InteresseDTOs.InteresseCreateDTO;
import com.exactpromos.dto.response.InteresseDTOs.InteresseResponseDTO;
import com.exactpromos.entity.Interesse;
import com.exactpromos.entity.Usuario;
import com.exactpromos.mapper.InteresseMapper;
import com.exactpromos.repository.InteresseRepository;
import com.exactpromos.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class InteresseService {

    private final InteresseRepository interesseRepository;
    private final UsuarioRepository usuarioRepository;
    private final InteresseMapper interesseMapper;

    public InteresseService(InteresseRepository interesseRepository,
                            UsuarioRepository usuarioRepository,
                            InteresseMapper interesseMapper) {
        this.interesseRepository = interesseRepository;
        this.usuarioRepository = usuarioRepository;
        this.interesseMapper = interesseMapper;
    }

    public InteresseResponseDTO criarInteresse(InteresseCreateDTO dto) {
        Usuario usuario = usuarioRepository.findFirstByOrderByIdAsc().orElse(null);
        Interesse interesse = interesseMapper.toEntity(dto, usuario);
        return interesseMapper.toResponseDTO(interesseRepository.save(interesse));
    }

    public List<InteresseResponseDTO> listarInteresses() {
        return interesseMapper.toResponseDTOList(interesseRepository.findAll());
    }

    public InteresseResponseDTO desativarInteresse(Long id) {
        Interesse interesse = interesseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Interesse nao encontrado"));
        interesse.setAtivo(false);
        return interesseMapper.toResponseDTO(interesseRepository.save(interesse));
    }
}
