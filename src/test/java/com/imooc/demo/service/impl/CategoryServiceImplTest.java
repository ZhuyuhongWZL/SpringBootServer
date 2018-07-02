package com.imooc.demo.service.impl;

import com.imooc.demo.dataobject.ProductCategory;
import com.imooc.demo.service.CategoryService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceImplTest {

    @Autowired
    private CategoryServiceImpl categoryService;

    @Test
    public void findOne() throws Exception{
        ProductCategory productCategory=categoryService.findOne(1);
        Assert.assertEquals(new Integer(1),productCategory.getCategoryId());
    }

    @Test
    public void findAll() {
        List<ProductCategory> list= categoryService.findAll();
    }

    @Test
    public void findByCategoryTypeIn() {
        List<Integer> list=new ArrayList<>(Arrays.asList(1,2,3,4));
        List<ProductCategory> list1=categoryService.findByCategoryTypeIn(list);
    }

    @Test
    public void save() {
        ProductCategory productCategory=new ProductCategory("朱朱最爱",5);
        categoryService.save(productCategory);
    }
}