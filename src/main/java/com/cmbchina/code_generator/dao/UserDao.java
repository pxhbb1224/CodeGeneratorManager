package com.cmbchina.code_generator.dao;

import com.cmbchina.code_generator.entity.Config;
import com.cmbchina.code_generator.entity.Table;
import com.cmbchina.code_generator.entity.TableColumns;
import com.cmbchina.code_generator.entity.UserData;

import java.util.List;


public interface UserDao {

    /**
     * 数据库中创建表并添加table_info表记录
     * @param table
     * @return
     */
    boolean createTable(Table table);

    /**
     * 返回表是否存在(同时检查数据库中和table_info中的记录)
     * @param tableName
     * @return
     */
    boolean isTableExists(String tableName);

    /**
     * 返回项目是否存在
     * @param projectName
     * @return
     */
    boolean isProjectExists(String projectName);

    /**
     * 返回配置是否存在
     * @param projectName
     * @return
     */
    boolean isConfigExists(String projectName);


    /**
     * 往项目结构中添加表
     * @param projectName
     * @param table
     * @return
     */
    boolean addTable(String projectName, Table table);

    /**
     * 删除对应项目结构中的表,tableName为空时删除项目
     * @param projectName
     * @return
     */
    boolean deleteTable(String projectName, String tableName);

    /**
     * 数据库中删除表，同时删除table_info和project表中记录
     * @param tableName
     * @return
     */
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
