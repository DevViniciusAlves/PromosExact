package com.exactpromos.dto.response.UsuarioDTOs;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

public class UsuarioResponseDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3,max = 100, message = "Nome é obrigatorio")
    private String nome;

    @NotBlank
    @Size(min = 3,max = 100, message = "Nome de usuario é obrigatorio")
    private String nomeDeUsuario;

    private perfilEnum perfil;

    private Boolean premium;

}
