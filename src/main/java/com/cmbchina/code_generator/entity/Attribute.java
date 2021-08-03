package com.cmbchina.code_generator.entity;

import lombok.Data;

/**
 * 一张数据库表中的属性
 */
@Data
public class Attribute {

    private String name; //名称
    private String type; //类型
    private int length; //长度
    private int precision; //精度
    private int isNotNull; //是否非空
    private int isPrimary; //是否主键
    private int isUnique; //是否唯一
    //private String foreignKey; //外键，格式如：表名(属性) 操作 级联选项
    private String comment; //注释

}
