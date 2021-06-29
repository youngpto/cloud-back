package com.iot.cloudback.entity;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @ProjectName: cloud-back
 * @Package: com.iot.cloudback.entity
 * @ClassName: MqttLog
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/23 12:25
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/23 12:25
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Document(collection = "mqtt_log")
@CompoundIndexes({
        @CompoundIndex(name = "timing_idx", def = "{'timing':-1}")
})
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MqttLog {
    /**
     * 日志类别
     * <p>
     * 1、设备上报平台
     * 2、平台转发
     * 3、平台下发
     * <pre>
     *     设备上报时，平台根据监听回调记录对应日志。
     *     设备上报后，平台检索数据转发表，向后续终端设备转发该数据。
     *     平台下发属于管理员权限，直接通过前端界面调试对应设备。
     * </pre>
     */
    private int logType;

    @Indexed
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timing;

    private String topic;

    private JSONObject jsonObject;
}
