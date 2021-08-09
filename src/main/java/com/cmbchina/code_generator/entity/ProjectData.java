package com.cmbchina.code_generator.entity;

import com.cmbchina.code_generator.utils.DateTimeUtils;
import lombok.Data;

@Data
public class ProjectData {

    private String name;
    private String info;
    private int tableCount;
    private String generateTime;

    public ProjectData(UserData userData)
    {
        name = userData.getConfig().getProjectName();
        info = userData.getConfig().getDescription();
        tableCount = userData.getTableList() == null ? 0 : userData.getTableList().size();
        generateTime = userData.getConfig().getGenerateTime();
    }
}
