package com.iot.cloudback.entity;

import lombok.*;

/**
 * @ProjectName: Cloud
 * @Package: com.iot.cloud.entity
 * @ClassName: Token
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/19 21:32
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/19 21:32
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Token{

    private String type;

    private int id;

    private String uuid;

    private String tokenValue;
}