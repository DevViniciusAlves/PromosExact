package com.exactpromos.service;

import com.exactpromos.dto.external.MercadoLivreExternal.MercadoLivreProdutoDTO;
import com.exactpromos.dto.request.ProdutoDTOs.ProdutoCreateDTO;
import com.exactpromos.dto.external.ShopeeExternal.ShopeeProdutoDTO;
import com.exactpromos.dto.response.ProdutoDTOs.ProdutoDetalhadoDTO;
import com.exactpromos.dto.response.ProdutoDTOs.ProdutoResponseDTO;
import com.exactpromos.entity.Produto;
import com.exactpromos.mapper.ProdutoMapper;
import com.exactpromos.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    public ProdutoService(ProdutoRepository produtoRepository, ProdutoMapper produtoMapper) {
        this.produtoRepository = produtoRepository;
        this.produtoMapper = produtoMapper;
    }

    public List<ProdutoResponseDTO> listarProdutos() {
        return produtoRepository.findAll().stream()
                .map(produtoMapper::toResponseDTO)
                .toList();
    }

    public ProdutoDetalhadoDTO buscarProdutoPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto nao encontrado"));
        return produtoMapper.toDetalhadoDTO(produto);
    }

    public ProdutoResponseDTO criarProduto(ProdutoCreateDTO dto) {
        Produto produto = produtoMapper.toEntity(dto);
        return produtoMapper.toResponseDTO(salvarOuAtualizar(produto));
    }

    public ProdutoResponseDTO salvarIntegracaoShopee(ShopeeProdutoDTO dto) {
        Produto produto = produtoMapper.toEntity(dto);
        return produtoMapper.toResponseDTO(salvarOuAtualizar(produto));
    }

    public ProdutoResponseDTO salvarIntegracaoMercadoLivre(MercadoLivreProdutoDTO dto) {
        Produto produto = produtoMapper.toEntity(dto);
        return produtoMapper.toResponseDTO(salvarOuAtualizar(produto));
    }

    private Produto salvarOuAtualizar(Produto produto) {
        Produto existente = produtoRepository.findByProdutoIdAndPlataforma(produto.getProdutoId(), produto.getPlataforma())
                .orElse(null);
        if (existente != null) {
            produto.setId(existente.getId());
        }
        return produtoRepository.save(produto);
    }
}
