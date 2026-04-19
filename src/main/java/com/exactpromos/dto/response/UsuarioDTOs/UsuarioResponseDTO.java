package com.exactpromos.dto.response.UsuarioDTOs;

import com.exactpromos.Enum.PerfilEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    private Long id;

    private String nome;

    private String nomeDeUsuario;

    private PerfilEnum perfil;

    private Boolean premium;
}
