package com.cmbchina.code_generator.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * 存储所有项目的数据结构
 */
@Data
public class DataMap {
    private Map<String, Relation> relationMap = new HashMap<>(); //关系id唯一标识的项目和表的映射关系
    private Map<String, Table> tableMap = new HashMap<>(); //所有创建表的集合
    private Map<String, Integer> referenceNum = new HashMap<>(); //表引用次数
    private Map<String, Config> configMap = new HashMap<>(); //所有配置的id->config映射
    /**
     * 新增或修改项目
     * @param relation
     * @param table
     * @return
     */
    public boolean setRelationMap(Relation relation, Table table) {
        if(table != null)
        {
            String tableId = table.getTableId();
            String tableName = table.getTableName();
            if(!tableMap.containsKey(tableId))//如果不存在对应表
            {
                tableMap.put(tableId, table);
            }
            else //如果存在对应表
            {
                tableMap.replace(tableId, table);
            }
            if(referenceNum.containsKey(tableId)) //修改表引用次数
                referenceNum.replace(tableId, referenceNum.get(tableId) + 1);
            else
                referenceNum.put(tableId, 1);

        }
        if(relation != null)
            if(!relationMap.containsKey(relation.getProjectId() + "/" + relation.getTableId()))
            {
                relationMap.put(relation.getProjectId() + "/" + relation.getTableId(), relation);
                return true;
            }
            else
            {
                System.out.println("关系已存在！");
                return false;
            }
        return true;
    }

    /**
     * 设置配置信息
     * @param config
     * @return
     */

    public boolean setConfigMap(Config config)
    {
        String id = config.getProjectId();
        if(!configMap.containsKey(id))
        {
            configMap.put(id, config);
        }
        else
        {
            configMap.replace(id, config);
        }
        return configMap.containsKey(id);
    }
    /**
     * 删除项目
     * @param projectId
     * @return
     */
    public List<String> deleteMap(String projectId)
    {
        List<String> tableList = new ArrayList<>();
        String name = configMap.get(projectId).getProjectName();
        if(!configMap.containsKey(projectId))
        {
            System.out.println("需要删除的项目不存在！");
            return tableList;
        }
        List<String> relationIdList = new ArrayList<>();
        for(Relation relation : relationMap.values())//处理掉userMap中关联表和表引用次数
        {
            if(relation.getProjectId().equals(projectId))
            {
                relationIdList.add(relation.getProjectId() + "/" + relation.getTableId());//获取相应的userData的key信息
                String tableId = relation.getTableId();
                if(referenceNum.containsKey(tableId))
                {
                    Integer num = referenceNum.get(tableId) - 1;
                    if(num > 0)
                    {
                        referenceNum.replace(tableId, num);
                    }
                    else
                    {
                        referenceNum.remove(tableId);
                        if(tableMap.containsKey(tableId))
                        {
                            tableMap.remove(tableId);
                            tableList.add(tableId);
                        }
                    }
                }
            }
        }

        for(String relationId : relationIdList)
        {
            if(relationMap.containsKey(relationId))
            {
                relationMap.remove(relationId);
            }
        }
        configMap.remove(projectId);
        System.out.println("项目" + name + "删除成功！");

        return tableList;
    }

    public String getTableName(String tableId)
    {
        if(tableMap.containsKey(tableId))
        {
            return tableMap.get(tableId).getTableName();
        }
        else
            return null;
    }

    public String getProjectName(String projectId)
    {
        if(configMap.containsKey(projectId))
        {
            return configMap.get(projectId).getProjectName();
        }
        else
            return null;
    }

    /**
     * 修改表结构中的表
     * @param table
     * @return
     */
    public boolean setTableMap(Table table)
    {
        String tableId = table.getTableId();
        if(tableId != null)
            if(tableMap.containsKey(tableId))
            {
                tableMap.replace(tableId, table);
                return true;
            }
        return false;
    }
    /**
     * 删除relation中项目和表相应的映射,返回值反映需不需要删除数据库表
     * @param projectId
     * @param tableId
     * @return
     */
    public boolean deleteTableMap(String projectId, String tableId)
    {
        if(tableMap.containsKey(tableId))
        {
            deleteRelationMap(projectId, tableId);
            if(referenceNum.containsKey(tableId))
            {
                Integer temp = referenceNum.get(tableId);
                temp -= 1;
                if(temp > 0)
                {
                    referenceNum.replace(tableId, temp);
                }
                else
                {
                    referenceNum.remove(tableId);
                    return true;
                }

            }
            System.out.println("删除表成功！");
        }
        else
        {
            System.out.println("表已不存在！");
        }
        return false;
    }

    /**
     * 删除与table相关的relation映射
     * @param projectId
     * @param tableId
     */
    public void deleteRelationMap(String projectId, String tableId)
    {
        for(String relationId : relationMap.keySet())
        {
            if(projectId != null) {
                if (relationId.equals(projectId + "/" + tableId))
                    relationMap.remove(relationId);
            }else
            {
                if (relationId.endsWith("/" + tableId))
                    relationMap.remove(relationId);
            }
        }
        return;
    }
    /**
     * 清除map内容
     */
    public void clearMap()
    {
        relationMap.clear();
        tableMap.clear();
        referenceNum.clear();
        configMap.clear();
    }

    /**
     * 将map内容转化为ProjectData
     * @return
     */
    public List<ProjectData> formatMap()
    {
        List<ProjectData> result = new ArrayList<>();
        for(String projectId : configMap.keySet())
        {
            int temp = 0;
            for(Relation relation : relationMap.values())
            {
                if(relation.getProjectId().equals(projectId))
                {
                   temp++;
                }
            }
            ProjectData projectData = new ProjectData(configMap.get(projectId), temp);
            result.add(projectData);
        }
        return result;
    }
}
