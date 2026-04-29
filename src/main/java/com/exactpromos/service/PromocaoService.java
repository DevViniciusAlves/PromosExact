package com.exactpromos.service;

import com.exactpromos.Enum.PlataformaEnum;
import com.exactpromos.Enum.PromocaoEnum;
import com.exactpromos.Enum.TipoNotificacao;
import com.exactpromos.dto.request.PromocaoDTOs.PromocaoCreateDTO;
import com.exactpromos.dto.request.PromocaoDTOs.PromocaoLinkRequestDTO;
import com.exactpromos.dto.request.PromocaoDTOs.PromocaoLoteItemDTO;
import com.exactpromos.dto.request.PromocaoDTOs.PromocaoLoteRequestDTO;
import com.exactpromos.dto.request.PromocaoDTOs.PromocaoFilterDTO;
import com.exactpromos.dto.response.PromocaoDTOs.PromocaoLinkResponseDTO;
import com.exactpromos.dto.response.PromocaoDTOs.PromocaoLinkResultadoDTO;
import com.exactpromos.dto.response.PromocaoDTOs.PromocaoLoteResponseDTO;
import com.exactpromos.dto.response.PromocaoDTOs.PromocaoLoteResultadoDTO;
import com.exactpromos.dto.response.PromocaoDTOs.PromocaoResponseDTO;
import com.exactpromos.entity.NotificacaoLog;
import com.exactpromos.entity.Promocao;
import com.exactpromos.entity.Produto;
import com.exactpromos.mapper.PromocaoMapper;
import com.exactpromos.mapper.ProdutoMapper;
import com.exactpromos.repository.NotificacaoLogRepository;
import com.exactpromos.repository.ProdutoRepository;
import com.exactpromos.repository.PromocaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.List;

@Service
public class PromocaoService {

    private static final Logger log = LoggerFactory.getLogger(PromocaoService.class);
    private static final List<PromocaoEnum> STATUS_QUE_BLOQUEIAM_DUPLICIDADE = List.of(
            PromocaoEnum.RASCUNHO,
            PromocaoEnum.PRONTA_PARA_ENVIO,
            PromocaoEnum.PUBLICADA
    );

    private final PromocaoRepository promocaoRepository;
    private final ProdutoRepository produtoRepository;
    private final PromocaoMapper promocaoMapper;
    private final ProdutoMapper produtoMapper;
    private final TelegramService telegramService;
    private final NotificacaoLogRepository notificacaoLogRepository;
    private final LinkAfiliadoService linkAfiliadoService;
    private final LinkPreviewService linkPreviewService;

    public PromocaoService(PromocaoRepository promocaoRepository,
                           ProdutoRepository produtoRepository,
                           PromocaoMapper promocaoMapper,
                           ProdutoMapper produtoMapper,
                           TelegramService telegramService,
                           NotificacaoLogRepository notificacaoLogRepository,
                           LinkAfiliadoService linkAfiliadoService,
                           LinkPreviewService linkPreviewService) {
        this.promocaoRepository = promocaoRepository;
        this.produtoRepository = produtoRepository;
        this.promocaoMapper = promocaoMapper;
        this.produtoMapper = produtoMapper;
        this.telegramService = telegramService;
        this.notificacaoLogRepository = notificacaoLogRepository;
        this.linkAfiliadoService = linkAfiliadoService;
        this.linkPreviewService = linkPreviewService;
    }

    public PromocaoResponseDTO criarPromocao(PromocaoCreateDTO dto) {
        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto nao encontrado"));

        if (produto.getLinkAfiliado() == null || produto.getLinkAfiliado().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Produto sem link afiliado");
        }
        validarProdutoSemPromocaoAtiva(produto);

        Promocao promocao = new Promocao();
        promocao.setProduto(produto);
        promocao.setPrecoPromocional(dto.getPrecoPromocional());
        promocao.setDescontoPercentual(dto.getDescontoPercentual());
        promocao.setLinkAfiliado(produto.getLinkAfiliado());
        promocao.setDataInicio(dto.getDataInicio() != null ? dto.getDataInicio() : LocalDateTime.now());
        promocao.setDataFim(dto.getDataFim());
        promocao.setStatus(PromocaoEnum.RASCUNHO);
        promocao.setVisualizacoes(0);
        promocao.setCliques(0);

        Promocao promocaoSalva = promocaoRepository.save(promocao);

        publicarPromocao(promocaoSalva);

        Promocao promocaoFinal = promocaoRepository.save(promocaoSalva);
        return promocaoMapper.toResponseDTO(promocaoFinal);
    }

