package com.imooc.demo.controller;

import VO.ProductInfoVO;
import VO.ProductVO;
import VO.ResultVO;
import com.imooc.demo.dataobject.ProductCategory;
import com.imooc.demo.dataobject.ProductInfo;
import com.imooc.demo.service.CategoryService;
import com.imooc.demo.service.ProductService;
import com.imooc.demo.service.impl.CategoryServiceImpl;
import com.imooc.demo.service.impl.ProductServiceImp;
import com.imooc.demo.utils.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController   //jason格式
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductServiceImp productService;

    @Autowired
    private CategoryServiceImpl categoryService;

    //在用户界面进行商品界面的展示
    @GetMapping("/list")
    public ResultVO list(){
        //1.查询所有的上架商品
        List<ProductInfo> productInfoList=productService.findUpAll();

        //2.查询类目（一次性查询）
        //传统方法
        List<Integer> categoryTypeList=new ArrayList<>();
        for(ProductInfo productInfo:productInfoList){
            categoryTypeList.add(productInfo.getCategoryType());
        }
        //精简方法（java8，lambda）
        /*List<Integer> categoryTypeList=productInfoList.stream()
                .map(e->e.getCategoryType())
                .collect(Collectors.toList());*/
        List<ProductCategory> productCategoryList=categoryService.findByCategoryTypeIn(categoryTypeList);
        //3.数据拼装
        List<ProductVO> productVOList=new ArrayList<>();
        for (ProductCategory productCategory:productCategoryList){
            ProductVO productVO=new ProductVO();
            productVO.setCategoryType(productCategory.getCategoryType());
            productVO.setCategoryName(productCategory.getCategoryName());
            //遍历所有的上架商品，如果商品类目等于该类目，则进行拼装
            //拼装是要把productInfo拷贝进productInfoVO
            //注意要先遍历商品类目再遍历商品，因为包装包含关系
            List<ProductInfoVO> productInfoVOList=new ArrayList<>();
            for (ProductInfo productInfo:productInfoList){
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVO productInfoVO=new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo,productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }
        return ResultVOUtil.success(productVOList);
    }
}
