package com.zaq.sjk.repomngsys.entity;

import lombok.*;

/**
 * @author ZAQ
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeliveryGoods {
    private Good good;
    private int gid;
    private int count;
    private float price;
    private float sum;
    private String note;
    private String deliveryId;
}
