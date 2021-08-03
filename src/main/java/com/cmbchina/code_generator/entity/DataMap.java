package com.cmbchina.code_generator.entity;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class DataMap {
    private Map<String, UserData> userDataMap = new HashMap<>();

    public boolean addMap(UserData userData) {
        String name = userData.getConfig().getProjectName();
        if (!userDataMap.containsKey(name)) {
            userDataMap.put(name, userData);
            System.out.println("项目添加成功！");
            return true;
        } else {
            System.out.println("项目已存在！");
            return false;
        }

    }

    public boolean setMap(String projectName, UserData userData)
    {
        if(userDataMap.containsKey(projectName)) {
            userDataMap.replace(projectName, userData);
            System.out.println("项目修改成功！");
            return true;
        } else {
            System.out.println("项目已存在！");
            return false;
        }
    }

    public boolean deleteMap(String projectName)
    {
        if(userDataMap.containsKey(projectName))
        {
            userDataMap.remove(projectName);
            System.out.println("项目删除成功！");
            return true;
        }
        else
        {
            System.out.println("需要删除的项目不存在！");
            return false;
        }
    }
}
