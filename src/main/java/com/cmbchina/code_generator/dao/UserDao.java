package com.cmbchina.code_generator.dao;


import com.cmbchina.code_generator.model.Table;

public interface UserDao {
    public boolean createTable(Table table);
    public boolean isTableExists(String tableName);
}
