package com.exactpromos.service;

import com.exactpromos.config.TelegramProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
public class TelegramService {

    private static final String TELEGRAM_API_URL = "https://api.telegram.org";

    private final TelegramProperties telegramProperties;
    private final RestClient restClient;

    public TelegramService(TelegramProperties telegramProperties, RestClient.Builder restClientBuilder) {
        this.telegramProperties = telegramProperties;
        this.restClient = restClientBuilder.baseUrl(TELEGRAM_API_URL).build();
    }

    public void enviarMensagem(String texto) {
        validarConfiguracao();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("chat_id", telegramProperties.getChatId());
        body.put("text", texto);
        body.put("parse_mode", "HTML");

        restClient.post()
                .uri("/bot{token}/sendMessage", telegramProperties.getBotToken())
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }

    public void enviarFotoComLegenda(String fotoUrl, String legenda) {
        validarConfiguracao();

        if (fotoUrl == null || fotoUrl.isBlank()) {
            enviarMensagem(legenda);
            return;
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("chat_id", telegramProperties.getChatId());
        body.put("photo", fotoUrl);
        body.put("caption", legenda);
        body.put("parse_mode", "HTML");

        restClient.post()
                .uri("/bot{token}/sendPhoto", telegramProperties.getBotToken())
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }

    private void validarConfiguracao() {
        if (telegramProperties.getBotToken() == null || telegramProperties.getBotToken().isBlank()) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Token do Telegram nao configurado");
        }

        if (telegramProperties.getChatId() == null || telegramProperties.getChatId().isBlank()) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Chat ID do Telegram nao configurado");
        }
    }
}
