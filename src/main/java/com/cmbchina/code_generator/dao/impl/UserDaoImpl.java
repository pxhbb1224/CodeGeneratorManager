package com.cmbchina.code_generator.dao.impl;

import com.cmbchina.code_generator.dao.UserDao;
import com.cmbchina.code_generator.entity.User;
import com.cmbchina.code_generator.mapper.UserMapper;
import com.cmbchina.code_generator.model.Attribute;
import com.cmbchina.code_generator.model.Table;
import com.cmbchina.code_generator.utils.FormatNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cmbchina.code_generator.utils.FormatNameUtils.formatToSql;

@Repository
public class UserDaoImpl implements UserDao{
    @Autowired
    private UserMapper userMapper;
    @Override
    public boolean createTable(Table table) {
        try
        {
            String str = formatToSql(table);
            userMapper.createTable(str);
            return isTableExists(table.getTableName());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean isTableExists(String tableName)
    {
        try
        {
            return (userMapper.isTableExists(tableName) == 0 ? false : true);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
