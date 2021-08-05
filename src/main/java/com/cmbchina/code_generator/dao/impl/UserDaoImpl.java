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

import com.cmbchina.code_generator.utils.FormatNameUtils;

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
            String str = FormatNameUtils.formatToCreateSql(table);
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
                if(temp.get(i).getTableName().equals(table.getTableName()))
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
     * 删除对应项目结构中的表,tableName为空时删除项目
     * @param projectName
     * @return
     */
    @Override
    public boolean deleteTable(String projectName, String tableName)
    {
        if(tableName == null || tableName.length() == 0)
            return dataMap.deleteMap(projectName);
        else if(dataMap.getUserDataMap().containsKey(projectName))
        {
            UserData userData = dataMap.getUserDataMap().get(projectName);
            List<Table> temp = userData.getTableList();
            if(temp == null)
                temp = new ArrayList<>();
            boolean isFound = false;
            for(int i = 0; i < temp.size(); i++)
            {
                if(temp.get(i).getTableName().equals(tableName))
                {
                    temp.remove(i);
                    isFound = true;
                    System.out.println("项目 " + projectName + "表 " + tableName + "删除成功！");
                    break;
                }
            }
            if(isFound)
            {
                userData.setTableList(temp);
                return dataMap.setMap(projectName, userData);
            }
            else
            {
                System.out.println("表不存在！");
                return false;
            }
        }
        else
        {
            System.out.println("项目不存在！");
            return false;
        }
    }

    /**
     * 数据库中删除表
     * @param tableName
     * @return
     */
    @Override
    public boolean dropTable(String tableName) {
        try
        {
            String str = FormatNameUtils.formatToDropSql(tableName);
            userMapper.dropTable(str);
            return !isTableExists(tableName);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除项目结构及其关联数据库表
     * @param projectName
     * @return
     */
    @Override
    public boolean deleteProject(String projectName)
    {
        if(dataMap.getUserDataMap().containsKey(projectName))
        {
            UserData userData = dataMap.getUserDataMap().get(projectName);
            for(Table t : userData.getTableList())
            {
                if(dropTable(t.getTableName()))
                {
                    System.out.println("删除数据库表" + t.getTableName() + "成功！");
                }
                else
                {
                    System.out.println("删除数据库表" + t.getTableName() + "失败！");
                }

            }
            if(deleteTable(projectName, null))
            {
                System.out.println("删除项目" + projectName + "成功！");
                return true;
            }
            else
            {
                System.out.println("删除项目" + projectName + "失败！");
                return false;
            }
        }
        else
        {
            System.out.println("待删除项目不存在！");
            return false;
        }
    }
    /**
     * 新增或者修改配置，可用于新建项目
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
