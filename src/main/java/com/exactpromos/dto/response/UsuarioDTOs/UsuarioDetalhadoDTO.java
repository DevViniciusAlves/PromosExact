package com.exactpromos.dto.response.UsuarioDTOs;

import com.exactpromos.Enum.PerfilEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDetalhadoDTO {

    private Long id;

    private String nomeDeUsuario;

    private PerfilEnum perfil;

    private Boolean premium;

    private LocalDateTime premiumAte;

    private LocalDateTime dataCriacao;
}
