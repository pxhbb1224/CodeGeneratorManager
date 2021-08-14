package com.cmbchina.code_generator.dao;

import com.cmbchina.code_generator.entity.*;

import java.util.List;


public interface UserDao {

    /**
     * 返回DataMap
     * @return
     */
    DataMap getDataMap();//
    /**
     * 数据库中创建表并添加table_info表记录
     * @param table
     * @return
     */
    boolean createTable(Table table);//

    /**
     * 返回表是否存在于数据库中
     * @param tableId
     * @return
     */
    boolean isTableExists(String tableId);//

    /**
     * 返回table_info表中是否存在记录
     * @param tableId
     * @return
     */
    boolean isExistsInTable(String tableId);//

    /**
     * 返回项目是否存在
     * @param projectId
     * @return
     */
    boolean isProjectExists(String projectId);//

    /**
     * 返回配置是否存在
     * @param projectName
     * @return
     */
    boolean isConfigExists(String projectName);//


    /**
     * 往项目结构中添加表
     * @param projectId
     * @param table
     * @return
     */
    boolean addTable(String projectId, Table table);//

    /**
     * 修改数据库中表，同时修改table_info表信息
     * 注意，tableId不会改变
     * @param table
     * @return
     */
    boolean replaceTable(Table table);//

    /**
     * 删除对应项目结构中的表，返回值代表需不需要删除数据库表
     * @param projectId
     * @param tableId
     * @return
     */

    boolean deleteTable(String projectId, String tableId);//

    /**
     * 数据库中删除表，同时删除table_info和project表中记录
     * 布尔值代表是否忽略删除项目表中的记录
     * @param tableId
     * @param leaveProject
     * @return
     */
    boolean dropTable(String tableId, boolean leaveProject);//
    boolean insertProject(String projectId, String tableId);//
    boolean deleteProject(String projectId);//
    boolean dropProject(String projectId);//
    boolean setConfig(String projectId, Config config);//
    boolean insertConfig(String projectId, Config config);//
    boolean dropConfig(String projectId);//
    UserData getUserData(String projectId);//
    List<TableColumns> getInfo(String tableName);//保留
    List<Config> getConfig();//
    List<String> getTable(String projectId);//
    String getTime(String tableName);//保留
    void updateData();//
    Table formatToTable(List<TableColumns> tableColumnsList);//
    void printDataMap();//
}
