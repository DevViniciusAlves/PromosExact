package com.exactpromos.controller;

import com.exactpromos.dto.external.MercadoLivreExternal.MercadoLivreProdutoDTO;
import com.exactpromos.dto.external.ShopeeExternal.ShopeeProdutoDTO;
import com.exactpromos.dto.response.ProdutoDTOs.ProdutoDetalhadoDTO;
import com.exactpromos.dto.response.ProdutoDTOs.ProdutoResponseDTO;
import com.exactpromos.service.ProdutoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService){
        this.produtoService = produtoService;
    }
    @GetMapping
    public List<ProdutoResponseDTO> listarProdutos(){
        return produtoService.listarProdutos();
    }
    @GetMapping("/{id}")
    public ProdutoDetalhadoDTO buscarProdutoPorId(@PathVariable Long id){
        return produtoService.buscarProdutoPorId(id);
    }
    @PostMapping("/shopee")
    public ProdutoResponseDTO salvarIntegracaoShopee(@RequestBody ShopeeProdutoDTO dto){
        return produtoService.salvarIntegracaoShopee(dto);
    }
    @PostMapping("/mercadolivre")
    public ProdutoResponseDTO salvarIntegracaoMercadoLivre(@RequestBody MercadoLivreProdutoDTO dto){
        return produtoService.salvarIntegracaoMercadoLivre(dto);

    }
}
