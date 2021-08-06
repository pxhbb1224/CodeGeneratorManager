package com.cmbchina.code_generator.mapper;

import com.cmbchina.code_generator.entity.Config;
import com.cmbchina.code_generator.entity.Table;
import com.cmbchina.code_generator.entity.TableColumns;
import com.cmbchina.code_generator.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * 获取表的字段属性
     * @param tableName
     * @return
     */
    List<TableColumns> getInfo(String tableName);

    /**
     * 获取配置表中所有配置
     * @return
     */
    List<Config> getConfig();

    /**
     * 获取项目中所有表的表名
     * @param projectName
     * @return
     */
    List<String> getTable(String projectName);

    /**
     * 获取对应表的生成时间
     * @param tableName
     * @return
     */
    String getTime(String tableName);

    /**
     *
     * @return
     */
    List<User> findAll();

    /**
     * 从config表中删除配置
     * @param projectName
     */
    void dropConfig(String projectName);

    /**
     * 往config表中添加或修改配置
     * @param config
     */
    void setConfig(Config config);

    /**
     * 确定config表中是否存在特定配置
     * @param projectName
     * @return
     */
    int isConfigExists(String projectName);

    /**
     * 往project表中添加项目和表的对应关系
     * @param projectName
     * @param tableName
     */
    void addProject(@Param("projectName") String projectName, @Param("tableName") String tableName);

    /**
     * 删除特定表在project表中的记录
     * @param tableName
     */
    void deleteProject(String tableName);

    /**
     * 删除特定项目在project表中的记录
     * @param projectName
     */
    void dropProject(String projectName);

    /**
     * 查看特定项目是否在project表中存在
     * @param projectName
     * @return
     */
    int isProjectExists(String projectName);

    /**
     * 查看特定表是否在project表中存在
     * @param tableName
     * @return
     */
    int isExistsInProject(String tableName);

    /**
     * 用drop删除数据库表
     * @param str
     */
    void dropTable(String str);

    /**
     * 删除特定表在table_info表中的记录
     * @param tableName
     */
    void deleteTable(String tableName);

    /**
     * 向table_info表中插入表数据
     * @param tableName
     * @param generateTime
     */
    void insertTable(@Param("tableName") String tableName, @Param("generateTime") String generateTime);

    /**
     * 创建数据库表
     * @param str
     */
    void createTable(String str);

    /**
     * 查看特定表是否在数据库中存在
     * @param tableName
     * @return
     */
    int isTableExists(String tableName);

    /**
     * 查看特定表是否在table_info表中存在
     * @param tableName
     * @return
     */
    int isExistsInTable(String tableName);
}
