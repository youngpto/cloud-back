package com.iot.cloudback.entity;

import lombok.*;

/**
 * @ProjectName: Cloud
 * @Package: com.iot.cloud.entity
 * @ClassName: User
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/19 21:30
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/19 21:30
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id;

    private String name;

    private String password;

    private String email;

    private int age;

    private int authority;
}
