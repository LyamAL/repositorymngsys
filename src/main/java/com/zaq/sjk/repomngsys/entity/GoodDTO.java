package com.zaq.sjk.repomngsys.entity;

import lombok.*;

import java.util.List;

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
    private String unit;

    private String repoIds;
}
