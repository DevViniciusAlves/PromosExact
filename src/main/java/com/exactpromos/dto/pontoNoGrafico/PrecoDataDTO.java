package com.exactpromos.dto.pontoNoGrafico;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrecoDataDTO {

    private BigDecimal preco;

    private LocalDateTime data;
}
