package com.imooc.demo.repository;

import com.imooc.demo.dataobject.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,String> {

    //master里面一条记录可能对应detail里面多条记录
    List<OrderDetail> findByOrderId(String orderID);
}
