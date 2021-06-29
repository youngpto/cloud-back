package com.iot.cloudback.entity;

import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @ProjectName: cloud-back
 * @Package: com.iot.cloudback.entity
 * @ClassName: ForwardTable
 * @Description:
 * @Author: Young
 * @CreateDate: 2021/6/23 0:21
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/6/23 0:21
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Document(collection = "forward_table")
@CompoundIndexes({
        @CompoundIndex(name = "forward_senderx", def = "{'sender':1}", unique = true)
})
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ForwardTable {

    @Indexed
    private String sender;

    private List<String> receiver;
}
