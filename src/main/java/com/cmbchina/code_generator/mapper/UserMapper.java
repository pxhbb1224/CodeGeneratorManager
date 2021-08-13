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
     * @param projectId
     * @return
     */
    List<String> getTable(String projectId);

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
     * @param projectId
     */
    void dropConfig(String projectId);

    /**
     * 往config表中添加或修改配置
     * @param config
     */
    void setConfig(Config config);

    /**
     * 确定config表中是否存在特定配置
     * @param projectId
     * @return
     */
    int isConfigExists(String projectId);

    /**
     * 往project表中添加项目和表的对应关系
     * @param projectId
     * @param tableId
     */
    void addProject(@Param("projectId") String projectId, @Param("tableId") String tableId);

    /**
     * 删除特定表在project表中的记录
     * @param tableId
     */
    void deleteProject(String tableId);

    /**
     * 删除特定项目在project表中的记录
     * @param projectId
     */
    void dropProject(String projectId);

    /**
     * 查看特定项目是否在project表中存在
     * @param projectId
     * @return
     */
    int isProjectExists(String projectId);

    /**
     * 查看特定表是否在project表中存在
     * @param tableId
     * @return
     */
    int isExistsInProject(String tableId);

    /**
     * 用drop删除数据库表
     * @param str
     */
    void dropTable(String str);

    /**
     * 删除特定表在table_info表中的记录
     * @param tableId
     */
    void deleteTable(String tableId);

    /**
     * 向table_info表中插入表数据
     * @param tableId
     * @param tableName
     * @param generateTime
     */
    void insertTable(@Param("tableId") String tableId, @Param("tableName") String tableName, @Param("generateTime") String generateTime);

    /**
     * 创建数据库表
     * @param str
     */
    void createTable(String str);

    /**
     * 查看特定表是否在数据库中存在
     * @param tableId
     * @return
     */
    int isTableExists(String tableId);

    /**
     * 查看特定表是否在table_info表中存在
     * @param tableId
     * @return
     */
    int isExistsInTable(String tableId);
}
