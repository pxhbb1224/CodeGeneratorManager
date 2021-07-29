package com.cmbchina.code_generator.dao.impl;

import com.cmbchina.code_generator.dao.UserDao;
import com.cmbchina.code_generator.entity.Config;
import com.cmbchina.code_generator.entity.UserData;
import com.cmbchina.code_generator.mapper.UserMapper;
import com.cmbchina.code_generator.entity.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;



import static com.cmbchina.code_generator.utils.FormatNameUtils.formatToSql;

@Repository
public class UserDaoImpl implements UserDao{
    @Autowired
    private UserMapper userMapper;

    private UserData userData = new UserData();

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

    @Override
    public void addTable(Table table)
    {
        userData.getTableList().add(table);
    }

    @Override
    public void setConfig(Config config)
    {
        userData.setConfig(config);
    }

    @Override
    public UserData getUserData() { return userData; }
}
