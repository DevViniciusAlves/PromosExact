package com.exactpromos.service;

import com.exactpromos.Enum.PlataformaEnum;
import com.exactpromos.config.AffiliateLinkProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LinkAfiliadoService {

    private final AffiliateLinkProperties affiliateLinkProperties;

    public LinkAfiliadoService(AffiliateLinkProperties affiliateLinkProperties) {
        this.affiliateLinkProperties = affiliateLinkProperties;
    }

    public String gerarLink(PlataformaEnum plataforma, String linkOrigem, String produtoId, String nome) {
        if (!StringUtils.hasText(linkOrigem)) {
            return null;
        }

        String template = obterTemplate(plataforma);
        if (!StringUtils.hasText(template)) {
            return linkOrigem;
        }

        return template
                .replace("{url}", linkOrigem)
                .replace("{produtoId}", produtoId != null ? produtoId : "")
                .replace("{nome}", nome != null ? nome : "");
    }

    public String limitarParaBanco(String valor) {
        if (valor == null) {
            return null;
        }

        if (valor.length() <= 255) {
            return valor;
        }

        return valor.substring(0, 255);
    }

    private String obterTemplate(PlataformaEnum plataforma) {
        if (plataforma == PlataformaEnum.SHOPEE) {
            return affiliateLinkProperties.getShopeeTemplate();
        }

        if (plataforma == PlataformaEnum.MERCADO_LIVRE) {
            return affiliateLinkProperties.getMercadoLivreTemplate();
        }

        return null;
    }
}
