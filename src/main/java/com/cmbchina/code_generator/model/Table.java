package com.cmbchina.code_generator.model;

import com.cmbchina.code_generator.model.Attribute;
import java.util.List;
import java.util.ArrayList;



public class Table {
    private String tableName;
    private List<Attribute>  table;
    public Table()
    {
        tableName = "tb_test";
        table = new ArrayList<>();
        table.add(new Attribute(1));
        table.add(new Attribute());
    }
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }
    public String getTableName()
    {
        return this.tableName;
    }
    public void addTable(Attribute a)
    {
        table.add(a);
    }
    public List<Attribute> getTable()
    {
        return table;
    }
    public int getAttributeNum()
    {
        return table.size();
    }
}
