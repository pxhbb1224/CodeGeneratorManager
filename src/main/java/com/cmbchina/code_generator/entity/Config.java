package com.cmbchina.code_generator.entity;

import lombok.Data;

@Data
public class Config {

    private String packageName;
    private String authorName;
    private String moduleName;
    private String frontEndPath;
    private String interfaceName;
    private String prefix;
    private int needCovered;
}
