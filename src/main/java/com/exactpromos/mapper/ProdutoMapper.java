package com.exactpromos.mapper;


import com.exactpromos.Enum.PlataformaEnum;
import com.exactpromos.dto.external.ShopeeExternal.ShopeeProdutoDTO;
import com.exactpromos.dto.response.ProdutoDTOs.ProdutoDetalhadoDTO;
import com.exactpromos.dto.response.ProdutoDTOs.ProdutoResponseDTO;
import com.exactpromos.dto.response.ProdutoDTOs.ProdutoSimplificadoDTO;
import com.exactpromos.entity.Produto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Component
public class ProdutoMapper {

    public ProdutoResponseDTO toResponseDTO(Produto produto){
        if(produto == null){
            return null;
        }
        ProdutoResponseDTO dto = new ProdutoResponseDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setCategoria(produto.getCategoria());
        dto.setMarca(produto.getMarca());
        dto.setPrecoAtual(produto.getPrecoAtual());
        dto.setDescontoPercentual(produto.getDescontoPercentual());
        dto.setUrlImagem(produto.getUrlImagem());
        dto.setPlataforma(produto.getPlataforma());

        return dto;
    }
    public ProdutoSimplificadoDTO toSimplififcadoDTO(Produto produto){
        if(produto == null){
            return null;
        }
        ProdutoSimplificadoDTO dto = new ProdutoSimplificadoDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setUrlImagem(produto.getUrlImagem());
        dto.setPrecoAtual(produto.getPrecoAtual());

        return dto;
    }
    public ProdutoDetalhadoDTO toDetalhadoDTO(Produto produto){
        if (produto == null){
            return null;
        }
        ProdutoDetalhadoDTO dto = new ProdutoDetalhadoDTO();
        dto.setId(produto.getId());
        dto.setDescricao(produto.getDescricao());
        dto.setCategoria(produto.getCategoria());
        dto.setMarca(produto.getMarca());
        dto.setPrecoAtual(produto.getPrecoAtual());
        dto.setPrecoOriginal(produto.getPrecoOriginal());
        dto.setUrlImagem(produto.getUrlImagem());
        dto.setEmEstoque(produto.getEmEstoque());;

        return dto;
    }
    public Produto toEntity(ShopeeProdutoDTO dto){
        if (dto == null){
            return null;
        }
        Produto produto = new Produto();
        produto.setProdutoId(dto.getItemId());
        produto.setNome(dto.getName());
        produto.setMarca(dto.getBrand());
        produto.setDescricao(dto.getDescription());
        produto.setUrlImagem(dto.getImage());
        produto.setCategoria(dto.getCategoryPath());

        // Conversões

        BigDecimal precoAtual = converterCentavosParaReais(dto.getPrice());
        produto.setPrecoAtual(precoAtual);
        BigDecimal precoOriginal = converterCentavosParaReais(dto.getPriceBeforeDiscount());
        produto.setPrecoOriginal(precoOriginal);
        Boolean emEstoque = dto.getStock() != null && dto.getStock() > 0;
        produto.setEmEstoque(emEstoque);

        // Desconto
        Integer desconto = calcularDescontoPercentual(precoOriginal, precoAtual);
        produto.setDescontoPercentual(desconto);


        produto.setPlataforma(PlataformaEnum.SHOPEE);
        produto.setUltimaAtualizacao(LocalDateTime.now());

        return produto;


    }

    private BigDecimal  converterCentavosParaReais(Long centavos){
        if (centavos == null){
            return BigDecimal.ZERO;
        }
        return new BigDecimal(centavos)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    private Integer calcularDescontoPercentual(BigDecimal original, BigDecimal atual) {
        if (original == null || atual == null || original.compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }

        BigDecimal desconto = original.subtract(atual)
                .divide(original, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));

        return desconto.intValue();
    }
}
