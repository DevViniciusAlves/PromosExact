package com.exactpromos.controller;

import com.exactpromos.dto.request.PromocaoDTOs.PromocaoCreateDTO;
import com.exactpromos.dto.request.PromocaoDTOs.PromocaoFilterDTO;
import com.exactpromos.dto.request.PromocaoDTOs.PromocaoLinkRequestDTO;
import com.exactpromos.dto.request.PromocaoDTOs.PromocaoLoteRequestDTO;
import com.exactpromos.dto.response.PromocaoDTOs.PromocaoLinkResponseDTO;
import com.exactpromos.dto.response.PromocaoDTOs.PromocaoLoteResponseDTO;
import com.exactpromos.dto.response.PromocaoDTOs.PromocaoResponseDTO;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import com.exactpromos.service.PromocaoService;
import com.exactpromos.service.RateLimitService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promocoes")
public class PromocaoController {

    private final PromocaoService promocaoService;
    private final RateLimitService rateLimitService;

    public PromocaoController(PromocaoService promocaoService, RateLimitService rateLimitService){
        this.promocaoService = promocaoService;
        this.rateLimitService = rateLimitService;
    }
    @PostMapping
    public PromocaoResponseDTO criarPromocao(@Valid @RequestBody PromocaoCreateDTO dto){
        return promocaoService.criarPromocao(dto);
    }
    @GetMapping("/{id}")
    public PromocaoResponseDTO buscarPromocaoPorId(@PathVariable Long id){
        return promocaoService.buscarPromocaoPorId(id);
    }
    @GetMapping("/filtrar")
    public List<PromocaoResponseDTO> filtrarPromocoes(PromocaoFilterDTO dto){
        return promocaoService.filtrarPromocoes(dto);
    }

    @PostMapping("/lote")
    public PromocaoLoteResponseDTO processarPromocoesEmLote(@Valid @RequestBody PromocaoLoteRequestDTO dto, HttpServletRequest request) {
        rateLimitService.validar(chaveRateLimit(request, "promocoes-lote"));
        return promocaoService.processarPromocoesEmLote(dto);
    }

    @PostMapping("/links")
    public PromocaoLinkResponseDTO processarPromocoesPorLinks(@Valid @RequestBody PromocaoLinkRequestDTO dto, HttpServletRequest request) {
        rateLimitService.validar(chaveRateLimit(request, "promocoes-links"));
        return promocaoService.processarPromocoesPorLinks(dto);
    }

    private String chaveRateLimit(HttpServletRequest request, String rota) {
        return rota + ":" + request.getRemoteAddr();
    }
}
