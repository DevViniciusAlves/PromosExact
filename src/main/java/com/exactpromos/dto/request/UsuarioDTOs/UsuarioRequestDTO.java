package com.exactpromos.dto.request.UsuarioDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDTO {

    @NotBlank(message = "Obrigatorio")
    private String telegramId;

    @NotBlank@Size(min = 3,max = 100, message = "Nome é obrigatorio")
    private String nome;

    private perfilEnum perfil;


}
