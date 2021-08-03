package com.cmbchina.code_generator.dao;

import com.cmbchina.code_generator.entity.Config;
import com.cmbchina.code_generator.entity.Table;
import com.cmbchina.code_generator.entity.UserData;


public interface UserDao {

    boolean createTable(Table table);
    boolean isTableExists(String tableName);
    boolean addTable(String projectName, Table table);
    boolean setConfig(String projectName, Config config);
    UserData getUserData(String projectName);
    void printDataMap();
}
