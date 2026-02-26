package com.exactpromos.dto.external.MercadoLivreExternal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class MercadoLivreProdutoDTO {

    private String id;

    private String title;

    private String categoryId;

    private Double price;

    private Double originalPrice;

    private String thumbnail;

    private Integer availableQuantity;

    private String condition;

    private String permalink;

    private List<AttributeDTO> attributes;



}




