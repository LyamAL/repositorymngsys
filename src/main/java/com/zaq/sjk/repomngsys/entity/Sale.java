package com.zaq.sjk.repomngsys.entity;

import lombok.*;

/**
 * @author ZAQ
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Sale {
    private int gid;
    private float sum;
    private int count;
    private String date;
}
