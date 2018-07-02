package com.imooc.demo.service.impl;

import com.imooc.demo.dataobject.ProductInfo;
import com.imooc.demo.dto.CartDTO;
import com.imooc.demo.enums.ProductStatusEnum;
import com.imooc.demo.enums.ResultEnum;
import com.imooc.demo.exception.SellException;
import com.imooc.demo.repository.ProductInfoRepository;
import com.imooc.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Override
    public ProductInfo findOne(String productId) {
        return productInfoRepository.findById(productId).get();
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return productInfoRepository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return productInfoRepository.findAll(pageable);

    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return productInfoRepository.save(productInfo);
    }

    @Override
    @Transactional
    public void increaseStock(List<CartDTO> cartDTOS) {
        for (CartDTO cartDTO:cartDTOS){
            //获取数据库中的商品实体
            ProductInfo productInfo=productInfoRepository.findById(cartDTO.getProductId()).get();
            if (productInfo==null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //设置该商品的库存结果
            Integer result=productInfo.getProductStock()+cartDTO.getProductQuantity();
            //设置库存stock，save
            productInfo.setProductStock(result);
            productInfoRepository.save(productInfo);
        }
    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOS) {
        for(CartDTO cartDTO:cartDTOS){
            ProductInfo productInfo=productInfoRepository.findById(cartDTO.getProductId()).get();
            if (productInfo==null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            Integer result=productInfo.getProductStock()-cartDTO.getProductQuantity();
            if (result<0){
                throw  new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }

            productInfo.setProductStock(result);
            productInfoRepository.save(productInfo);
        }
    }
}
