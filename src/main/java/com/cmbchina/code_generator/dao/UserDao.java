package com.cmbchina.code_generator.dao;

import com.cmbchina.code_generator.entity.Config;
import com.cmbchina.code_generator.entity.Table;
import com.cmbchina.code_generator.entity.UserData;


public interface UserDao {

    boolean createTable(Table table);
    boolean isTableExists(String tableName);
    void addTable(Table table);
    void setConfig(Config config);
    UserData getUserData();
}
