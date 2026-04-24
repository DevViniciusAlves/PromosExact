package com.exactpromos.controller;

import com.exactpromos.dto.external.TelagramExternal.TelegramMessageDTO;
import com.exactpromos.service.TelegramService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/telegram")
public class TelegramController {

    private final TelegramService telegramService;

    public TelegramController(TelegramService telegramService){
        this.telegramService = telegramService;
    }
    @PostMapping("/teste")
    public Map<String, String> testeTelegram() {
        telegramService.enviarMensagem("Teste de integracao com o Telegram");
        return Map.of("status", "ok", "mensagem", "Mensagem enviada para o Telegram");
    }

}
