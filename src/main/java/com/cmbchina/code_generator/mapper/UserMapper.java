package com.cmbchina.code_generator.mapper;

import com.cmbchina.code_generator.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    List<User> findAll();
    boolean dropTable(String str);
    boolean createTable(String str);
    int isTableExists(String tablename);
}
