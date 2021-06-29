package com.iot.cloudback.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iot.cloudback.entity.base.UpdateStreamData;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @ProjectName: iot201859225220
 * @Package: com.young.entity
 * @ClassName: Measurement
 * @Description: 数值测量
 * @Author: Young
 * @CreateDate: 2021/6/11 22:46
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/11 22:46
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Document(collection = "measurement")
@CompoundIndexes(
        @CompoundIndex(name = "timing_idx", def = "{'upDataStreamId':1,'timing':-1}")
)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Measurement extends UpdateStreamData {

    /**
     * 向上数据通道
     */
    @Indexed
    private String upDataStreamId;

    /**
     * 时间戳
     */
    @Indexed
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timing;

    /**
     * 数值
     */
    private String value;
}
