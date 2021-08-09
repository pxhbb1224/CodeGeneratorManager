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
    private Map<String, UserData> userDataMap = new HashMap<>(); //项目名唯一标识的项目数据

    /**
     * 新增项目
     * @param userData
     * @return
     */
    public boolean addMap(UserData userData) {
        String name = userData.getConfig().getProjectName();
        if (!userDataMap.containsKey(name)) {
            userDataMap.put(name, userData);
            System.out.println("项目" + name + "添加成功！");
            return true;
        } else {
            System.out.println("项目" + name + "已存在！");
            return false;
        }

    }

    /**
     * 修改项目
     * @param projectName
     * @param userData
     * @return
     */
    public boolean setMap(String projectName, UserData userData)
    {
        if(userDataMap.containsKey(projectName)) {
            userDataMap.replace(projectName, userData);
            System.out.println("项目" + projectName + "修改成功！");
            return true;
        } else {
            System.out.println("项目" + projectName + "已存在！");
            return false;
        }
    }

    /**
     * 删除项目
     * @param projectName
     * @return
     */
    public boolean deleteMap(String projectName)
    {
        if(userDataMap.containsKey(projectName))
        {
            userDataMap.remove(projectName);
            System.out.println("项目" + projectName + "删除成功！");
            return true;
        }
        else
        {
            System.out.println("需要删除的项目" + projectName + "不存在！");
            return false;
        }
    }

    /**
     * 清除map内容
     */
    public void clearMap()
    {
        userDataMap.clear();
    }

    /**
     * 将map内容转化为ProjectData
     * @return
     */
    public List<ProjectData> formatMap()
    {
        List<ProjectData> result = new ArrayList<>();
        for(UserData userData : userDataMap.values())
        {
            ProjectData projectData = new ProjectData(userData);
            result.add(projectData);
        }
        return result;
    }
}
