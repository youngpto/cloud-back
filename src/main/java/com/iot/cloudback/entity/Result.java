package com.iot.cloudback.entity;

import lombok.*;

/**
 * @ProjectName: Cloud
 * @Package: com.iot.cloud.entity
 * @ClassName: Result
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/19 21:23
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/19 21:23
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private int status;

    private String msg;

    private Object data;
}
