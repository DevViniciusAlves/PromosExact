package com.exactpromos.dto.response.NotificacaoLogDTOs;

import com.exactpromos.Enum.TipoNotificacao;
import com.exactpromos.dto.response.PromocaoDTOs.PromocaoSimplificadaDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificacaoLogDTO {

    private Long id;

    private TipoNotificacao tipo;

    private String mensagem;

    private Boolean lida;

    private LocalDateTime dataEnvio;

    private PromocaoSimplificadaDTO promocao;
}
