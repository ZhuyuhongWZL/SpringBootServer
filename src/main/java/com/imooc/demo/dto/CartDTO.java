package com.imooc.demo.dto;

import lombok.Getter;

//购物车,根据前端返回文档编写
@Getter
public class CartDTO {
    //商品Id
    private String productId;

    //数量
    private Integer productQuantity;

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
