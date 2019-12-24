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
public class EntryGoods {
    private int gid;
    private Good good;
    private int count;
    private float sum;
    private float price;
    private String entryId;
    private int qualified;
}
