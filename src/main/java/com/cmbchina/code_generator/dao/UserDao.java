package com.cmbchina.code_generator.dao;

import com.cmbchina.code_generator.entity.Config;
import com.cmbchina.code_generator.entity.Table;
import com.cmbchina.code_generator.entity.TableColumns;
import com.cmbchina.code_generator.entity.UserData;

import java.util.List;


public interface UserDao {

    boolean createTable(Table table);
    boolean isTableExists(String tableName);
    boolean isProjectExists(String projectName);
    boolean isConfigExists(String projectName);
    boolean addTable(String projectName, Table table);
    boolean deleteTable(String projectName, String tableName);
    boolean dropTable(String tableName);
    boolean insertProject(String projectName, String tableName);
    boolean deleteProject(String projectName);
    boolean dropProject(String projectName);
    boolean setConfig(String projectName, Config config);
    boolean insertConfig(String projectName, Config config);
    boolean dropConfig(String projectName);
    UserData getUserData(String projectName);
    List<TableColumns> getInfo(String tableName);
    List<Config> getConfig();
    List<String> getTable(String projectName);
    String getTime(String tableName);
    void updateData();
    Table formatToTable(List<TableColumns> tableColumnsList);
    void printDataMap();
}
