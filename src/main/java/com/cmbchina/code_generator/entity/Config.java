package com.cmbchina.code_generator.entity;

import lombok.Data;

/**
 * 存储一个项目的配置信息
 */
@Data
public class Config {

    private String packageName; //包名
    private String authorName; //作者名
    //private String moduleName;
    //private String frontEndPath;
    //private String interfaceName;
    private String prefix; //前缀
    private int needCovered; //是否覆盖
    private String description; //项目描述
    private String projectName; //项目名
}
