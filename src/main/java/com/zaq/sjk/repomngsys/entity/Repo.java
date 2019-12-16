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
public class Good {
    private int id;
    private String origin;
    private String name;
    private float price;
    private int count;
    private int repoId;
}
