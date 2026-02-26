package com.exactpromos.dto.response.AvaliacaoDTOs;


import com.exactpromos.dto.response.UsuarioDTOs.UsuarioSimplificadoDTO;
import com.exactpromos.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoResponseDTO {

    private Long id;

    private UsuarioSimplificadoDTO usuario;

    private Boolean valeApena;

    private Integer nota;

    private String comentario;

    private Integer likes;

    private LocalDateTime dataCriacao;

}
