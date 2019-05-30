package com.funny.mall.leads.entity;

import com.funny.mall.base.BaseEntity;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity extends BaseEntity implements Serializable {
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
    private String userName;

    /**
     *
     */
    private String orderNo;
}
