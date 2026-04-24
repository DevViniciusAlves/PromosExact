package com.exactpromos.service;

import com.exactpromos.Enum.PlataformaEnum;
import com.exactpromos.Enum.PromocaoEnum;
import com.exactpromos.Enum.TipoNotificacao;
import com.exactpromos.dto.request.PromocaoDTOs.PromocaoCreateDTO;
import com.exactpromos.dto.request.PromocaoDTOs.PromocaoFilterDTO;
import com.exactpromos.dto.response.PromocaoDTOs.PromocaoResponseDTO;
import com.exactpromos.entity.NotificacaoLog;
import com.exactpromos.entity.Promocao;
import com.exactpromos.entity.Produto;
import com.exactpromos.mapper.PromocaoMapper;
import com.exactpromos.repository.NotificacaoLogRepository;
import com.exactpromos.repository.ProdutoRepository;
import com.exactpromos.repository.PromocaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PromocaoService {

    private final PromocaoRepository promocaoRepository;
    private final ProdutoRepository produtoRepository;
    private final PromocaoMapper promocaoMapper;
    private final TelegramService telegramService;
    private final NotificacaoLogRepository notificacaoLogRepository;

    public PromocaoService(PromocaoRepository promocaoRepository,
                           ProdutoRepository produtoRepository,
                           PromocaoMapper promocaoMapper,
                           TelegramService telegramService,
                           NotificacaoLogRepository notificacaoLogRepository) {
        this.promocaoRepository = promocaoRepository;
        this.produtoRepository = produtoRepository;
        this.promocaoMapper = promocaoMapper;
        this.telegramService = telegramService;
        this.notificacaoLogRepository = notificacaoLogRepository;
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

        if (promocao.getCashback() == null || promocao.getCashback().compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cashback deve ser maior ou igual a zero");
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

    private String montarMensagemPromocao(Promocao promocao) {
        PlataformaEnum plataforma = promocao.getProduto().getPlataforma();
        BigDecimal precoOriginal = promocao.getProduto().getPrecoOriginal();
        BigDecimal precoPromocional = promocao.getPrecoPromocional();
        BigDecimal diferenca = precoOriginal.subtract(precoPromocional);
        BigDecimal descontoPercentualCalculado = diferenca
                .multiply(BigDecimal.valueOf(100))
                .divide(precoOriginal, 2, RoundingMode.HALF_UP);

        String titulo;

        if (plataforma == PlataformaEnum.SHOPEE){
            titulo = "🔥 Oferta na Shopee";
        } else if(plataforma == PlataformaEnum.MERCADO_LIVRE){
            titulo = "🔥 Oferta no Mercado Livre";
        } else {
            titulo = "🔥 Oferta em destaque";
        }

        return """
        %s
        
        %s

        🔥 De <s>R$ %s</s> por R$ %s
        (%s%% OFF)

        Corre que essa oferta pode acabar a qualquer momento.

        Cashback: R$ %s

        Link: %s
        """.formatted(
                titulo,
                promocao.getProduto().getNome(),
                precoOriginal,
                precoPromocional,
                descontoPercentualCalculado,
                promocao.getCashback(),
                promocao.getLinkAfiliado()
        );
    }

    private void publicarPromocao(Promocao promocao) {
        validarDuplicidadePromocao(promocao);
        validarPromocaoParaDivulgar(promocao);
        marcarComoProntaParaEnvio(promocao);

        String mensagem = montarMensagemPromocao(promocao);

        try {
            telegramService.enviarMensagem(mensagem);
            promocao.setStatus(PromocaoEnum.PUBLICADA);
            salvarLogNotificacao(promocao, mensagem, true);
        } catch (Exception e) {
            promocao.setStatus(PromocaoEnum.ERRO_NO_ENVIO);
            salvarLogNotificacao(promocao, mensagem, false);
        }
    }
    private void validarDuplicidadePromocao(Promocao promocao){
        if (promocao.getStatus() == PromocaoEnum.PUBLICADA
                || promocao.getStatus() == PromocaoEnum.ERRO_NO_ENVIO
                || notificacaoLogRepository.existsByPromocaoAndEnviadaTrue(promocao)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Promoção ja enviada");
        }
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
    private void salvarLogNotificacao(Promocao promocao, String mensagem, boolean enviada){
        NotificacaoLog notificacaoLog = new NotificacaoLog();
        notificacaoLog.setPromocao(promocao);
        notificacaoLog.setMensagem(mensagem);
        notificacaoLog.setTipo(TipoNotificacao.NOVA_PROMOCAO);
        notificacaoLog.setEnviada(enviada);
        notificacaoLog.setDataEnvio(LocalDateTime.now());

        notificacaoLogRepository.save(notificacaoLog);

    }

}
