package com.exactpromos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "affiliate-link")
public class AffiliateLinkProperties {

    private String shopeeTemplate;
    private String mercadoLivreTemplate;

    public String getShopeeTemplate() {
        return shopeeTemplate;
    }

    public void setShopeeTemplate(String shopeeTemplate) {
        this.shopeeTemplate = shopeeTemplate;
    }

    public String getMercadoLivreTemplate() {
        return mercadoLivreTemplate;
    }

    public void setMercadoLivreTemplate(String mercadoLivreTemplate) {
        this.mercadoLivreTemplate = mercadoLivreTemplate;
    }
}