    private void validarPromocaoParaDivulgar(Promocao promocao) {
        if (promocao.getLinkAfiliado() == null || promocao.getLinkAfiliado().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Link de afiliado obrigatorio");
        }

        if (promocao.getPrecoPromocional() == null || promocao.getPrecoPromocional().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Preco promocional deve ser maior que zero");
        }

        if (promocao.getDescontoPercentual() == null || promocao.getDescontoPercentual() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Desconto percentual deve ser maior que zero");
        }

        if (promocao.getDataFim() != null
                && promocao.getDataInicio() != null
                && promocao.getDataFim().isBefore(promocao.getDataInicio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data fim nao pode ser anterior a data inicio");
        }
    }

    private void marcarComoProntaParaEnvio(Promocao promocao) {
        promocao.setStatus(PromocaoEnum.PRONTA_PARA_ENVIO);
    }

    private void publicarPromocao(Promocao promocao) {
        validarDuplicidadePromocao(promocao);
        validarPromocaoParaDivulgar(promocao);
        marcarComoProntaParaEnvio(promocao);

        String mensagem = montarMensagemPromocao(promocao);

        try {
            String imagem = promocao.getProduto().getUrlImagem();
            if (imagem != null && !imagem.isBlank()) {
                try {
                    telegramService.enviarFotoComLegenda(imagem, mensagem);
                } catch (Exception fotoException) {
                    telegramService.enviarMensagem(mensagem);
                }
            } else {
                telegramService.enviarMensagem(mensagem);
            }
            promocao.setStatus(PromocaoEnum.PUBLICADA);
            salvarLogNotificacao(promocao, mensagem, true);
        } catch (Exception e) {
            promocao.setStatus(PromocaoEnum.ERRO_NO_ENVIO);
            log.error("Falha ao publicar promocao id={}", promocao.getId(), e);
            salvarLogNotificacao(promocao, mensagem, false);
        }
    }


    private String montarMensagemPromocao(Promocao promocao) {
        PlataformaEnum plataforma = promocao.getProduto().getPlataforma();
        BigDecimal precoOriginal = promocao.getProduto().getPrecoOriginal();
        BigDecimal precoPromocional = promocao.getPrecoPromocional();

        if (precoOriginal == null || precoPromocional == null || precoOriginal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Preços invalidos para montar promocao");
        }

        BigDecimal diferenca = precoOriginal.subtract(precoPromocional);
        BigDecimal descontoPercentualCalculado = diferenca
                .multiply(BigDecimal.valueOf(100))
                .divide(precoOriginal, 0, RoundingMode.HALF_UP);

        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String precoOriginalFormatado = formatoMoeda.format(precoOriginal);
        String precoPromocionalFormatado = formatoMoeda.format(precoPromocional);
        String nomeSeguro = HtmlUtils.htmlEscape(promocao.getProduto().getNome());
        String linkSeguro = HtmlUtils.htmlEscape(promocao.getLinkAfiliado());

        String titulo;
        if (plataforma == PlataformaEnum.SHOPEE) {
            titulo = "Oferta na Shopee \uD83D\uDD25";
        } else if (plataforma == PlataformaEnum.MERCADO_LIVRE) {
            titulo = "Oferta no Mercado Livre \uD83D\uDD25";
        } else {
            titulo = "Oferta em destaque \uD83D\uDD25";
        }

        return String.format(
                "%s%n%n%s%n%n De <s>%s</s> por \uD83D\uDD25 %s%n(%s%% OFF)%n%nCorre que essa oferta pode acabar a qualquer momento.%n%nLink: %s",
                titulo,
                nomeSeguro,
                precoOriginalFormatado,
                precoPromocionalFormatado,
                descontoPercentualCalculado,
                linkSeguro
        );
    }
    private void validarDuplicidadePromocao(Promocao promocao){
        if (promocao.getStatus() == PromocaoEnum.PUBLICADA
                || promocao.getStatus() == PromocaoEnum.ERRO_NO_ENVIO
                || notificacaoLogRepository.existsByPromocaoAndEnviadaTrue(promocao)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "PromoÃ§Ã£o ja enviada");
        }
    }

