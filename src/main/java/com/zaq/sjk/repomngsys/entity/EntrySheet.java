package com.zaq.sjk.repomngsys.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author ZAQ
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EntrySheet {
    private String id;
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date closeTime;
    private Repo repoId;
    private String contact;
    private String note;
    private String producer;
    private EntryStatus status;
}
