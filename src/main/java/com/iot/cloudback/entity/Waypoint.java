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
 * @ClassName: Waypoint
 * @Description: 地理位置定位型
 * @Author: Young
 * @CreateDate: 2021/6/11 23:20
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/11 23:20
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Document(collection = "waypoint")
@CompoundIndexes(
        @CompoundIndex(name = "timing_idx", def = "{'upDataStreamId':1,'timing':-1}")
)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Waypoint extends UpdateStreamData {

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
     * 经度
     */
    private String longitude;

    /**
     * 维度
     */
    private String latitude;

    /**
     * 海拔
     */
    private String elevation;
}
