package com.iot.cloudback.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @ProjectName: Cloud
 * @Package: com.iot.cloud.entity
 * @ClassName: Device
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/20 2:16
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/20 2:16
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Document(collection = "device")
@CompoundIndexes({
//        @CompoundIndex(name = "udsId_idx", def = "{'upDataStreamId':1}", unique = true),
        @CompoundIndex(name = "device_idx", def = "{'deviceId':1}", unique = true),
        @CompoundIndex(name = "timing_idx", def = "{'timing':-1}")
}
)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    private String userName;

    private String deviceName;

    @Indexed
    private int deviceId;

    //@Indexed(unique = true)
    private String upDataStreamId;

    private String subscribeTopic;

    @Indexed
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timing;

    private int dataType;

    private int isOnline;
}
