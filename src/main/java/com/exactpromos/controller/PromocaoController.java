package com.exactpromos.controller;

import com.exactpromos.dto.request.PromocaoDTOs.PromocaoCreateDTO;
import com.exactpromos.dto.request.PromocaoDTOs.PromocaoFilterDTO;
import com.exactpromos.dto.response.PromocaoDTOs.PromocaoResponseDTO;
import com.exactpromos.service.PromocaoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promocoes")
public class PromocaoController {

    private final PromocaoService promocaoService;

    public PromocaoController(PromocaoService promocaoService){
        this.promocaoService = promocaoService;
    }
    @PostMapping
    public PromocaoResponseDTO criarPromocao(@RequestBody PromocaoCreateDTO dto){
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
}
