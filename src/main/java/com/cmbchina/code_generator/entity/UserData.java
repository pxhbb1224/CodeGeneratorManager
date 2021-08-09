package com.cmbchina.code_generator.entity;

import lombok.Data;

import java.util.List;

/**
 * 存储一个项目的信息
 */
@Data
public class UserData {

    private List<Table> tableList; //存储项目中所有表的列表
    private Config config; //存储项目的配置信息
    public Table getTable(String tableName) {
        for (Table t : tableList)
        {
            if(t.getTableName().equals(tableName))
            {
                return t;
            }
        }
        return null;
    }
}
