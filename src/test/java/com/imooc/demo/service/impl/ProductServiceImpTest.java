package com.imooc.demo.service.impl;

import com.imooc.demo.dataobject.ProductInfo;
import com.imooc.demo.enums.ProductStatusEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceImpTest {
    @Autowired
    private ProductServiceImp serviceImp;

    @Test
    public void findOne() {
        ProductInfo productInfo=serviceImp.findOne("123456");
        Assert.assertEquals("123456",productInfo.getProductId());
    }

    @Test
    public void findUpAll() {
        List<ProductInfo> list=serviceImp.findUpAll();
        Assert.assertNotEquals(0,list.size());
    }

    @Test
    public void findAll() {
        //PageRequest实现了pageable接口，查看第0页，每页size
        PageRequest request=new PageRequest(0,2);
        Page<ProductInfo> page= serviceImp.findAll(request);
        //System.out.println(page.getTotalElements());
        Assert.assertNotEquals(0,page.getTotalElements());

    }

    @Test
    public void save() {
        ProductInfo productInfo=new ProductInfo();
        productInfo.setProductId("123457");
        productInfo.setProductName("皮皮虾");
        productInfo.setProductPrice(new BigDecimal(10));
        productInfo.setProductStock(100);
        productInfo.setProductDescription("很好吃的虾");
        productInfo.setProductIcon("http://xxxxx.jpg");
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        productInfo.setCategoryType(1);
        ProductInfo result=serviceImp.save(productInfo);
        Assert.assertNotNull(result);
    }
}