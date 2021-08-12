package com.cmbchina.code_generator.entity;

import lombok.Data;


import java.util.HashMap;
import java.util.Map;


/**
 * 存储项目和表的关系信息
 */
@Data
public class Relation {

    private String projectId; //项目id
    private String tableId; //表id
    public Relation(String projectId, String tableId)
    {
        this.projectId = projectId;
        this.tableId = tableId;
    }
}
