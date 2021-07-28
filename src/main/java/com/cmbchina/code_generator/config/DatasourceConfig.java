package com.cmbchina.code_generator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

/**
 * 数据库配置信息
 * @author Bin
 */
@Component
@ConfigurationProperties(prefix = "spring.datasource")
@Data
public class DatasourceConfig {
    private String url;
    private String userName;
    private String password;

}
