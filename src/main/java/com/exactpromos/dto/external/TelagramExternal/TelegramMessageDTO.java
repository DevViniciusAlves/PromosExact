package com.exactpromos.dto.external.TelagramExternal;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TelegramMessageDTO {

    private String chatId;

    private String text;

    private String parseMode;

    private List<InlineButtonDTO> buttons; // Botões clicaveis

    private String photoUrl; // URl da imagem q vai ser enviada;
}
