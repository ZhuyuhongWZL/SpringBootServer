package com.imooc.demo.repository;

import com.imooc.demo.dataobject.OrderMaster;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRepositoryTest {

    private final String OPENID="110110";

    @Autowired
    private OrderMasterRepository repository;

    @Test
    public void saveTest(){
        OrderMaster orderMaster=new OrderMaster();
        orderMaster.setOrderId("1234567");
        orderMaster.setBuyerName("朱禹宏");
        orderMaster.setBuyerPhone("13679255627");
        orderMaster.setBuyerAddress("西安交大");
        orderMaster.setBuyerOpenid("110110");
        orderMaster.setOrderAmount(new BigDecimal(2.5));

        repository.save(orderMaster);
    }

    @Test
    public void findByBuyerOpenid() {

        PageRequest request=new PageRequest(1,3);

        Page<OrderMaster> result=repository.findByBuyerOpenid(OPENID,request);
        Assert.assertNotEquals(0,result.getTotalElements());
        System.out.println(result.getTotalElements());
    }
}