package com.exactpromos.dto.request.UsuarioDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UsuarioUpdateDTO {

    @NotBlank
    @Size(min = 3,max = 100, message = "Nome é obrigatorio")
    private String nome;

    private perfilEnum perfil;
}
