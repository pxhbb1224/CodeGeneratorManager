package com.cmbchina.code_generator.entity;

import com.cmbchina.code_generator.utils.DateTimeUtils;
import lombok.Data;

@Data
public class ProjectData {

    private String name;
    private String info;
    private int tableCount;
    private String generateTime;

    public ProjectData(Config config)
    {
        name = config.getProjectName();
        info = config.getProjectName();
        tableCount = 0;
        generateTime = DateTimeUtils.getDateTime();
    }
}
