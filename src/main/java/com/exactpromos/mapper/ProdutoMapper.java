package com.exactpromos.mapper;

import com.exactpromos.Enum.PlataformaEnum;
import com.exactpromos.dto.external.MercadoLivreExternal.AttributeDTO;
import com.exactpromos.dto.external.MercadoLivreExternal.MercadoLivreProdutoDTO;
import com.exactpromos.dto.request.ProdutoDTOs.ProdutoCreateDTO;
import com.exactpromos.dto.external.ShopeeExternal.ShopeeProdutoDTO;
import com.exactpromos.dto.response.ProdutoDTOs.ProdutoDetalhadoDTO;
import com.exactpromos.dto.response.ProdutoDTOs.ProdutoResponseDTO;
import com.exactpromos.dto.response.ProdutoDTOs.ProdutoSimplificadoDTO;
import com.exactpromos.entity.Produto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ProdutoMapper {

    public ProdutoResponseDTO toResponseDTO(Produto produto) {
        if (produto == null) {
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

    public ProdutoSimplificadoDTO toSimplificadoDTO(Produto produto) {
        if (produto == null) {
            return null;
        }

        ProdutoSimplificadoDTO dto = new ProdutoSimplificadoDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setUrlImagem(produto.getUrlImagem());
        dto.setPrecoAtual(produto.getPrecoAtual());
        return dto;
    }

    public ProdutoSimplificadoDTO toSimplififcadoDTO(Produto produto) {
        return toSimplificadoDTO(produto);
    }

    public ProdutoDetalhadoDTO toDetalhadoDTO(Produto produto) {
        if (produto == null) {
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
        dto.setPlataforma(produto.getPlataforma());
        dto.setEmEstoque(produto.getEmEstoque());
        return dto;
    }

    public Produto toEntity(ShopeeProdutoDTO dto) {
        if (dto == null) {
            return null;
        }

        Produto produto = new Produto();
        produto.setProdutoId(dto.getItemId());
        produto.setNome(dto.getName());
        produto.setMarca(dto.getBrand() != null ? dto.getBrand() : "Nao informada");
        produto.setDescricao(dto.getDescription() != null ? dto.getDescription() : dto.getName());
        produto.setUrlImagem(dto.getImage());
        produto.setCategoria(dto.getCategoryPath());
        produto.setPrecoAtual(converterCentavosParaReais(dto.getPrice()));
        produto.setPrecoOriginal(converterCentavosParaReais(dto.getPriceBeforeDiscount()));
        produto.setEmEstoque(dto.getStock() != null && dto.getStock() > 0);
        produto.setDescontoPercentual(calcularDescontoPercentual(produto.getPrecoOriginal(), produto.getPrecoAtual()));
        produto.setPlataforma(PlataformaEnum.SHOPEE);
        produto.setUltimaAtualizacao(LocalDateTime.now());
        return produto;
    }

    public Produto toEntity(ProdutoCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        Produto produto = new Produto();
        produto.setProdutoId(dto.getProdutoId());
        produto.setPlataforma(dto.getPlataforma());
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setUrlImagem(dto.getUrlImagem());
        produto.setCategoria(dto.getCategoria());
        produto.setMarca(dto.getMarca());
        produto.setPrecoAtual(dto.getPrecoAtual());
        produto.setPrecoOriginal(dto.getPrecoOriginal());
        produto.setEmEstoque(dto.getEmEstoque());
        produto.setDescontoPercentual(calcularDescontoPercentual(dto.getPrecoOriginal(), dto.getPrecoAtual()));
        produto.setUltimaAtualizacao(LocalDateTime.now());
        return produto;
    }

    public Produto toEntity(MercadoLivreProdutoDTO dto) {
        if (dto == null) {
            return null;
        }

        Produto produto = new Produto();
        produto.setProdutoId(dto.getId());
        produto.setNome(dto.getTitle());
        produto.setMarca(extrairMarca(dto.getAttributes()));
        produto.setDescricao(dto.getTitle());
        produto.setUrlImagem(dto.getThumbnail());
        produto.setCategoria(dto.getCategoryId());
        produto.setPrecoAtual(converterNumeroParaBigDecimal(dto.getPrice()));
        produto.setPrecoOriginal(converterNumeroParaBigDecimal(dto.getOriginalPrice()));
        produto.setEmEstoque(dto.getAvailableQuantity() != null && dto.getAvailableQuantity() > 0);
        produto.setDescontoPercentual(calcularDescontoPercentual(produto.getPrecoOriginal(), produto.getPrecoAtual()));
        produto.setPlataforma(PlataformaEnum.MERCADO_LIVRE);
        produto.setUltimaAtualizacao(LocalDateTime.now());
        return produto;
    }

    private String extrairMarca(List<AttributeDTO> atributos) {
        if (atributos == null) {
            return "Nao informada";
        }

        return atributos.stream()
                .filter(atributo -> atributo.getName() != null)
                .filter(atributo -> "brand".equalsIgnoreCase(atributo.getName()) || "marca".equalsIgnoreCase(atributo.getName()))
                .map(AttributeDTO::getValueName)
                .filter(valor -> valor != null && !valor.isBlank())
                .findFirst()
                .orElse("Nao informada");
    }

    private BigDecimal converterCentavosParaReais(Long centavos) {
        if (centavos == null) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(centavos).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal converterNumeroParaBigDecimal(Double valor) {
        if (valor == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP);
    }

    private Integer calcularDescontoPercentual(BigDecimal original, BigDecimal atual) {
        if (original == null || atual == null || original.compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }

        BigDecimal desconto = original.subtract(atual)
                .divide(original, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
        return Math.max(desconto.intValue(), 0);
    }
}
