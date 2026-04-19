package com.exactpromos.service;

import com.exactpromos.dto.request.PromocaoDTOs.PromocaoCreateDTO;
import com.exactpromos.dto.request.PromocaoDTOs.PromocaoFilterDTO;
import com.exactpromos.dto.response.PromocaoDTOs.PromocaoResponseDTO;
import com.exactpromos.entity.Promocao;
import com.exactpromos.entity.Produto;
import com.exactpromos.mapper.PromocaoMapper;
import com.exactpromos.repository.ProdutoRepository;
import com.exactpromos.repository.PromocaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PromocaoService {

    private final PromocaoRepository promocaoRepository;
    private final ProdutoRepository produtoRepository;
    private final PromocaoMapper promocaoMapper;

    public PromocaoService(PromocaoRepository promocaoRepository,
                           ProdutoRepository produtoRepository,
                           PromocaoMapper promocaoMapper) {
        this.promocaoRepository = promocaoRepository;
        this.produtoRepository = produtoRepository;
        this.promocaoMapper = promocaoMapper;
    }

    public PromocaoResponseDTO criarPromocao(PromocaoCreateDTO dto) {
        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto nao encontrado"));

        Promocao promocao = new Promocao();
        promocao.setProduto(produto);
        promocao.setPrecoPromocional(dto.getPrecoPromocional());
        promocao.setDescontoPercentual(dto.getDescontoPercentual());
        promocao.setCashback(dto.getCashback());
        promocao.setLinkAfiliado(dto.getLinkAfiliado());
        promocao.setDataInicio(dto.getDataInicio() != null ? dto.getDataInicio() : LocalDateTime.now());
        promocao.setDataFim(dto.getDataFim());
        promocao.setAtiva(true);
        promocao.setVisualizacoes(0);
        promocao.setCliques(0);
        return promocaoMapper.toResponseDTO(promocaoRepository.save(promocao));
    }

    public PromocaoResponseDTO buscarPromocaoPorId(Long id) {
        Promocao promocao = promocaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Promocao nao encontrada"));
        return promocaoMapper.toResponseDTO(promocao);
    }

    public List<PromocaoResponseDTO> filtrarPromocoes(PromocaoFilterDTO dto) {
        return promocaoRepository.findAll().stream()
                .filter(promocao -> dto.getCategorias() == null || dto.getCategorias().isEmpty() || dto.getCategorias().contains(promocao.getProduto().getCategoria()))
                .filter(promocao -> dto.getPrecoMinimo() == null || promocao.getPrecoPromocional().compareTo(dto.getPrecoMinimo()) >= 0)
                .filter(promocao -> dto.getPrecoMaximo() == null || promocao.getPrecoPromocional().compareTo(dto.getPrecoMaximo()) <= 0)
                .filter(promocao -> dto.getPlataforma() == null || dto.getPlataforma().isEmpty() || dto.getPlataforma().contains(promocao.getProduto().getPlataforma()))
                .map(promocaoMapper::toResponseDTO)
                .toList();
    }
}
