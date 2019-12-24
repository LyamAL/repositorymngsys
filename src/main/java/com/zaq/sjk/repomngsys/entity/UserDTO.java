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
public class UserDTO {
    private String username;
    private String phone;
    private String oldPhone;
    private String titles;
}
