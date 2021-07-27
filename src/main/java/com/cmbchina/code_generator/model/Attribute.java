package com.cmbchina.code_generator.model;

import java.util.List;
import java.util.ArrayList;
import lombok.Data;

@Data
public class Attribute {

    private String nameId;
    private String typeName;
    private List<Integer> length;
    private boolean isNotNull;
    private boolean isPrimary;
    private boolean isUnique;
    private String foreignKey;

    public Attribute()
    {
        nameId = "type_name";
        typeName = "varchar";
        length = new ArrayList<>();
        isNotNull = true;
        isPrimary = false;
        isUnique = true;
        foreignKey = "dept(name) update cascade";
    }
    public Attribute(int i)
    {
        nameId = "name_id";
        typeName = "decimal";
        length = new ArrayList<>();
        length.add(10);
        length.add(2);
        isNotNull = true;
        isPrimary = true;
        isUnique = true;
        foreignKey = "dept(id) update cascade";
    }
}
