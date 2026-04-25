package com.exactpromos.controller;

import com.exactpromos.Enum.PlataformaEnum;
import com.exactpromos.dto.external.MercadoLivreExternal.MercadoLivreProdutoDTO;
import com.exactpromos.dto.request.ProdutoDTOs.ProdutoCreateDTO;
import com.exactpromos.dto.external.ShopeeExternal.ShopeeProdutoDTO;
import com.exactpromos.dto.response.ProdutoDTOs.ProdutoDetalhadoDTO;
import com.exactpromos.dto.response.ProdutoDTOs.ProdutoResponseDTO;
import jakarta.validation.Valid;
import com.exactpromos.service.ProdutoService;
import com.exactpromos.service.LinkAfiliadoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final LinkAfiliadoService linkAfiliadoService;

    public ProdutoController(ProdutoService produtoService, LinkAfiliadoService linkAfiliadoService){
        this.produtoService = produtoService;
        this.linkAfiliadoService = linkAfiliadoService;
    }
    @GetMapping
    public List<ProdutoResponseDTO> listarProdutos(){
        return produtoService.listarProdutos();
    }
    @GetMapping("/{id}")
    public ProdutoDetalhadoDTO buscarProdutoPorId(@PathVariable Long id){
        return produtoService.buscarProdutoPorId(id);
    }
    @PostMapping
    public ProdutoResponseDTO criarProduto(@Valid @RequestBody ProdutoCreateDTO dto){
        return produtoService.criarProduto(dto);
    }
    @PostMapping("/shopee")
    public ProdutoResponseDTO salvarIntegracaoShopee(@RequestBody ShopeeProdutoDTO dto){
        return produtoService.salvarIntegracaoShopee(dto);
    }
    @PostMapping("/mercadolivre")
    public ProdutoResponseDTO salvarIntegracaoMercadoLivre(@RequestBody MercadoLivreProdutoDTO dto){
        return produtoService.salvarIntegracaoMercadoLivre(dto);

    }

    @PostMapping("/link-afiliado/testar")
    public String testarConversaoLink(@RequestBody TesteLinkAfiliadoDTO dto) {
        return linkAfiliadoService.gerarLink(
                dto.getPlataforma(),
                dto.getLinkOrigem(),
                dto.getProdutoId(),
                dto.getNome()
        );
    }

    public static class TesteLinkAfiliadoDTO {
        private PlataformaEnum plataforma;
        private String linkOrigem;
        private String produtoId;
        private String nome;

        public PlataformaEnum getPlataforma() {
            return plataforma;
        }

        public void setPlataforma(PlataformaEnum plataforma) {
            this.plataforma = plataforma;
        }

        public String getLinkOrigem() {
            return linkOrigem;
        }

        public void setLinkOrigem(String linkOrigem) {
            this.linkOrigem = linkOrigem;
        }

        public String getProdutoId() {
            return produtoId;
        }

        public void setProdutoId(String produtoId) {
            this.produtoId = produtoId;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }
    }
}
