package com.funny.mall.leads.dao;

import com.funny.mall.base.BaseMapper;
import com.funny.mall.leads.entity.OrderEntity;

import java.util.List;

public interface OrderMapper extends BaseMapper<OrderEntity> {

    List<OrderEntity> findByUserId(Long userId);


    List<OrderEntity> findByOrderIds(List<Long> orderIds);


}