package com.funny.mall.leads.entity;

import com.funny.mall.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1559039452790L;

    /**
     *
     */
    private Long orderId;

    /**
     *
     */
    private Long userId;

    /**
     *
     */
    private Long itemId;

    /**
     *
     */
    private String itemName;
}
