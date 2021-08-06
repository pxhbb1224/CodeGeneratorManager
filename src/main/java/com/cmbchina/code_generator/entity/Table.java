package com.cmbchina.code_generator.entity;

import lombok.Data;

import java.util.List;

/**
 * 存储一个表的所有信息
 */
@Data
public class Table {
    private String tableName; //数据库表名
    private List<Attribute>  properties; //数据库表含有的属性
    private String comment; //表注释
    private String generateTime;//生成时间

    public int getAttributeNum()
    {
        return properties.size();
    }
}
