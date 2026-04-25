package com.exactpromos.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RateLimitService {

    private static final int LIMITE_POR_JANELA = 20;
    private static final Duration JANELA = Duration.ofMinutes(1);

    private final Map<String, Janela> janelas = new ConcurrentHashMap<>();

    public void validar(String chave) {
        Instant agora = Instant.now();
        Janela janela = janelas.compute(chave, (k, atual) -> {
            if (atual == null || atual.expiraEm.isBefore(agora)) {
                return new Janela(agora.plus(JANELA), new AtomicInteger(1));
            }

            atual.contador.incrementAndGet();
            return atual;
        });

        if (janela.contador.get() > LIMITE_POR_JANELA) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Muitas requisicoes, tente novamente em instantes");
        }
    }

    private record Janela(Instant expiraEm, AtomicInteger contador) {}
}
