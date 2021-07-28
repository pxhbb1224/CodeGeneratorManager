package com.cmbchina.code_generator.entity;


import lombok.Data;
import java.util.List;

@Data
public class Table {
    private String tableName;
    private List<Attribute>  properties;
    private String generateTime;

    public int getAttributeNum()
    {
        return properties.size();
    }
}
