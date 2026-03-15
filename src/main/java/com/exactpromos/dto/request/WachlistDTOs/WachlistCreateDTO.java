package com.exactpromos.dto.request.WachlistDTOs;


import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class WachlistCreateDTO {

    @NotNull
    private Long produtoId;

    @NotNull
    @Positive
    private BigDecimal precoAlerta;

    private Boolean notificarEstoque = true;
}
