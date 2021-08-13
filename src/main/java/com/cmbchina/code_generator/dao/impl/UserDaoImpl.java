package com.cmbchina.code_generator.dao.impl;

import com.cmbchina.code_generator.dao.UserDao;
import com.cmbchina.code_generator.entity.*;
import com.cmbchina.code_generator.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
            String tableId = table.getTableId();
            String tableName = table.getTableName();
            String str = FormatNameUtils.formatToCreateSql(table);
            userMapper.createTable(str);//创建数据库表
            userMapper.insertTable(tableId, tableName, table.getGenerateTime());//添加table_info表记录
            return isTableExists(tableId) && isExistsInTable(tableId);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 返回表是否存在于数据库中
     * @param tableId
     * @return
     */
    @Override
    public boolean isTableExists(String tableId)
    {
        try
        {
            return userMapper.isTableExists('%' + tableId) == 1 ? true : false;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 返回table_info表中是否存在记录
     * @param tableId
     * @return
     */
    @Override
    public boolean isExistsInTable(String tableId)
    {
        try
        {
            return userMapper.isExistsInTable(tableId) == 1 ? true : false;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 返回项目是否存在
     * @param projectId
     * @return
     */
    @Override
    public boolean isProjectExists(String projectId)
    {
        try
        {
            return (userMapper.isProjectExists(projectId) == 0 ? false : true);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 返回配置是否存在
     * @param projectId
     * @return
     */
    @Override
    public boolean isConfigExists(String projectId)
    {
        try
        {
            return (userMapper.isConfigExists(projectId) == 0 ? false : true);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 往项目结构中添加表
     * @param projectId
     * @param table
     * @return
     */
    @Override
    public boolean addTable(String projectId, Table table)
    {
        if(dataMap.getConfigMap().containsKey(projectId))
        {
            Relation relation = new Relation(projectId, table.getTableId()) ;
            return dataMap.setRelationMap(relation, table);
        }
        else
        {
            System.out.println("项目不存在！");
            return false;
        }
    }

    /**
     * 修改数据库中表，同时修改table_info表信息
     * 注意，tableId不会改变
     * @param table
     * @return
     */
    public boolean replaceTable(Table table)
    {
        String tableId = table.getTableId();
        String tableName = table.getTableName();
        if(tableId != null && tableId.trim().length() > 0)
            if(dataMap.getTableMap().containsKey(tableId))
            {
                if(dropTable(tableId, true))
                    if(createTable(table))
                        return dataMap.setTableMap(table);
            }
        return false;
    }

    /**
     * 删除对应项目结构中的表，返回值代表需不需要删除数据库表
     * @param projectId
     * @param tableId
     * @return
     */
    @Override
    public boolean deleteTable(String projectId, String tableId)
    {
        if(dataMap.getTableMap().containsKey(tableId))
        {
            return dataMap.deleteTableMap(projectId, tableId);
        }
        else
        {
            System.out.println("表不存在！");
            return false;
        }
    }

    /**
     * 数据库中删除表，同时删除table_info和project表中记录
     * 布尔值代表是否忽略删除项目表中的记录
     * 尽量要在本地表结构存在之前执行，否则要从数据库中获取tableName
     * @param tableId
     * @param leaveProject
     * @return
     */
    @Override
    public boolean dropTable(String tableId, boolean leaveProject) {
        try
        {
            String tableName = dataMap.getTableName(tableId) == null ?
                    userMapper.getTableName(tableId) : dataMap.getTableName(tableId);
            String str = FormatNameUtils.formatToDropSql(dataMap.getTableName(tableId) + "_" + tableId);
            userMapper.dropTable(str);//删除数据库表
            userMapper.deleteTable(tableId);//删除table_info表中记录
            boolean temp = false;
            if(!leaveProject)
            {
                userMapper.deleteProject(tableId);//删除project表中记录
                temp = userMapper.isExistsInProject(tableId) == 0 ? false : true;//确认记录是否存在与project表中
            }
            return !isTableExists(tableId) && !isExistsInTable(tableId) && !temp;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 在项目表中添加表记录
     * @param projectId
     * @param tableId
     * @return
     */
    @Override
    public boolean insertProject(String projectId, String tableId)
    {
        try{
            String relationId = projectId + "/" + tableId;
            userMapper.addProject(relationId, projectId, tableId);
            return userMapper.isExistsInProject(tableId) == 0 ? false : true;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除项目结构及其关联数据库表
     * @param projectId
     * @return
     */
    @Override
    public boolean deleteProject(String projectId)
    {
        if(dataMap.getConfigMap().containsKey(projectId))
        {
            List<String> tableList = dataMap.deleteMap(projectId);
            for(String tableId : tableList)
            {
                if(dropTable(tableId, false))//删除数据库中表以及table_info表中的记录
                {
                    System.out.println("删除数据库表" + tableId + "成功！");
                }
                else
                {
                    System.out.println("删除数据库表" + tableId + "失败！");
                }

            }

            //删除配置表中的记录             删除项目表中的记录
            if(dropConfig(projectId) && dropProject(projectId))
            {
                System.out.println("删除数据库中项目相关项" + dataMap.getProjectName(projectId) + "成功！");
            }
            else
            {
                System.out.println("删除数据库中项目相关项" + dataMap.getProjectName(projectId) + "失败！");
            }
            System.out.println("删除项目" + dataMap.getProjectName(projectId) + "成功！");
            return true;
        }
        else
        {
            System.out.println("待删除项目不存在！");
            return false;
        }
    }

    /**
     * 删除project表中对应项目
     * @param projectId
     * @return
     */
    @Override
    public boolean dropProject(String projectId)
    {
        try{
            userMapper.dropProject(projectId);
            return !isProjectExists(projectId);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

    }
    /**
     * 新增或者修改配置，可用于新建项目
     * @param projectId
     * @param config
     * @return
     */
    @Override
    public boolean setConfig(String projectId, Config config)
    {
        if(insertConfig(projectId, config))
        {
            return dataMap.setConfigMap(config);
        }
        else
            return false;
    }

    /**
     *在config表中插入或修改配置
     * @param projectId
     * @param config
     * @return
     */
    @Override
    public boolean insertConfig(String projectId, Config config)
    {
        try{
            userMapper.setConfig(config);//将config数据插入config表中
            return isConfigExists(projectId);
        }catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 删除配置表的记录
     * @param projectId
     * @return
     */
    @Override
    public boolean dropConfig(String projectId)
    {
        try
        {
            userMapper.dropConfig(projectId);
            return !isConfigExists(projectId);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据项目名获取项目
     * @param projectId
     * @return
     */
    @Override
    public UserData getUserData(String projectId)
    {
        UserData userData = new UserData();
        List<Table> tableList = new ArrayList<>();
        if(dataMap.getConfigMap().containsKey(projectId))
        {
            List<String> tableIdList = new ArrayList<>();
            for(String relationId : dataMap.getRelationMap().keySet())
            {
                if(relationId.startsWith(projectId + "/"))
                {
                    tableIdList.add(relationId.substring(relationId.indexOf("/") + 1));
                }
            }
            for(String tableId : tableIdList)
            {
                if(dataMap.getTableMap().containsKey(tableId))
                {
                    tableList.add(dataMap.getTableMap().get(tableId));
                }
                else
                {
                    dataMap.deleteRelationMap(null, tableId);//如果不存在，代表表已被删除，需要消除该关系
                }
            }
            userData.setTableList(tableList);
            userData.setConfig(dataMap.getConfigMap().get(projectId));
        }
        return userData;
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
     * @param projectId
     * @return
     */
    @Override
    public List<String> getTable(String projectId)
    {
        return userMapper.getTable(projectId);
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
        if(dataMap.getConfigMap().size() > 0)
            return;
        dataMap.clearMap();
        List<Config> configList = getConfig();
        for(Config config : configList)
        {
            String projectId = config.getProjectId();
            setConfig(projectId, config);
            List<String> tableList = getTable(projectId);
            for(String tableId : tableList)
            {
                addTable(projectId, formatToTable(getInfo('%' + tableId)));
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
        String tableName = tableColumnsList.get(0).getTableName();
        t.setTableId(tableName.substring(tableName.lastIndexOf("_") + 1));
        t.setTableName(tableName.substring(0, tableName.lastIndexOf("_")));
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
        System.out.println(dataMap.getConfigMap());
        System.out.println(dataMap.getTableMap());
        System.out.println(dataMap.getRelationMap());
        System.out.println(dataMap.getReferenceNum());
    }

}
