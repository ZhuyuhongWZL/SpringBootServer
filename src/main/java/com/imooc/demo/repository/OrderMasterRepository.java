package com.imooc.demo.repository;

import com.imooc.demo.dataobject.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMasterRepository extends JpaRepository<OrderMaster,String> {

    //根据某个人微信id来查询订单，因为不需要一次性查出所有订单，所以这里使用page
    Page<OrderMaster> findByBuyerOpenid(String buyerOpenid,Pageable pageable);
}
