package com.example.catalogservice.vo;

import lombok.Data;

@Data

public class CatalogDto {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;

    private Integer orderId;
    private Integer userId;
}
