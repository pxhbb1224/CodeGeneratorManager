package com.cmbchina.code_generator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

/**
 * 全局配置
 * @author Bin
 */
@Component
@ConfigurationProperties(prefix = "codegenerator.global")
@Data
public class GlobalConfig {
    /**
     * 作者
     */
    public String author;
    /**
     * 包路径
     */
    private String packagePath;
    /**
     * 读取模板总路径
     */
    private String templateBasePath;

    /**
     * 输出文件总路径
     */
    private String writeFileBasePath;

    /**
     * 数据库表名前缀
     */
    private String tablePrefix;

    /**
     * mybatis生成的路径
     */

    private String  mapperPath;
}
