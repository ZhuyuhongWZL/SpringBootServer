package com.imooc.demo.repository;

import com.imooc.demo.dataobject.ProductCategory;
import lombok.ToString;
import org.assertj.core.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {
    @Autowired
    private ProductCategoryRepository repository;

    @Test
    public void findOneTest(){
        ProductCategory productCategory=repository.findById(1).get();
        System.out.println(productCategory.toString());
    }

    @Test
    public void saveTest(){
        ProductCategory productCategory1=new ProductCategory("女生最爱",3);
        ProductCategory productCategory2=new ProductCategory("男生最爱",4);
        ProductCategory result=repository.save(productCategory1);
        ProductCategory result2=repository.save(productCategory2);
        Assert.assertNotNull(result);
        //Assert.assertNotEquals(null,result);
    }

    @Test
    public void findByCategoryTypeInTest(){
        List<Integer> list=new ArrayList<>();
        list.add(2);
        list.add(2);
        list.add(4);
        List<ProductCategory> result=repository.findByCategoryTypeIn(list);
        Assert.assertNotEquals(0,result.size());
    }
}