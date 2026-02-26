package com.exactpromos.dto.external.ShopeeExternal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ShopeeProdutoDTO {

    private String itemId;

    private String name;

    private String description;

    private Long price;

    private Long priceBeforeDiscount;

    private String image;

    private Integer stock;

    private Double rating;

    private Integer ratingCount;

    private String  categoryPath;

    private String brand;
}










