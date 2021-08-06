package com.cmbchina.code_generator.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.cmbchina.code_generator.common.TemplateCommon;
import com.cmbchina.code_generator.config.GlobalConfig;
import com.cmbchina.code_generator.entity.Config;
import com.cmbchina.code_generator.entity.Table;
import com.cmbchina.code_generator.entity.UserData;
import com.cmbchina.code_generator.service.TemplateGeneratorService;
import com.cmbchina.code_generator.service.ConfigService;
import com.cmbchina.code_generator.utils.DateTimeUtils;
import com.cmbchina.code_generator.utils.FileUtils;
import com.cmbchina.code_generator.utils.FormatNameUtils;
import com.cmbchina.code_generator.utils.ReplaceUtils;
import com.cmbchina.code_generator.utils.ZipUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("TemplateService")
public class TemplateGeneratorServiceImpl implements TemplateGeneratorService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GlobalConfig codeGeneratorConfig;
    @Resource
    private ConfigService configService;
    /**
     * 通用的替换
     * @author Bin
     * @param table
     * @param classDescription
     * @return
     */
    private Map<String, Object> replaceMap(Table table, Config config, String classDescription) {
        String prefix = config.getPrefix();
        Map<String, Object> map = new HashMap<>();
        map.put("classDescription", classDescription);
        map.put("author", config.getAuthorName());
        map.put("dateTime", DateTimeUtils.getDateTime());
        map.put("className", configService.getClassName(table.getTableName(), prefix));
        map.put("primaryKey", configService.getPrimaryKeyDataType(table));
        return map;
    }

    /**
     * 获取包路径
     *
     * @author Bin
     * @param name
     * @return
     */
    private String getPackagePath(String name,String packageName) {
        if (TemplateCommon.entity.equals(name)) {
            return packageName + "." + TemplateCommon.entity;
        } else if (TemplateCommon.dao.equals(name)) {
            return packageName + "." + TemplateCommon.dao;
        } else if (TemplateCommon.service.equals(name)) {
            return packageName + "." + TemplateCommon.service;
        } else if (TemplateCommon.serviceImpl.equals(name)) {
            return packageName + "." + TemplateCommon.service + "." + TemplateCommon.impl;
        } else if (TemplateCommon.controller.equals(name)) {
            return packageName + "." + TemplateCommon.controller;
        }
        return "";
    }

    /**
     * 获取模板路径和名称
     * @author Bin
     * @param name
     * @return
     */
    private String getTemplateFileName(String name) {
        if (TemplateCommon.entity.equals(name)) {
            return codeGeneratorConfig.getTemplateBasePath() + TemplateCommon.defaultEntityTemplateFileName;
        } else if (TemplateCommon.dao.equals(name)) {
            return codeGeneratorConfig.getTemplateBasePath() + TemplateCommon.defaultDaoTemplateFileName;
        } else if (TemplateCommon.service.equals(name)) {
            return codeGeneratorConfig.getTemplateBasePath() + TemplateCommon.defaultServiceTemplateFileName;
        } else if (TemplateCommon.serviceImpl.equals(name)) {
            return codeGeneratorConfig.getTemplateBasePath() + TemplateCommon.defaultServiceImplTemplateFileName;
        } else if (TemplateCommon.controller.equals(name)) {
            return codeGeneratorConfig.getTemplateBasePath() + TemplateCommon.defaultControllerTemplateFileName;
        } else if (TemplateCommon.mapper.equals(name)) {
            return codeGeneratorConfig.getTemplateBasePath() + TemplateCommon.defaultMapperTemplateFileName;
        }
        return "";
    }

    /**
     * 获取生成路径
     * @author Bin
     * @param name
     * @return
     */
    private String getWriteFilePath(String name, String projectName, String tableName) {
        if (TemplateCommon.entity.equals(name)) {
            return codeGeneratorConfig.getWriteFileBasePath() + projectName + "\\" + tableName + "\\" + TemplateCommon.entity;
        } else if (TemplateCommon.dao.equals(name)) {
            return codeGeneratorConfig.getWriteFileBasePath() + projectName + "\\" + tableName + "\\" + TemplateCommon.dao;
        } else if (TemplateCommon.service.equals(name)) {
            return codeGeneratorConfig.getWriteFileBasePath()  + projectName + "\\" + tableName + "\\" + TemplateCommon.service;
        } else if (TemplateCommon.serviceImpl.equals(name)) {
            return codeGeneratorConfig.getWriteFileBasePath() + projectName + "\\" + tableName + "\\" + TemplateCommon.service + "\\" + TemplateCommon.impl;
        } else if (TemplateCommon.controller.equals(name)) {
            return codeGeneratorConfig.getWriteFileBasePath() + projectName + "\\" + tableName + "\\"  + TemplateCommon.controller;
        }else if (TemplateCommon.mapper.equals(name)) {
            return codeGeneratorConfig.getMapperPath() + projectName + "\\" + tableName + "\\" + TemplateCommon.mapper;
        }
        return "";
    }

    /**
     * 获取生成文件名+后缀
     * @author Bin
     * @param name
     * @param prefix
     * @param tableName
     * @return
     */
    private String getWriteFileName(String name, String prefix, String tableName) { ;
        if (TemplateCommon.entity.equals(name)) {
            return configService.getClassName(tableName, prefix) + TemplateCommon.javaEntitySuffix;
        } else if (TemplateCommon.dao.equals(name)) {
            return configService.getClassName(tableName, prefix) + TemplateCommon.javaDaoSuffix;
        } else if (TemplateCommon.service.equals(name)) {
            return configService.getClassName(tableName, prefix) + TemplateCommon.javaServiceSuffix;
        } else if (TemplateCommon.serviceImpl.equals(name)) {
            return configService.getClassName(tableName, prefix) + TemplateCommon.javaServiceImplSuffix;
        } else if (TemplateCommon.controller.equals(name)) {
            return configService.getClassName(tableName, prefix) + TemplateCommon.javaControllerImplSuffix;
        }else if (TemplateCommon.mapper.equals(name)) {
            return configService.getClassName(tableName, prefix) + TemplateCommon.xmlMapperImplSuffix;
        }
        return "";
    }

    /**
     * 生成文件
     * @author Bin
     * @param name
     * @param prefix
     * @param tableName
     * @param fileName
     */
    private void fileWrite(String name, String prefix, String tableName, String fileName, String projectName) {
        if (TemplateCommon.entity.equals(name)) {
            FileUtils.writeContent(getWriteFilePath(TemplateCommon.entity, projectName, tableName),
                    getWriteFileName(TemplateCommon.entity, prefix, tableName), fileName);
        } else if (TemplateCommon.dao.equals(name)) {
            FileUtils.writeContent(getWriteFilePath(TemplateCommon.dao, projectName, tableName),
                    getWriteFileName(TemplateCommon.dao, prefix, tableName), fileName);
        } else if (TemplateCommon.service.equals(name)) {
            FileUtils.writeContent(getWriteFilePath(TemplateCommon.service, projectName, tableName),
                    getWriteFileName(TemplateCommon.service, prefix, tableName), fileName);
        } else if (TemplateCommon.serviceImpl.equals(name)) {
            FileUtils.writeContent(getWriteFilePath(TemplateCommon.serviceImpl, projectName, tableName),
                    getWriteFileName(TemplateCommon.serviceImpl, prefix, tableName), fileName);
        } else if (TemplateCommon.controller.equals(name)) {
            FileUtils.writeContent(getWriteFilePath(TemplateCommon.controller, projectName, tableName),
                    getWriteFileName(TemplateCommon.controller, prefix, tableName), fileName);
        }else if (TemplateCommon.mapper.equals(name)) {
            FileUtils.writeContent(getWriteFilePath(TemplateCommon.mapper, projectName, tableName),
                    getWriteFileName(TemplateCommon.mapper, prefix, tableName), fileName);
        }

    }

    /**
     * 创建实体模板
     * @author Bin
     * @param table
     * @param classDescription
     */
    @Override
    public void createEntityTemplate(Table table, Config config, String classDescription)
    {
        logger.info(">>>>>开始创建实体<<<<<");

        String prefix = config.getPrefix();
        String tableName = table.getTableName();
        String fileName = FileUtils.readContent(getTemplateFileName(TemplateCommon.entity));

        Map<String, Object> map = replaceMap(table, config, classDescription);
        map.put("packagePath", getPackagePath(TemplateCommon.entity, config.getPackageName()));
        map.put("alias", configService.getAliasName(tableName, prefix));
        map.put("table", tableName);
        map.put("entityData", configService.getEntityData(table));

        fileName = ReplaceUtils.replace(fileName, map);

        logger.info("创建实体文本:\n" + fileName);
        fileWrite(TemplateCommon.entity, prefix, tableName, fileName, config.getProjectName());
        logger.info(">>>>>结束创建实体<<<<<");
    }

    /**
     * 创建DAO模板
     * @author Bin
     * @param table
     * @param classDescription
     */
    @Override
    public void createDaoTemplate(Table table, Config config, String classDescription)
    {
        logger.info(">>>>>开始创建DAO<<<<<");

        String fileName = FileUtils.readContent(getTemplateFileName(TemplateCommon.dao));

        Map<String, Object> map = replaceMap(table, config, classDescription);
        map.put("packagePath", getPackagePath(TemplateCommon.dao, config.getPackageName()));

        fileName = ReplaceUtils.replace(fileName, map);

        logger.info("创建DAO文本:\n" + fileName);
        fileWrite(TemplateCommon.dao, config.getPrefix(), table.getTableName(), fileName, config.getProjectName());
        logger.info(">>>>>结束创建DAO<<<<<");

    }

    /**
     * 创建service模板
     * @author Bin
     * @param table
     * @param classDescription
     */
    @Override
    public void createServiceTemplate(Table table, Config config, String classDescription)
    {
        logger.info(">>>>>开始创建Service<<<<<");

        String fileName = FileUtils.readContent(getTemplateFileName(TemplateCommon.service));

        Map<String, Object> map = replaceMap(table, config, classDescription);
        map.put("packagePath", getPackagePath(TemplateCommon.service, config.getPackageName()));

        fileName = ReplaceUtils.replace(fileName, map);

        logger.info("创建Service文本:\n" + fileName);
        fileWrite(TemplateCommon.service, config.getPrefix(), table.getTableName(), fileName, config.getProjectName());
        logger.info(">>>>>结束创建Service<<<<<");
    }

    /**
     * 创建service实现类模板
     * @author Bin
     * @param table
     * @param classDescription
     */
    @Override
    public void createServiceImplTemplate(Table table, Config config, String classDescription)
    {
        String prefix = config.getPrefix();
        String tableName = table.getTableName();
        logger.info(">>>>>开始创建ServiceImpl<<<<<");
        String fileName = FileUtils.readContent(getTemplateFileName(TemplateCommon.serviceImpl));

        Map<String, Object> map = replaceMap(table, config, classDescription);
        map.put("lowerClassName", FormatNameUtils.formatNameCamelCase(configService.getClassName(tableName, prefix), false));
        map.put("packagePath", getPackagePath(TemplateCommon.serviceImpl, config.getPackageName()));
        map.put("daoPackagePath", getPackagePath(TemplateCommon.dao, config.getPackageName()));
        map.put("servicePackagePath", getPackagePath(TemplateCommon.service, config.getPackageName()));

        fileName = ReplaceUtils.replace(fileName, map);

        logger.info("创建ServiceImpl文本:\n" + fileName);
        fileWrite(TemplateCommon.serviceImpl, prefix, tableName, fileName, config.getProjectName());
        logger.info(">>>>>结束创建ServiceImpl<<<<<");

    }

    /**
     * 创建控制器模板
     * @author Bin
     * @param table
     * @param classDescription
     */
    @Override
    public void createControllerTemplate(Table table, Config config, String classDescription)
    {
        String prefix = config.getPrefix();
        String tableName = table.getTableName();
        logger.info(">>>>>开始创建controller<<<<<");
        String fileName = FileUtils.readContent(getTemplateFileName(TemplateCommon.controller));

        Map<String, Object> map = replaceMap(table, config, classDescription);
        map.put("packagePath", getPackagePath(TemplateCommon.controller, config.getPackageName()));
        map.put("servicePackagePath", getPackagePath(TemplateCommon.service, config.getPackageName()));
        map.put("lowerClassName", FormatNameUtils.formatNameCamelCase(configService.getClassName(tableName, prefix), false));

        fileName = ReplaceUtils.replace(fileName, map);

        logger.info("创建控制器文本:\n" + fileName);
        fileWrite(TemplateCommon.controller, prefix, tableName, fileName, config.getProjectName());
        logger.info(">>>>>结束创建controller<<<<<");
    }

    /**
     * 创建mybatis-mapper模板
     * @author Bin
     * @param table
     * @param classDescription
     */
    @Override
    public void createMapperTemplate(Table table, Config config, String classDescription)
    {
        String prefix = config.getPrefix();
        String tableName = table.getTableName();
        logger.info(">>>>>开始创建Mapper<<<<<");
        String fileName = FileUtils.readContent(getTemplateFileName(TemplateCommon.mapper));

        Map<String, Object> map = replaceMap(table, config, classDescription);
        map.put("tableName", tableName);
        map.put("className", configService.getClassName(tableName, prefix));
        map.put("alias", FormatNameUtils.formatNameCamelCase(configService.getClassName(tableName, prefix), false));
        map.put("daoPackagePath",  getPackagePath(TemplateCommon.dao, config.getPackageName()));
        map.put("primaryKey", configService.getPrimaryKeyName(table));
        map.put("Columns", configService.getMapperColumns(table, prefix));
        map.put("insertColumns", configService.getInsertColumns(table));
        map.put("insertValues", configService.getInsertValues(table));
        map.put("updateColumns", configService.getUpdateColumns(table));

        fileName = ReplaceUtils.replace(fileName, map);

        logger.info("创建mybatis文本:\n:" + fileName);
        fileWrite(TemplateCommon.mapper, prefix, tableName, fileName, config.getProjectName());
        logger.info(">>>>>结束创建Mapper<<<<<");

    }

    /**
    * 生成代码
    * @author Bin
    * @param userData
    */
    @Override
    public boolean generateCode(UserData userData)
    {
        try
        {
            List<Table> tableList = userData.getTableList();
            Config config = userData.getConfig();
            String classDescription = config.getDescription();
            for(Table table : tableList)
            {

                createEntityTemplate(table, config, classDescription);
                createDaoTemplate(table, config, classDescription);
                createServiceTemplate(table, config, classDescription);
                createServiceImplTemplate(table, config, classDescription);
                createControllerTemplate(table, config, classDescription);
                createMapperTemplate(table, config, classDescription);
            }
            return true;
        }catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 文件下载
     * @param projectName 项目名
     * @return
     * @throws FileNotFoundException
     */
    @Override
    public void downloadCode(String projectName, HttpServletResponse response) throws IOException {
        ZipUtils.zip(codeGeneratorConfig.getWriteFileBasePath(), projectName);
        new FileUtils().downloadFile(codeGeneratorConfig.getWriteFileBasePath(), projectName+".zip", response);
    }
}
