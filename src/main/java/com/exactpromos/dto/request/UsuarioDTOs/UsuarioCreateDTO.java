package com.exactpromos.dto.request.UsuarioDTOs;

import jakarta.persistence.Column;
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

public class UsuarioCreateDTO {

    @NotBlank(message = "Obrigatório")
    @Column(unique = true)
    private String telagramId;

    @NotBlank
    @Size(min = 3,max = 100, message = "Nome é obrigatorio")
    private String nome;

    @NotBlank
    @Column(unique = true)
    private String nomeDeUsuario;

    @NotNull
    private perfilEnum perfil;
}
