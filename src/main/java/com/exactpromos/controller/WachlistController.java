package com.exactpromos.controller;

import com.exactpromos.dto.request.WachlistDTOs.WachlistCreateDTO;
import com.exactpromos.dto.response.WachlistDTOs.WachlistResponseDTO;
import com.exactpromos.service.WachlistService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wachlists")
public class WachlistController {

    private final WachlistService wachlistService;

   public WachlistController(WachlistService wachlistService){
        this.wachlistService = wachlistService;
    }
   @PostMapping
   public WachlistResponseDTO adicionarItemNaWachlist(@Valid @RequestBody WachlistCreateDTO dto){
        return wachlistService.adicionarItemNaWachlist(dto);

   }
   @GetMapping("/{id}")
    public List<WachlistResponseDTO> listarWachlistDeUsuario(@PathVariable Long id){
        return wachlistService.listarWachlistDeUsuario(id);

   }
   @PatchMapping("/{id}")
    public WachlistResponseDTO desativarItem(@PathVariable Long id){
        return wachlistService.desativarItem(id);
   }

}
