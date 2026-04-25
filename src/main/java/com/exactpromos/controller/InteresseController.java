package com.exactpromos.controller;

import com.exactpromos.dto.request.InteresseDTOs.InteresseCreateDTO;
import com.exactpromos.dto.response.InteresseDTOs.InteresseResponseDTO;
import com.exactpromos.service.InteresseService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interesses")
public class InteresseController {

    private final InteresseService interesseService;

    public InteresseController(InteresseService interesseService){
        this.interesseService = interesseService;
    }
    @PostMapping
    public InteresseResponseDTO criarInteresse(@Valid @RequestBody InteresseCreateDTO dto){
        return interesseService.criarInteresse(dto);
    }
    @GetMapping
    public List<InteresseResponseDTO> listarInteresses(){
        return interesseService.listarInteresses();
    }
    @PatchMapping("/{id}")
    public InteresseResponseDTO desativarInteresse(@PathVariable Long id){
        return interesseService.desativarInteresse(id);
    }

}
