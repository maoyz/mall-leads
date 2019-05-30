package com.funny.mall.leads.controller;

import com.funny.mall.leads.dao.OrderMapper;
import com.funny.mall.leads.entity.OrderEntity;
import com.funny.mall.leads.util.SnowflakeIdWorker;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private OrderMapper orderMapper;

    @RequestMapping("/order/save")
    @ResponseBody
    public String save() {
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        for (int i = 0; i < 100; i++) {
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setUserName("test user " + i);
            orderEntity.setUserId(Long.valueOf(i));
            orderEntity.setOrderId(idWorker.nextId());
            orderEntity.setOrderNo("order Name");
            orderMapper.insert(orderEntity);
        }
        return "success";
    }

    @GetMapping("/order/{id}")
    public OrderEntity getOrderId(@PathVariable Long id) {
        return orderMapper.findById(id);
    }


    @GetMapping("/order/user/{userId}")
    public List<OrderEntity> getUserOrders(@PathVariable Long userId) {
        return orderMapper.findByUserId(userId);
    }


    @GetMapping("/orders/in")
    public List<OrderEntity> findByOrderIds() {
        return orderMapper.findByOrderIds(Lists.newArrayList(340779241480126464L, 340779267635806209L));
    }


}
