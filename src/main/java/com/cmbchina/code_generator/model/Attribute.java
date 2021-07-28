package com.cmbchina.code_generator.model;

import java.util.List;
import java.util.ArrayList;
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

}
