package com.exactpromos.controller;

import com.exactpromos.dto.request.UsuarioDTOs.UsuarioCreateDTO;
import com.exactpromos.dto.request.UsuarioDTOs.UsuarioUpdateDTO;
import com.exactpromos.dto.response.UsuarioDTOs.UsuarioDetalhadoDTO;
import com.exactpromos.dto.response.UsuarioDTOs.UsuarioResponseDTO;
import com.exactpromos.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")

public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioResponseDTO> listarUsuarios(){
        return usuarioService.listarTodos();
    }
    @GetMapping("/{id}")
    public UsuarioDetalhadoDTO buscarPorId(@PathVariable Long id){
        return usuarioService.buscarPorId(id);
    }
    @PostMapping
    public UsuarioResponseDTO criarUsuario(@RequestBody UsuarioCreateDTO dto){
        return usuarioService.criarUsuario(dto);
    }
    @PatchMapping("/atualizar/{id}")
    public UsuarioResponseDTO atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioUpdateDTO dto){
        return usuarioService.atualizarUsuario(id, dto);
    }
    @PatchMapping("/desativar/{id}")
    public UsuarioResponseDTO desativarUsuario(@PathVariable Long id){
        return usuarioService.desativarUsuario(id);
    }
}
