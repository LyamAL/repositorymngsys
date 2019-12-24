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
public class Storage {
    private int gid;
    private Good good;
    private Repo repo;
    private int repoId;
    private int count;
    private int qualified;
}
