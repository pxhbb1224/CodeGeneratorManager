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

    /**
     * 数据库中创建表
     * @param table
     * @return
     */
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

    /**
     * 返回表是否存在
     * @param tableName
     * @return
     */
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

    /**
     * 往项目中添加表
     * @param projectName
     * @param table
     * @return
     */
    @Override
    public boolean addTable(String projectName, Table table)
    {
        if(dataMap.getUserDataMap().containsKey(projectName))
        {
            UserData userData = dataMap.getUserDataMap().get(projectName);
            List<Table> temp = userData.getTableList();
            if(temp == null)
                temp = new ArrayList<>();
            for(int i = 0; i < temp.size(); i++)
            {
                if(temp.get(i).getTableName() == table.getTableName())
                {
                    System.out.println("数据库表已存在！");
                    return false;
                }
            }
            temp.add(table);
            userData.setTableList(temp);
            return dataMap.setMap(projectName, userData);
        }
        else
        {
            System.out.println("项目不存在！");
            return false;
        }
    }

    /**
     * 新增或者修改配置
     * @param projectName
     * @param config
     * @return
     */
    @Override
    public boolean setConfig(String projectName, Config config)
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

    /**
     * 根据项目名获取项目
     * @param projectName
     * @return
     */
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

    /**
     * 输出所有项目信息
     */
    @Override
    public void printDataMap()
    {
        System.out.println(dataMap.getUserDataMap());
    }

}
