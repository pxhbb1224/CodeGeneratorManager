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
     * 返回DataMap
     */
    @Override
    public DataMap getDataMap()
    {
        return dataMap;
    }
    /**
     * 数据库中创建表并添加table_info表记录
     * @param table
     * @return
     */
    @Override
    public boolean createTable(Table table) {
        try
        {
            String str = FormatNameUtils.formatToCreateSql(table);
            userMapper.createTable(str);//创建数据库表
            userMapper.insertTable(table.getTableName(), table.getGenerateTime());//添加table_info表记录
            return isTableExists(table.getTableName());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 返回表是否存在(同时检查数据库中和table_info中的记录)
     * @param tableName
     * @return
     */
    @Override
    public boolean isTableExists(String tableName)
    {
        try
        {
            return (userMapper.isTableExists(tableName) +
                    userMapper.isExistsInTable(tableName) == 2 ? true : false);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 返回项目是否存在
     * @param projectName
     * @return
     */
    @Override
    public boolean isProjectExists(String projectName)
    {
        try
        {
            return (userMapper.isProjectExists(projectName) == 0 ? false : true);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 返回配置是否存在
     * @param projectName
     * @return
     */
    @Override
    public boolean isConfigExists(String projectName)
    {
        try
        {
            return (userMapper.isConfigExists(projectName) == 0 ? false : true);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 往项目结构中添加表
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
     * 数据库中删除表，同时删除table_info和project表中记录
     * @param tableName
     * @return
     */
    @Override
    public boolean dropTable(String tableName) {
        try
        {
            String str = FormatNameUtils.formatToDropSql(tableName);
            userMapper.dropTable(str);//删除数据库表
            userMapper.deleteTable(tableName);//删除table_info表中记录
            userMapper.deleteProject(tableName);//删除project表中记录
            boolean temp = userMapper.isExistsInProject(tableName) == 0 ? false : true;//确认记录是否存在与project表中
            return !isTableExists(tableName) && !temp;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 在项目表中添加表记录
     * @param projectName
     * @param tableName
     * @return
     */
    @Override
    public boolean insertProject(String projectName, String tableName)
    {
        try{
            userMapper.addProject(projectName, tableName);
            return userMapper.isExistsInProject(tableName) == 0 ? false : true;
        }catch (Exception e)
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
            if(userData.getTableList() != null) {
                for (Table t : userData.getTableList()) {
                    if (dropTable(t.getTableName()))//删除数据库中表以及table表中的记录
                    {
                        System.out.println("删除数据库表" + t.getTableName() + "成功！");
                    } else {
                        System.out.println("删除数据库表" + t.getTableName() + "失败！");
                    }

                }
            }

            //删除配置表中的记录             删除项目表中的记录
            if(dropConfig(projectName) && dropProject(projectName))
            {
                System.out.println("删除数据库中项目相关项" + projectName + "成功！");
            }
            else
            {
                System.out.println("删除数据库中项目相关项" + projectName + "失败！");
            }
            if(deleteTable(projectName, null))//删除表的项目结构
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
     * 删除project表中对应项目
     * @param projectName
     * @return
     */
    @Override
    public boolean dropProject(String projectName)
    {
        try{
            userMapper.dropProject(projectName);
            return !isProjectExists(projectName);
        }catch(Exception e){
            e.printStackTrace();
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
            if(insertConfig(projectName, config))
                return dataMap.setMap(projectName, userData);
            else
                return false;
        }
        else
        {
            UserData userData = new UserData();
            userData.setConfig(config);
            if(insertConfig(projectName, config))
                return dataMap.addMap(userData);
            else
                return false;
        }
    }

    /**
     *在config表中插入或修改配置
     * @param projectName
     * @param config
     * @return
     */
    @Override
    public boolean insertConfig(String projectName, Config config)
    {
        try{
            userMapper.setConfig(config);//将config数据插入config表中
            return isConfigExists(projectName);
        }catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 删除配置表的记录
     * @param projectName
     * @return
     */
    @Override
    public boolean dropConfig(String projectName)
    {
        try
        {
            userMapper.dropConfig(projectName);
            return !isConfigExists(projectName);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
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

    @Override
    public String getTime(String tableName)
    {
        return userMapper.getTime(tableName);
    }
    /**
     * 将数据库中的项目同步到项目结构中
     */
    @Override
    public void updateData()
    {
        if(dataMap.getUserDataMap().size() > 0)
            return;
        dataMap.clearMap();
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

    /**
     * 将表的字段属性转化为table结构
     * @param tableColumnsList
     * @return
     */
    @Override
    public Table formatToTable(List<TableColumns> tableColumnsList)
    {
        List<Attribute> attributeList = new ArrayList<>();
        Table t = new Table();
        if(tableColumnsList.size() == 0)
            return t;
        t.setTableName(tableColumnsList.get(0).getTableName());
        t.setComment(tableColumnsList.get(0).getTableComment());
        t.setGenerateTime(getTime(tableColumnsList.get(0).getTableName()));
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
