package com.cmbchina.code_generator.dao.impl;

import com.cmbchina.code_generator.dao.UserDao;
import com.cmbchina.code_generator.entity.*;
import com.cmbchina.code_generator.mapper.UserMapper;
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
            if(userMapper.dropProject(projectName))
            {
                System.out.println("删除数据库中项目项" + projectName + "成功！");
            }
            else
            {
                System.out.println("删除数据库中项目项" + projectName + "失败！");
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
     * 获取表信息
     * @param tableName
     * @return
     */
    @Override
    public List<TableColumns> getInfo(String tableName)
    {
        return userMapper.getInfo(tableName);
    }

    /**
     * 获取配置表信息
     * @return
     */
    @Override
    public List<Config> getConfig()
    {
        return userMapper.getConfig();
    }

    /**
     * 获取项目对应表的列表
     * @param projectName
     * @return
     */
    @Override
    public List<String> getTable(String projectName)
    {
        return userMapper.getTable(projectName);
    }

    /**
     * 将数据库中的项目同步到项目结构中
     */
    @Override
    public void updateData()
    {
        List<Config> configList = getConfig();
        for(Config config : configList)
        {
            String projectName = config.getProjectName();
            setConfig(projectName, config);
            List<String> tableList = getTable(projectName);
            for(String tableName : tableList)
            {
                addTable(projectName, formatToTable(getInfo(tableName)));
            }
        }
        return;
    }
    @Override
    public Table formatToTable(List<TableColumns> tableColumnsList)
    {
        List<Attribute> attributeList = new ArrayList<>();
        Table t = new Table();
        t.setTableName(tableColumnsList.get(0).getTableName());
        t.setComment(tableColumnsList.get(0).getTableComment());
        for(TableColumns column : tableColumnsList)
        {
            Attribute a = new Attribute();
            a.setName(column.getColumnName());
            a.setType(column.getDataType());
            if(!column.getColumnType().equals(column.getDataType()))
                if(column.getCharacterMaximumLength() != null)
                    a.setLength(Integer.parseInt(column.getCharacterMaximumLength()));
                else
                {
                    if(column.getNumericPrecision() != null)
                        a.setLength(Integer.parseInt(column.getNumericPrecision()));
                    if(column.getNumericScale() != null)
                        a.setPrecision(Integer.parseInt(column.getNumericScale()));
                }
            if(column.getIsNullable().equals("NO"))
                a.setIsNotNull(1);
            if(column.getColumnKey().equals("PRI"))
            {
                a.setIsPrimary(1);
            }
            else if(column.getColumnKey().equals("UNI"))
            {
                a.setIsUnique(1);
            }
            a.setComment(column.getColumnComment());
            attributeList.add(a);
        }
        t.setProperties(attributeList);
        return t;
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
