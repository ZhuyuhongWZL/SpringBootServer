package com.imooc.demo.service;

import com.imooc.demo.dataobject.ProductInfo;
import com.imooc.demo.dto.CartDTO;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.List;

//商品Service接口
public interface ProductService {
    ProductInfo findOne(String productId);

    List<ProductInfo> findUpAll();//查询在架上的商品列表

    Page<ProductInfo> findAll(Pageable pageable);//查询所有商品，同时要分页

    ProductInfo save(ProductInfo productInfo);//存储商品

    //加库存
    void increaseStock(List<CartDTO> cartDTOS);

    //减掉库存
    void decreaseStock(List<CartDTO> cartDTOS);
}
