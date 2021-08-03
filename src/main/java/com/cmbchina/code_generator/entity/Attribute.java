package com.cmbchina.code_generator.entity;

import lombok.Data;

@Data
public class Attribute {

    private String name;
    private String type;
    private int length;
    private int precision;
    private int isNotNull;
    private int isPrimary;
    private int isUnique;
    //private String foreignKey;
    private String comment;

}
