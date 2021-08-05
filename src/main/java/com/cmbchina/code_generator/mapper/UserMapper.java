package com.cmbchina.code_generator.mapper;

import com.cmbchina.code_generator.entity.Config;
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
    List<User> findAll();
    boolean addProject(@Param("projectName") String projectName, @Param("tableName") String tableName);
    boolean dropProject(String projectName);
    int isProjectExists(String projectName);
    boolean dropTable(String str);
    boolean createTable(String str);
    int isTableExists(String tablename);
}
