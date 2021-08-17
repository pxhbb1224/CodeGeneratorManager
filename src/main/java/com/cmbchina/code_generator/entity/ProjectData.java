package com.cmbchina.code_generator.entity;

import com.cmbchina.code_generator.utils.DateTimeUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectData {

    private String id;
    private String name;
    private String info;
    private int tableCount;
    private String generateTime;

    public ProjectData(Config config, int tableNum)
    {
        id = config.getProjectId();
        name = config.getProjectName();
        info = config.getDescription();
        tableCount = tableNum;
        generateTime = config.getGenerateTime();
    }
}
