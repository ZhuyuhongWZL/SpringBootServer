package com.imooc.demo.service.impl;

import com.imooc.demo.dataobject.OrderDetail;
import com.imooc.demo.dto.OrderDTO;
import com.imooc.demo.enums.OrderStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    private final String buyerOpenid="110110";

    private final String ORDER_ID="1530164150345514836";

    @Test
    public void create() {
        OrderDTO orderDTO=new OrderDTO();
        orderDTO.setBuyerAddress("慕课网");
        orderDTO.setBuyerName("朱禹宏");
        orderDTO.setBuyerPhone("13679255627");
        orderDTO.setBuyerOpenid(buyerOpenid);
        //购物车
        List<OrderDetail> orderDetailList=new ArrayList<>();
        OrderDetail o1=new OrderDetail();

        o1.setProductId("123456");
        o1.setProductQuantity(2);
        orderDetailList.add(o1);
        orderDTO.setOrderDetailList(orderDetailList);

        OrderDetail o2=new OrderDetail();

        o2.setProductId("1234567");
        o2.setProductQuantity(3);
        orderDetailList.add(o2);




        OrderDTO result=orderService.create(orderDTO);
        log.info("[创建订单] result={}",result);
    }

    @Test
    public void findOne() {
        OrderDTO orderDTO=orderService.findOne(ORDER_ID);
        log.info("[查询单个订单]result={}",orderDTO);
    }

    @Test
    public void findList() {
        PageRequest request=new PageRequest(0,2);
        Page<OrderDTO> orderDTOPage=orderService.findList(buyerOpenid,request);
        Assert.assertNotEquals(0,orderDTOPage.getTotalElements());
    }

    @Test
    public void cancel() {
        OrderDTO orderDTO=orderService.findOne(ORDER_ID);
        OrderDTO result=orderService.cancel(orderDTO);
        Assert.assertNotEquals(OrderStatusEnum.CANCEL.getCode(),result.getOrderStatus());
    }

    @Test
    public void finish() {
    }

    @Test
    public void paid() {
    }
}