package com.imooc.demo.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class ProductInfo {

    @Id
    private String productId;

    private String productName;

    private BigDecimal productPrice;

    private Integer productStock;//库存

    private String productDescription;

    private String productIcon;

    private Integer productStatus;//商品状态 0正常 1下架

    private Integer categoryType;


}
