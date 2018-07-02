package com.imooc.demo.repository;

import com.imooc.demo.dataobject.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Integer>{
    //在JpaRepository中的findAll方法只能根据ID来查找，这里新增一个接口，用类型来findAll
    //这里的POJO命名规则，自动去除前面的findBy，在后面从实体类中，从右到左一次去除大写字母开头
    //的字符串，来在实体类中查看是否有该属性
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
