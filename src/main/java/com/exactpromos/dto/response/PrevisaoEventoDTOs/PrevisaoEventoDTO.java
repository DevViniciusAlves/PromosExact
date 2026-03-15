package com.exactpromos.dto.response.PrevisaoEventoDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrevisaoEventoDTO {

    private Long id;

    private String nomeEvento;

    private LocalDate dataEvento;

    private Integer diasRestantes;

    private String recomendacao;

    private List<String> categorias;
}