    public PromocaoResponseDTO buscarPromocaoPorId(Long id) {
        Promocao promocao = promocaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PromoÃ§Ã£o nÃ£o encontrada"));
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

    public PromocaoLoteResponseDTO processarPromocoesEmLote(PromocaoLoteRequestDTO request) {
        List<PromocaoLoteResultadoDTO> resultados = request.getItens().stream()
                .map(this::processarItemLote)
                .toList();

        int sucessos = (int) resultados.stream().filter(PromocaoLoteResultadoDTO::isSucesso).count();
        int falhas = resultados.size() - sucessos;
        return new PromocaoLoteResponseDTO(resultados.size(), sucessos, falhas, resultados);
    }

    public PromocaoLinkResponseDTO processarPromocoesPorLinks(PromocaoLinkRequestDTO request) {
        List<PromocaoLinkResultadoDTO> resultados = request.getLinks().stream()
                .map(this::processarLink)
                .toList();

        int sucessos = (int) resultados.stream().filter(PromocaoLinkResultadoDTO::isSucesso).count();
        int falhas = resultados.size() - sucessos;
        return new PromocaoLinkResponseDTO(resultados.size(), sucessos, falhas, resultados);
    }

    private PromocaoLinkResultadoDTO processarLink(String url) {
        try {
            LinkPreviewService.PreviewResultado preview = linkPreviewService.analisar(url);
            if (preview.titulo() == null || preview.preco() == null) {
                return new PromocaoLinkResultadoDTO(url, false, "Nao foi possivel processar o link informado", null);
            }

            String nome = preview.titulo().length() > 150 ? preview.titulo().substring(0, 150).trim() : preview.titulo();
            String descricao = preview.titulo().length() >= 15 ? preview.titulo() : (preview.titulo() + " - oferta automatica");
            BigDecimal precoPromocional = preview.preco();
            BigDecimal precoOriginal = resolverPrecoOriginal(preview, precoPromocional);
            Integer descontoPercentual = calcularDesconto(precoOriginal, precoPromocional);

            Produto produto = new Produto();
            String produtoIdCurto = gerarProdutoIdCanonico(url, preview.itemId());
            produto.setProdutoId(produtoIdCurto);
            produto.setPlataforma(preview.plataforma());
            produto.setNome(nome);
            produto.setDescricao(descricao);
            produto.setCategoria("Nao informada");
            produto.setMarca("Nao informada");
            produto.setPrecoAtual(precoPromocional);
            produto.setPrecoOriginal(precoOriginal);
            produto.setDescontoPercentual(descontoPercentual);
            produto.setEmEstoque(true);
            produto.setUrlImagem(linkAfiliadoService.limitarParaBanco(preview.imagem()));
            produto.setUltimaAtualizacao(LocalDateTime.now());
            produto.setLinkAfiliado(linkAfiliadoService.limitarParaBanco(
                    linkAfiliadoService.gerarLink(preview.plataforma(), url, url, nome)
            ));

            Produto existente = produtoRepository.findByProdutoIdAndPlataforma(produtoIdCurto, preview.plataforma()).orElse(null);
            if (existente != null) {
                produto.setId(existente.getId());
            }

            Produto salvo = produtoRepository.save(produto);
            if (produtoJaTemPromocaoAtiva(salvo)) {
                return new PromocaoLinkResultadoDTO(url, false, "Produto ja possui promocao cadastrada", null);
            }

            PromocaoCreateDTO promocaoCreateDTO = new PromocaoCreateDTO();
            promocaoCreateDTO.setProdutoId(salvo.getId());
            promocaoCreateDTO.setPrecoPromocional(precoPromocional);
            promocaoCreateDTO.setDescontoPercentual(descontoPercentual);

            PromocaoResponseDTO promocao = criarPromocao(promocaoCreateDTO);
            return new PromocaoLinkResultadoDTO(url, true, "Processado com sucesso", promocao);
        } catch (Exception e) {
            log.error("Falha ao processar link {}", url, e);
            return new PromocaoLinkResultadoDTO(url, false, "Nao foi possivel processar o link informado", null);
        }
    }

    private BigDecimal resolverPrecoOriginal(LinkPreviewService.PreviewResultado preview, BigDecimal precoPromocional) {
        if (preview.precoOriginal() != null && preview.precoOriginal().compareTo(precoPromocional) > 0) {
            return preview.precoOriginal();
        }

        Integer descontoPercentual = preview.descontoPercentual();
        if (descontoPercentual != null && descontoPercentual > 0 && descontoPercentual < 100) {
            BigDecimal fator = BigDecimal.ONE.subtract(
                    BigDecimal.valueOf(descontoPercentual).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
            );
            if (fator.compareTo(BigDecimal.ZERO) > 0) {
                return precoPromocional.divide(fator, 2, RoundingMode.HALF_UP);
            }
        }

        return precoPromocional;
    }

    private PromocaoLoteResultadoDTO processarItemLote(PromocaoLoteItemDTO item) {
        try {
            Produto produto = new Produto();
            produto.setProdutoId(item.getProdutoId());
            produto.setPlataforma(item.getPlataforma());
            produto.setNome(item.getNome());
            produto.setDescricao(item.getDescricao());
            produto.setCategoria(item.getCategoria());
            produto.setMarca(item.getMarca());
            produto.setPrecoAtual(item.getPrecoAtual());
            produto.setPrecoOriginal(item.getPrecoAtual());
            produto.setDescontoPercentual(calcularDesconto(item.getPrecoAtual(), item.getPrecoPromocional()));
            produto.setEmEstoque(true);
            produto.setUrlImagem(item.getUrlImagem());
            produto.setUltimaAtualizacao(LocalDateTime.now());
            produto.setLinkAfiliado(linkAfiliadoService.gerarLink(item.getPlataforma(), item.getLinkOrigem(), item.getProdutoId(), item.getNome()));

            Produto existente = produtoRepository.findByProdutoIdAndPlataforma(item.getProdutoId(), item.getPlataforma()).orElse(null);
            if (existente != null) {
                produto.setId(existente.getId());
            }

            Produto salvo = produtoRepository.save(produto);
            if (produtoJaTemPromocaoAtiva(salvo)) {
                return new PromocaoLoteResultadoDTO(item.getProdutoId(), false, "Produto ja possui promocao cadastrada", null);
            }

            PromocaoCreateDTO promocaoCreateDTO = new PromocaoCreateDTO();
            promocaoCreateDTO.setProdutoId(salvo.getId());
            promocaoCreateDTO.setPrecoPromocional(item.getPrecoPromocional());
            promocaoCreateDTO.setDescontoPercentual(salvo.getDescontoPercentual());

            PromocaoResponseDTO promocao = criarPromocao(promocaoCreateDTO);
            return new PromocaoLoteResultadoDTO(item.getProdutoId(), true, "Processado com sucesso", promocao);
        } catch (Exception e) {
            log.error("Falha ao processar item em lote produtoId={}", item.getProdutoId(), e);
            return new PromocaoLoteResultadoDTO(item.getProdutoId(), false, "Nao foi possivel processar o item informado", null);
        }
    }

    private String gerarProdutoIdCanonico(String url, String itemId) {
        if (itemId != null && !itemId.isBlank()) {
            return itemId.trim();
        }

        String urlNormalizada = normalizarUrl(url);
        if (urlNormalizada == null || urlNormalizada.isBlank()) {
            return "link-sem-url";
        }

        return "link-" + sha256Curto(urlNormalizada);
    }

    private String normalizarUrl(String url) {
        if (url == null || url.isBlank()) {
            return null;
        }

        try {
            java.net.URI uri = java.net.URI.create(url);
            StringBuilder builder = new StringBuilder();
            if (uri.getScheme() != null) {
                builder.append(uri.getScheme().toLowerCase(Locale.ROOT)).append("://");
            }
            if (uri.getHost() != null) {
                builder.append(uri.getHost().toLowerCase(Locale.ROOT));
            }
            if (uri.getPath() != null) {
                builder.append(uri.getPath());
            }
            return builder.toString();
        } catch (Exception e) {
            String limpa = url.trim().toLowerCase(Locale.ROOT);
            int query = limpa.indexOf('?');
            if (query >= 0) {
                limpa = limpa.substring(0, query);
            }
            int fragment = limpa.indexOf('#');
            if (fragment >= 0) {
                limpa = limpa.substring(0, fragment);
            }
            return limpa;
        }
    }

    private String sha256Curto(String valor) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(valor.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (int i = 0; i < 12 && i < hash.length; i++) {
                hex.append(String.format("%02x", hash[i]));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            return Integer.toHexString(valor.hashCode());
        }
    }

    private Integer calcularDesconto(BigDecimal original, BigDecimal promocional) {
        if (original == null || promocional == null || original.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }

        BigDecimal diferenca = original.subtract(promocional);
        if (diferenca.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }

        BigDecimal desconto = diferenca
                .divide(original, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        int valorArredondado = desconto.setScale(0, RoundingMode.HALF_UP).intValue();
        return Math.max(valorArredondado, 1);
    }

    private void validarProdutoSemPromocaoAtiva(Produto produto) {
        if (produtoJaTemPromocaoAtiva(produto)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Produto ja possui promocao cadastrada");
        }
    }

    private boolean produtoJaTemPromocaoAtiva(Produto produto) {
        return produto != null
                && produto.getId() != null
                && promocaoRepository.existsByProdutoAndStatusIn(produto, STATUS_QUE_BLOQUEIAM_DUPLICIDADE);
    }

    private void salvarLogNotificacao(Promocao promocao, String mensagem, boolean enviada){
        NotificacaoLog notificacaoLog = new NotificacaoLog();
        notificacaoLog.setPromocao(promocao);
        notificacaoLog.setMensagem(truncarMensagemLog(mensagem));
        notificacaoLog.setTipo(TipoNotificacao.NOVA_PROMOCAO);
        notificacaoLog.setEnviada(enviada);
        notificacaoLog.setDataEnvio(LocalDateTime.now());

        notificacaoLogRepository.save(notificacaoLog);

    }

    private String truncarMensagemLog(String mensagem) {
        if (mensagem == null) {
            return null;
        }

        String limpa = mensagem.strip();
        if (limpa.length() <= 300) {
            return limpa;
        }

        return limpa.substring(0, 300);
    }
}
