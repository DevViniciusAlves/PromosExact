package com.exactpromos.service;

import com.exactpromos.Enum.PlataformaEnum;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LinkPreviewService {

    private static final String HOST_MERCADO_LIVRE = "mercadolivre.com.br";
    private static final String HOST_MERCADO_LIVRE_SHORT = "meli.la";
    private static final String HOST_SHOPEE = "shopee.com.br";
    private static final String HOST_SHOPEE_GLOBAL = "shopee.com";

    private static final Pattern TITLE_META = Pattern.compile("<meta[^>]+property=[\"']og:title[\"'][^>]+content=[\"']([^\"']+)[\"'][^>]*>", Pattern.CASE_INSENSITIVE);
    private static final Pattern IMAGE_META = Pattern.compile("<meta[^>]+property=[\"']og:image[\"'][^>]+content=[\"']([^\"']+)[\"'][^>]*>", Pattern.CASE_INSENSITIVE);
    private static final Pattern PRICE_META = Pattern.compile("<meta[^>]+property=[\"']product:price:amount[\"'][^>]+content=[\"']([^\"']+)[\"'][^>]*>", Pattern.CASE_INSENSITIVE);
    private static final Pattern JSON_LD_NAME = Pattern.compile("\"name\"\\s*:\\s*\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
    private static final Pattern JSON_LD_IMAGE = Pattern.compile("\"image\"\\s*:\\s*\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
    private static final Pattern JSON_LD_PRICE = Pattern.compile("\"price\"\\s*:\\s*\"?([0-9]+(?:[\\.,][0-9]+)?)\"?", Pattern.CASE_INSENSITIVE);
    private static final Pattern MERCADO_LIVRE_PRODUCT_ID = Pattern.compile("(?<![A-Z0-9])(MLB\\d{6,})(?![A-Z0-9])", Pattern.CASE_INSENSITIVE);
    private static final Pattern MERCADO_LIVRE_PRODUCT_ID_JSON = Pattern.compile("\"(?:product_id|item_id)\"\\s*:\\s*\"(MLB\\d{6,})\"", Pattern.CASE_INSENSITIVE);
    private static final Pattern MERCADO_LIVRE_PRICE_JSON = Pattern.compile("\"type\"\\s*:\\s*\"price\".*?\"current_price\"\\s*:\\s*\\{\\s*\"value\"\\s*:\\s*([0-9]+(?:[\\.,][0-9]+)?)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern MERCADO_LIVRE_ORIGINAL_PRICE_JSON = Pattern.compile("\"type\"\\s*:\\s*\"price\".*?\"original_price\"\\s*:\\s*\\{\\s*\"value\"\\s*:\\s*([0-9]+(?:[\\.,][0-9]+)?)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern MERCADO_LIVRE_OLD_PRICE_JSON = Pattern.compile("\"old_price\"\\s*:\\s*\\{\\s*\"value\"\\s*:\\s*([0-9]+(?:[\\.,][0-9]+)?)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern MERCADO_LIVRE_PREVIOUS_PRICE_JSON = Pattern.compile("\"previous_price\"\\s*:\\s*\\{\\s*\"value\"\\s*:\\s*([0-9]+(?:[\\.,][0-9]+)?)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern MERCADO_LIVRE_LIST_PRICE_JSON = Pattern.compile("\"list_price\"\\s*:\\s*\\{\\s*\"value\"\\s*:\\s*([0-9]+(?:[\\.,][0-9]+)?)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern MERCADO_LIVRE_BASE_PRICE_JSON = Pattern.compile("\"base_price\"\\s*:\\s*([0-9]+(?:[\\.,][0-9]+)?)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern MERCADO_LIVRE_PRICE_BEFORE_DISCOUNT_JSON = Pattern.compile("\"price_before_discount\"\\s*:\\s*([0-9]+(?:[\\.,][0-9]+)?)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern MERCADO_LIVRE_DISCOUNT_PERCENT_JSON = Pattern.compile("\"(?:discount_percentage|discount|percent_off|percentage)\"\\s*:\\s*([0-9]+(?:[\\.,][0-9]+)?)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern MERCADO_LIVRE_TITLE_JSON = Pattern.compile("\"type\"\\s*:\\s*\"title\".*?\"text\"\\s*:\\s*\"([^\"]+)\"", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern MERCADO_LIVRE_IMAGE_JSON = Pattern.compile("\"type\"\\s*:\\s*\"image\".*?\"content\"\\s*:\\s*\"([^\"]+)\"", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public LinkPreviewService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    public PreviewResultado analisar(String url) {
        String html = buscarHtml(url);

        PlataformaEnum plataforma = identificarPlataforma(url);
        if (plataforma == PlataformaEnum.MERCADO_LIVRE) {
            PreviewResultado mercadoLivre = analisarMercadoLivre(url, html);
            if (mercadoLivre != null) {
                return mercadoLivre;
            }
        }

        String titulo = extrairPrimeiraString(html, TITLE_META, JSON_LD_NAME);
        String imagem = extrairPrimeiraString(html, IMAGE_META, JSON_LD_IMAGE);
        BigDecimal preco = extrairPreco(html);

        return new PreviewResultado(plataforma, titulo, imagem, preco, preco, 0);
    }

    private String buscarHtml(String url) {
        try {
            URI uri = validarUrlPermitida(url);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8")
                    .header("Accept-Language", "pt-BR,pt;q=0.9,en;q=0.8")
                    .header("Referer", "https://www.mercadolivre.com.br/")
                    .header("Upgrade-Insecure-Requests", "1")
                    .header("Cache-Control", "no-cache")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            validarUrlFinalPermitida(response.uri());
            return response.body();
        } catch (Exception e) {
            throw new IllegalStateException("Nao foi possivel carregar a pagina", e);
        }
    }

    private PlataformaEnum identificarPlataforma(String url) {
        String normalizado = url == null ? "" : url.toLowerCase(Locale.ROOT);
        if (normalizado.contains("mercadolivre") || normalizado.contains("meli.la")) {
            return PlataformaEnum.MERCADO_LIVRE;
        }
        return PlataformaEnum.SHOPEE;
    }

    private URI validarUrlPermitida(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("URL invalida");
        }

        URI uri = URI.create(url);
        if (!"https".equalsIgnoreCase(uri.getScheme())) {
            throw new IllegalArgumentException("Apenas URLs https sao permitidas");
        }

        String host = normalizarHost(uri.getHost());
        if (host == null || !hostPermitido(host)) {
            throw new IllegalArgumentException("Link fora das lojas permitidas");
        }

        bloquearHostPrivado(host);
        return uri;
    }

    private void validarUrlFinalPermitida(URI uri) {
        if (uri == null) {
            throw new IllegalArgumentException("URL final invalida");
        }

        String host = normalizarHost(uri.getHost());
        if (host == null || !hostPermitido(host)) {
            throw new IllegalArgumentException("Redirect final fora das lojas permitidas");
        }

        bloquearHostPrivado(host);
    }

    private boolean hostPermitido(String host) {
        return host.equals(HOST_MERCADO_LIVRE)
                || host.endsWith("." + HOST_MERCADO_LIVRE)
                || host.equals(HOST_MERCADO_LIVRE_SHORT)
                || host.equals(HOST_SHOPEE)
                || host.endsWith("." + HOST_SHOPEE)
                || host.equals(HOST_SHOPEE_GLOBAL)
                || host.endsWith("." + HOST_SHOPEE_GLOBAL);
    }

    private String normalizarHost(String host) {
        if (host == null) {
            return null;
        }
        return host.trim().toLowerCase(Locale.ROOT);
    }

    private void bloquearHostPrivado(String host) {
        try {
            InetAddress endereco = InetAddress.getByName(host);
            if (endereco.isAnyLocalAddress()
                    || endereco.isLoopbackAddress()
                    || endereco.isLinkLocalAddress()
                    || endereco.isSiteLocalAddress()) {
                throw new IllegalArgumentException("Host privado nao permitido");
            }
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Host invalido", e);
        }
    }

    private PreviewResultado analisarMercadoLivre(String url, String html) {
        PreviewResultado extraidoDoHtml = extrairMercadoLivreDoHtml(html);
        String itemId = extrairMercadoLivreItemId(url, html);
        PreviewResultado viaApi = null;

        if (itemId != null) {
            viaApi = buscarMercadoLivreViaApi(itemId);
        }

        return mesclarPreviewMercadoLivre(extraidoDoHtml, viaApi);
    }

    private PreviewResultado mesclarPreviewMercadoLivre(PreviewResultado extraidoDoHtml, PreviewResultado viaApi) {
        if (extraidoDoHtml == null && viaApi == null) {
            return null;
        }

        String titulo = primeiroNaoVazio(
                extraidoDoHtml != null ? extraidoDoHtml.titulo() : null,
                viaApi != null ? viaApi.titulo() : null
        );
        String imagem = primeiroNaoVazio(
                extraidoDoHtml != null ? extraidoDoHtml.imagem() : null,
                viaApi != null ? viaApi.imagem() : null
        );

        BigDecimal precoPromocional = escolherMenorPrecoValido(
                extraidoDoHtml != null ? extraidoDoHtml.preco() : null,
                extraidoDoHtml != null ? extraidoDoHtml.precoOriginal() : null,
                viaApi != null ? viaApi.preco() : null,
                viaApi != null ? viaApi.precoOriginal() : null
        );

        if (titulo == null || precoPromocional == null) {
            return null;
        }

        BigDecimal precoOriginal = escolherMaiorPrecoValido(
                extraidoDoHtml != null ? extraidoDoHtml.preco() : null,
                extraidoDoHtml != null ? extraidoDoHtml.precoOriginal() : null,
                viaApi != null ? viaApi.preco() : null,
                viaApi != null ? viaApi.precoOriginal() : null
        );

        if (precoOriginal != null && precoOriginal.compareTo(precoPromocional) <= 0) {
            precoOriginal = precoPromocional;
        }

        Integer descontoPercentual = escolherMaiorDescontoValido(
                extraidoDoHtml != null ? extraidoDoHtml.descontoPercentual() : null,
                viaApi != null ? viaApi.descontoPercentual() : null
        );

        if ((descontoPercentual == null || descontoPercentual <= 0)
                && precoOriginal != null
                && precoOriginal.compareTo(precoPromocional) > 0) {
            descontoPercentual = calcularDescontoPercentual(precoOriginal, precoPromocional);
        }

        return new PreviewResultado(
                PlataformaEnum.MERCADO_LIVRE,
                titulo,
                imagem,
                precoPromocional,
                precoOriginal,
                descontoPercentual
        );
    }

    private PreviewResultado extrairMercadoLivreDoHtml(String html) {
        Document document = Jsoup.parse(html == null ? "" : html);

        String titulo = primeiroNaoVazio(
                metaContent(document, "meta[property=og:title]"),
                metaContent(document, "meta[name=title]"),
                metaContent(document, "meta[name=twitter:title]"),
                extrair(html, MERCADO_LIVRE_TITLE_JSON),
                extrair(html, TITLE_META),
                extrair(html, JSON_LD_NAME)
        );

        String imagem = primeiroNaoVazio(
                metaContent(document, "meta[property=og:image]"),
                metaContent(document, "meta[name=image]"),
                metaContent(document, "meta[name=twitter:image]"),
                extrair(html, MERCADO_LIVRE_IMAGE_JSON),
                extrair(html, IMAGE_META),
                extrair(html, JSON_LD_IMAGE)
        );

        BigDecimal precoPromocional = extrairPrecoMercadoLivre(html);
        BigDecimal precoOriginal = extrairPrecoOriginalMercadoLivre(html);
        Integer descontoPercentual = extrairDescontoMercadoLivre(html);
        if (precoOriginal == null && precoPromocional != null) {
            if (descontoPercentual != null && descontoPercentual > 0) {
                BigDecimal fator = BigDecimal.ONE.subtract(BigDecimal.valueOf(descontoPercentual).divide(BigDecimal.valueOf(100), 4, java.math.RoundingMode.HALF_UP));
                if (fator.compareTo(BigDecimal.ZERO) > 0) {
                    precoOriginal = precoPromocional.divide(fator, 2, java.math.RoundingMode.HALF_UP);
                }
            }
        }
        if (precoPromocional == null && precoOriginal == null) {
            precoPromocional = extrairPreco(html);
        }

        if (titulo != null && (precoPromocional != null || precoOriginal != null)) {
            BigDecimal precoBase = precoOriginal != null ? precoOriginal : precoPromocional;
            BigDecimal precoFinal = precoPromocional != null ? precoPromocional : precoBase;
            if (descontoPercentual == null && precoBase != null && precoFinal != null && precoBase.compareTo(precoFinal) > 0) {
                descontoPercentual = calcularDescontoPercentual(precoBase, precoFinal);
            }
            return new PreviewResultado(PlataformaEnum.MERCADO_LIVRE, titulo, imagem, precoFinal, precoBase, descontoPercentual);
        }

        return null;
    }

    private String metaContent(Document document, String cssQuery) {
        if (document == null) {
            return null;
        }

        Element element = document.selectFirst(cssQuery);
        if (element == null) {
            return null;
        }

        String content = element.attr("content");
        return content != null && !content.isBlank() ? content.trim() : null;
    }

    private String primeiroNaoVazio(String... valores) {
        if (valores == null) {
            return null;
        }

        for (String valor : valores) {
            if (valor != null && !valor.isBlank()) {
                return valor.trim();
            }
        }
        return null;
    }

    private BigDecimal escolherMenorPrecoValido(BigDecimal... valores) {
        BigDecimal menor = null;
        if (valores == null) {
            return null;
        }

        for (BigDecimal valor : valores) {
            if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            if (menor == null || valor.compareTo(menor) < 0) {
                menor = valor;
            }
        }
        return menor;
    }

    private BigDecimal escolherMaiorPrecoValido(BigDecimal... valores) {
        BigDecimal maior = null;
        if (valores == null) {
            return null;
        }

        for (BigDecimal valor : valores) {
            if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            if (maior == null || valor.compareTo(maior) > 0) {
                maior = valor;
            }
        }
        return maior;
    }

    private Integer escolherMaiorDescontoValido(Integer... valores) {
        Integer maior = null;
        if (valores == null) {
            return null;
        }

        for (Integer valor : valores) {
            if (valor == null || valor <= 0) {
                continue;
            }
            if (maior == null || valor > maior) {
                maior = valor;
            }
        }
        return maior;
    }

    private PreviewResultado buscarMercadoLivreViaApi(String itemId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.mercadolibre.com/items/" + itemId))
                    .header("User-Agent", "Mozilla/5.0")
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                return null;
            }

            JsonNode node = objectMapper.readTree(response.body());
            String titulo = texto(node, "title");
            BigDecimal preco = node.hasNonNull("price") ? node.get("price").decimalValue() : null;
            BigDecimal precoOriginal = extrairPrecoOriginalDoNode(node);
            String imagem = texto(node, "thumbnail");
            if ((imagem == null || imagem.isBlank()) && node.path("pictures").isArray() && !node.path("pictures").isEmpty()) {
                JsonNode primeiraImagem = node.path("pictures").get(0);
                imagem = texto(primeiraImagem, "secure_url");
                if (imagem == null || imagem.isBlank()) {
                    imagem = texto(primeiraImagem, "url");
                }
            }

            if (titulo == null || preco == null) {
                return null;
            }

            BigDecimal precoBase = precoOriginal != null && precoOriginal.compareTo(preco) > 0 ? precoOriginal : preco;
            Integer descontoPercentual = calcularDescontoPercentual(precoBase, preco);
            return new PreviewResultado(PlataformaEnum.MERCADO_LIVRE, titulo, imagem, preco, precoBase, descontoPercentual);
        } catch (Exception e) {
            return null;
        }
    }

    private BigDecimal extrairPrecoOriginalDoNode(JsonNode node) {
        if (node == null) {
            return null;
        }

        String[] campos = {
                "original_price",
                "old_price",
                "previous_price",
                "list_price",
                "price_before_discount",
                "base_price",
                "sale_price",
                "regular_amount",
                "original_amount",
                "strikethrough_price"
        };

        BigDecimal valorDireto = procurarNumeroEmNode(node, campos);
        if (valorDireto != null) {
            return valorDireto;
        }

        return procurarNumeroRecursivo(node, campos, 3);
    }

    private BigDecimal procurarNumeroRecursivo(JsonNode node, String[] campos, int profundidade) {
        if (node == null || profundidade < 0) {
            return null;
        }

        BigDecimal direto = procurarNumeroEmNode(node, campos);
        if (direto != null) {
            return direto;
        }

        if (node.isObject()) {
            for (JsonNode filho : node) {
                BigDecimal encontrado = procurarNumeroRecursivo(filho, campos, profundidade - 1);
                if (encontrado != null) {
                    return encontrado;
                }
            }
        }

        if (node.isArray()) {
            for (JsonNode filho : node) {
                BigDecimal encontrado = procurarNumeroRecursivo(filho, campos, profundidade - 1);
                if (encontrado != null) {
                    return encontrado;
                }
            }
        }

        return null;
    }

    private BigDecimal procurarNumeroEmNode(JsonNode node, String[] campos) {
        if (node == null || !node.isObject()) {
            return null;
        }

        for (String campo : campos) {
            if (node.hasNonNull(campo)) {
                BigDecimal valor = converterParaBigDecimal(node.get(campo));
                if (valor != null) {
                    return valor;
                }
            }
        }

        return null;
    }

    private BigDecimal converterParaBigDecimal(JsonNode valorNode) {
        if (valorNode == null) {
            return null;
        }

        if (valorNode.isNumber()) {
            return valorNode.decimalValue();
        }

        if (valorNode.isTextual()) {
            String valor = valorNode.asText();
            if (valor != null && !valor.isBlank()) {
                try {
                    return new BigDecimal(valor.replace(",", "."));
                } catch (NumberFormatException ignored) {
                }
            }
        }

        if (valorNode.hasNonNull("value")) {
            return converterParaBigDecimal(valorNode.get("value"));
        }

        if (valorNode.hasNonNull("amount")) {
            return converterParaBigDecimal(valorNode.get("amount"));
        }

        return null;
    }

    private String texto(JsonNode node, String campo) {
        if (node == null || !node.hasNonNull(campo)) {
            return null;
        }
        String valor = node.get(campo).asText();
        return valor != null && !valor.isBlank() ? valor.trim() : null;
    }

    private String extrairMercadoLivreItemId(String url, String html) {
        String valor = extrair(url, MERCADO_LIVRE_PRODUCT_ID);
        if (valor != null && !valor.isBlank()) {
            return valor.trim();
        }

        valor = extrair(html, MERCADO_LIVRE_PRODUCT_ID_JSON);
        if (valor != null && !valor.isBlank()) {
            return valor.trim();
        }

        return null;
    }

    private String extrairPrimeiraString(String html, Pattern metaPattern, Pattern jsonPattern) {
        String valor = extrair(html, metaPattern);
        if (valor != null && !valor.isBlank()) {
            return valor.trim();
        }

        valor = extrair(html, jsonPattern);
        if (valor != null && !valor.isBlank()) {
            return valor.trim();
        }

        return null;
    }

    private BigDecimal extrairPreco(String html) {
        String valor = extrair(html, PRICE_META);
        if (valor == null || valor.isBlank()) {
            valor = extrair(html, JSON_LD_PRICE);
        }

        if (valor == null || valor.isBlank()) {
            return null;
        }

        String normalizado = valor.trim().replace(",", ".");
        return new BigDecimal(normalizado);
    }

    private BigDecimal extrairPrecoMercadoLivre(String html) {
        String valor = extrair(html, MERCADO_LIVRE_PRICE_JSON);
        if (valor == null || valor.isBlank()) {
            return null;
        }

        String normalizado = valor.trim().replace(",", ".");
        return new BigDecimal(normalizado);
    }

    private BigDecimal extrairPrecoOriginalMercadoLivre(String html) {
        Pattern[] padroes = {
                MERCADO_LIVRE_ORIGINAL_PRICE_JSON,
                MERCADO_LIVRE_OLD_PRICE_JSON,
                MERCADO_LIVRE_PREVIOUS_PRICE_JSON,
                MERCADO_LIVRE_LIST_PRICE_JSON,
                MERCADO_LIVRE_BASE_PRICE_JSON,
                MERCADO_LIVRE_PRICE_BEFORE_DISCOUNT_JSON
        };

        for (Pattern pattern : padroes) {
            String valor = extrair(html, pattern);
            if (valor != null && !valor.isBlank()) {
                String normalizado = valor.trim().replace(",", ".");
                return new BigDecimal(normalizado);
            }
        }
        return null;
    }

    private Integer extrairDescontoMercadoLivre(String html) {
        String valor = extrair(html, MERCADO_LIVRE_DISCOUNT_PERCENT_JSON);
        if (valor == null || valor.isBlank()) {
            return null;
        }

        String normalizado = valor.trim().replace(",", ".");
        try {
            return new BigDecimal(normalizado).setScale(0, java.math.RoundingMode.HALF_UP).intValue();
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer calcularDescontoPercentual(BigDecimal original, BigDecimal promocional) {
        if (original == null || promocional == null || original.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }

        BigDecimal diferenca = original.subtract(promocional);
        if (diferenca.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }

        BigDecimal desconto = diferenca
                .divide(original, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        int valor = desconto.setScale(0, java.math.RoundingMode.HALF_UP).intValue();
        return Math.max(valor, 1);
    }

    private String extrair(String html, Pattern pattern) {
        if (html == null) {
            return null;
        }

        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public record PreviewResultado(PlataformaEnum plataforma, String titulo, String imagem, BigDecimal preco, BigDecimal precoOriginal, Integer descontoPercentual) {}
}
