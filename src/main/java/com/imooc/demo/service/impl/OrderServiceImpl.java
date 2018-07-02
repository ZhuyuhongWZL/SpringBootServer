package com.imooc.demo.service.impl;

import com.imooc.demo.converter.OrderMaster2OrderDTOConverter;
import com.imooc.demo.dataobject.OrderDetail;
import com.imooc.demo.dataobject.OrderMaster;
import com.imooc.demo.dataobject.ProductInfo;
import com.imooc.demo.dto.CartDTO;
import com.imooc.demo.dto.OrderDTO;
import com.imooc.demo.enums.OrderStatusEnum;
import com.imooc.demo.enums.PayStatusEnum;
import com.imooc.demo.enums.ResultEnum;
import com.imooc.demo.exception.SellException;
import com.imooc.demo.repository.OrderDetailRepository;
import com.imooc.demo.repository.OrderMasterRepository;
import com.imooc.demo.service.OrderService;
import com.imooc.demo.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.beans.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl  implements OrderService{

    @Autowired
    private ProductServiceImp productServiceImp;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        //生成该订单的订单Id
        String orderId=KeyUtil.genUniqueKey();
        BigDecimal orderAmount=new BigDecimal(0);
        List<CartDTO> cartDTOList=new ArrayList<>();

        //1.查询商品（数量，价格）
        for (OrderDetail orderDetail:orderDTO.getOrderDetailList()){
            ProductInfo productInfo=productServiceImp.findOne(orderDetail.getProductId());
            if(productInfo==null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            //2.计算订单总价
            orderAmount=productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);
            //3.订单详情入库
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            orderDetail.setProductName(productInfo.getProductName());
            orderDetail.setProductPrice(productInfo.getProductPrice());
            orderDetail.setProductIcon(productInfo.getProductIcon());
            orderDetailRepository.save(orderDetail);

            CartDTO cartDTO=new CartDTO(orderDetail.getProductId(),orderDetail.getProductQuantity());
            cartDTOList.add(cartDTO);
        }

        //4.订单master入库

        OrderMaster orderMaster=new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMaster.setOrderId(orderId);
        orderMaster.setOrderAmount(orderAmount);

        orderMasterRepository.save(orderMaster);

        //5.扣库存
        productServiceImp.decreaseStock(cartDTOList);

        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        OrderMaster orderMaster=orderMasterRepository.findById(orderId).get();
        if (orderMaster==null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        List<OrderDetail> orderDetailList=orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)){
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }

        OrderDTO orderDTO=new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage=orderMasterRepository.findByBuyerOpenid(buyerOpenid,pageable);
        //这里返回的需要时Page<OrderDTO>,而dao层查找到的是Page<OrderMaster>,所以需要做一个类型转换
        List<OrderDTO> orderDTOList=OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
        //new的PageImpl传入的参数需要一个List，一个pageable参数和一个总长度
        Page<OrderDTO> orderDTOPage=new PageImpl<OrderDTO>(orderDTOList,pageable,orderMasterPage.getTotalElements());

        return orderDTOPage;
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        OrderMaster orderMaster=new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【取消订单】订单状态不正确，orderId={},orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //修改订单状态
        orderMaster.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        OrderMaster updateResult=orderMasterRepository.save(orderMaster);//会返回更新后的对象
        //如果返回为空，说明更新失败
        if (updateResult==null){
            log.error("【取消订单】取消订单失败,orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        //返还库存
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("【取消订单】订单中没有商品详情,orderDTO={}",orderDTO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDTO> cartDTOList=new ArrayList<>();
        for (OrderDetail orderDetail:orderDTO.getOrderDetailList()){
            CartDTO cartDTO=new CartDTO(orderDetail.getProductId(),orderDetail.getProductQuantity());
            cartDTOList.add(cartDTO);
        }
        productServiceImp.increaseStock(cartDTOList);
        //如果已经支付，需要退款
        if (orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())){
            //TODO
        }

        return orderDTO;
    }

    @Override
    public OrderDTO finish(OrderDTO orderDTO) {
        return null;
    }

    @Override
    public OrderDTO paid(OrderDTO orderDTO) {
        return null;
    }
}
