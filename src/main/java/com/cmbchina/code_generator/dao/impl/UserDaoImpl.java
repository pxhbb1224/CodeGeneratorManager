package com.cmbchina.code_generator.dao.impl;

import com.cmbchina.code_generator.dao.UserDao;
import com.cmbchina.code_generator.entity.Config;
import com.cmbchina.code_generator.entity.DataMap;
import com.cmbchina.code_generator.entity.UserData;
import com.cmbchina.code_generator.mapper.UserMapper;
import com.cmbchina.code_generator.entity.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;

import static com.cmbchina.code_generator.utils.FormatNameUtils.formatToSql;

@Repository
public class UserDaoImpl implements UserDao{
    @Autowired
    private UserMapper userMapper;

    private DataMap dataMap = new DataMap();

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
    public String addTable(String projectName, Table table)
    {
        if(dataMap.getUserDataMap().containsKey(projectName))
        {
            UserData userData = dataMap.getUserDataMap().get(projectName);
            List<Table> temp = userData.getTableList();
            if(temp == null)
                temp = new ArrayList<>();
            temp.add(table);
            userData.setTableList(temp);
            return dataMap.addMap(userData);
        }
        else
        {
            return "项目不存在！";
        }
    }

    @Override
    public String setConfig(String projectName, Config config)
    {
        if(dataMap.getUserDataMap().containsKey(projectName))
        {
            UserData userData = dataMap.getUserDataMap().get(projectName);
            userData.setConfig(config);
            return dataMap.setMap(projectName, userData);
        }
        else
        {
            UserData userData = new UserData();
            userData.setConfig(config);
            return dataMap.addMap(userData);
        }
    }

    @Override
    public UserData getUserData(String projectName)
    {
        if(dataMap.getUserDataMap().containsKey(projectName))
        {
            return dataMap.getUserDataMap().get(projectName);
        }
        else
        {
            return new UserData();
        }
    }
}
