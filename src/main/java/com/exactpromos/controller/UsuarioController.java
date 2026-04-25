package com.exactpromos.controller;

import com.exactpromos.dto.request.UsuarioDTOs.UsuarioCreateDTO;
import com.exactpromos.dto.request.UsuarioDTOs.UsuarioUpdateDTO;
import com.exactpromos.dto.response.UsuarioDTOs.UsuarioDetalhadoDTO;
import com.exactpromos.dto.response.UsuarioDTOs.UsuarioResponseDTO;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import com.exactpromos.service.UsuarioService;
import com.exactpromos.service.RateLimitService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")

public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RateLimitService rateLimitService;

    public UsuarioController(UsuarioService usuarioService, RateLimitService rateLimitService) {
        this.usuarioService = usuarioService;
        this.rateLimitService = rateLimitService;
    }

    @GetMapping
    public List<UsuarioResponseDTO> listarUsuarios(HttpServletRequest request){
        rateLimitService.validar("usuarios-lista:" + request.getRemoteAddr());
        return usuarioService.listarTodos().stream()
                .map(usuario -> new UsuarioResponseDTO(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getNomeDeUsuario(),
                        usuario.getPerfil(),
                        usuario.getPremium()
                ))
                .toList();
    }
    @GetMapping("/{id}")
    public UsuarioDetalhadoDTO buscarPorId(@PathVariable Long id, HttpServletRequest request){
        rateLimitService.validar("usuarios-detalhe:" + request.getRemoteAddr());
        return usuarioService.buscarPorId(id);
    }
    @PostMapping
    public UsuarioResponseDTO criarUsuario(@Valid @RequestBody UsuarioCreateDTO dto, HttpServletRequest request){
        rateLimitService.validar("usuarios-criar:" + request.getRemoteAddr());
        return usuarioService.criarUsuario(dto);
    }
    @PatchMapping("/atualizar/{id}")
    public UsuarioResponseDTO atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateDTO dto, HttpServletRequest request){
        rateLimitService.validar("usuarios-atualizar:" + request.getRemoteAddr());
        return usuarioService.atualizarUsuario(id, dto);
    }
    @PatchMapping("/desativar/{id}")
    public UsuarioResponseDTO desativarUsuario(@PathVariable Long id, HttpServletRequest request){
        rateLimitService.validar("usuarios-desativar:" + request.getRemoteAddr());
        return usuarioService.desativarUsuario(id);
    }
}
