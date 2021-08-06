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

    List<TableColumns> getInfo(String tableName);
    List<Config> getConfig();
    List<String> getTable(String projectName);
    String getTime(String tableName);
    List<User> findAll();
    void dropConfig(String projectName);
    void setConfig(Config config);
    int isConfigExists(String projectName);
    void addProject(@Param("projectName") String projectName, @Param("tableName") String tableName);
    void deleteProject(String tableName);
    void dropProject(String projectName);
    int isProjectExists(String projectName);
    int isExistsInProject(String tableName);
    void dropTable(String str);
    void deleteTable(String tableName);
    void insertTable(@Param("tableName") String tableName, @Param("generateTime") String generateTime);
    void createTable(String str);
    int isTableExists(String tableName);
    int isExistsInTable(String tableName);
}
