package com.exactpromos.dto.request.UsuarioDTOs;

import com.exactpromos.Enum.PerfilEnum;
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

    private PerfilEnum perfil;
}
