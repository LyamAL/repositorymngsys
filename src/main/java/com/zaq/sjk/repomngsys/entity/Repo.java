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
public class Repo {
    private int id;
    private int capacity;
    private int used;
    private String position;

    public Repo(String position) {
        this.position = position;
    }
}
