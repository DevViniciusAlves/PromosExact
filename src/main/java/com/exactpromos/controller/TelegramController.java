package com.exactpromos.controller;

import com.exactpromos.service.TelegramService;
import com.exactpromos.service.RateLimitService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/telegram")
public class TelegramController {

    private final TelegramService telegramService;
    private final RateLimitService rateLimitService;

    public TelegramController(TelegramService telegramService, RateLimitService rateLimitService){
        this.telegramService = telegramService;
        this.rateLimitService = rateLimitService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/teste")
    public Map<String, String> testeTelegram(HttpServletRequest request) {
        rateLimitService.validar("telegram-teste:" + request.getRemoteAddr());
        telegramService.enviarMensagem("Teste de integracao com o Telegram");
        return Map.of("status", "ok", "mensagem", "Mensagem enviada para o Telegram");
    }

}
