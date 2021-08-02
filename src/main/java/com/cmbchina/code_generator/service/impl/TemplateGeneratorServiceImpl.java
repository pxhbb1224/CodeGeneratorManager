package com.cmbchina.code_generator.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
    private Map<String, Object> replaceMap(Table table, String classDescription) {
        String prefix = "";
        Map<String, Object> map = new HashMap<>();
        map.put("classDescription", classDescription);
        map.put("author", codeGeneratorConfig.getAuthor());
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
    private String getWriteFilePath(String name) {
        if (TemplateCommon.entity.equals(name)) {
            return codeGeneratorConfig.getWriteFileBasePath() + TemplateCommon.entity;
        } else if (TemplateCommon.dao.equals(name)) {
            return codeGeneratorConfig.getWriteFileBasePath() + TemplateCommon.dao;
        } else if (TemplateCommon.service.equals(name)) {
            return codeGeneratorConfig.getWriteFileBasePath() + TemplateCommon.service;
        } else if (TemplateCommon.serviceImpl.equals(name)) {
            return codeGeneratorConfig.getWriteFileBasePath() + TemplateCommon.service + "\\" + TemplateCommon.impl;
        } else if (TemplateCommon.controller.equals(name)) {
            return codeGeneratorConfig.getWriteFileBasePath() + TemplateCommon.controller;
        }else if (TemplateCommon.mapper.equals(name)) {
            return codeGeneratorConfig.getMapperPath() + TemplateCommon.mapper;
        }
        return "";
    }

    /**
     * 获取生成文件名+后缀
     * @author Bin
     * @param name
     * @param tableName
     * @return
     */
    private String getWriteFileName(String name, String tableName) {
        String prefix = "";
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
     * @param tableName
     * @param fileName
     */
    private void fileWrite(String name, String tableName, String fileName) {
        if (TemplateCommon.entity.equals(name)) {
            FileUtils.writeContent(getWriteFilePath(TemplateCommon.entity),
                    getWriteFileName(TemplateCommon.entity, tableName), fileName);
        } else if (TemplateCommon.dao.equals(name)) {
            FileUtils.writeContent(getWriteFilePath(TemplateCommon.dao),
                    getWriteFileName(TemplateCommon.dao, tableName), fileName);
        } else if (TemplateCommon.service.equals(name)) {
            FileUtils.writeContent(getWriteFilePath(TemplateCommon.service),
                    getWriteFileName(TemplateCommon.service, tableName), fileName);
        } else if (TemplateCommon.serviceImpl.equals(name)) {
            FileUtils.writeContent(getWriteFilePath(TemplateCommon.serviceImpl),
                    getWriteFileName(TemplateCommon.serviceImpl, tableName), fileName);
        } else if (TemplateCommon.controller.equals(name)) {
            FileUtils.writeContent(getWriteFilePath(TemplateCommon.controller),
                    getWriteFileName(TemplateCommon.controller, tableName), fileName);
        }else if (TemplateCommon.mapper.equals(name)) {
            FileUtils.writeContent(getWriteFilePath(TemplateCommon.mapper),
                    getWriteFileName(TemplateCommon.mapper, tableName), fileName);
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

        String prefix = "";
        String fileName = FileUtils.readContent(getTemplateFileName(TemplateCommon.entity));

        Map<String, Object> map = replaceMap(table, classDescription);
        map.put("packagePath", getPackagePath(TemplateCommon.entity, config.getPackageName()));
        map.put("alias", configService.getAliasName(table.getTableName(), prefix));
        map.put("table", table.getTableName());
        map.put("entityData", configService.getEntityData(table));

        fileName = ReplaceUtils.replace(fileName, map);

        logger.info("创建实体文本:\n" + fileName);
        fileWrite(TemplateCommon.entity, table.getTableName(), fileName);
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

        Map<String, Object> map = replaceMap(table, classDescription);
        map.put("packagePath", getPackagePath(TemplateCommon.dao, config.getPackageName()));

        fileName = ReplaceUtils.replace(fileName, map);

        logger.info("创建DAO文本:\n" + fileName);
        fileWrite(TemplateCommon.dao, table.getTableName(), fileName);
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

        Map<String, Object> map = replaceMap(table, classDescription);
        map.put("packagePath", getPackagePath(TemplateCommon.service, config.getPackageName()));

        fileName = ReplaceUtils.replace(fileName, map);

        logger.info("创建Service文本:\n" + fileName);
        fileWrite(TemplateCommon.service, table.getTableName(), fileName);
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
        logger.info(">>>>>开始创建ServiceImpl<<<<<");
        String fileName = FileUtils.readContent(getTemplateFileName(TemplateCommon.serviceImpl));

        Map<String, Object> map = replaceMap(table, classDescription);
        map.put("lowerClassName", FormatNameUtils.formatNameCamelCase(configService.getClassName(table.getTableName(), prefix), false));
        map.put("packagePath", getPackagePath(TemplateCommon.serviceImpl, config.getPackageName()));
        map.put("daoPackagePath", getPackagePath(TemplateCommon.dao, config.getPackageName()));
        map.put("servicePackagePath", getPackagePath(TemplateCommon.service, config.getPackageName()));

        fileName = ReplaceUtils.replace(fileName, map);

        logger.info("创建ServiceImpl文本:\n" + fileName);
        fileWrite(TemplateCommon.serviceImpl, table.getTableName(), fileName);
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
        logger.info(">>>>>开始创建controller<<<<<");
        String fileName = FileUtils.readContent(getTemplateFileName(TemplateCommon.controller));

        Map<String, Object> map = replaceMap(table, classDescription);
        map.put("packagePath", getPackagePath(TemplateCommon.controller, config.getPackageName()));
        map.put("servicePackagePath", getPackagePath(TemplateCommon.service, config.getPackageName()));
        map.put("lowerClassName", FormatNameUtils.formatNameCamelCase(configService.getClassName(table.getTableName(), prefix), false));

        fileName = ReplaceUtils.replace(fileName, map);

        logger.info("创建控制器文本:\n" + fileName);
        fileWrite(TemplateCommon.controller, table.getTableName(), fileName);
        logger.info(">>>>>结束创建controller<<<<<");
    }

    /**
     * 创建mybaits-mapper模板
     * @author Bin
     * @param table
     * @param classDescription
     */
    @Override
    public void createMapperTemplate(Table table, Config config, String classDescription)
    {
        String prefix = "";
        logger.info(">>>>>开始创建Mapper<<<<<");
        String fileName = FileUtils.readContent(getTemplateFileName(TemplateCommon.mapper));

        Map<String, Object> map = replaceMap(table, classDescription);
        map.put("tableName", table.getTableName());
        map.put("className", configService.getClassName(table.getTableName(), prefix));
        map.put("alias", FormatNameUtils.formatNameCamelCase(configService.getClassName(table.getTableName(), prefix), false));
        map.put("daoPackagePath",  getPackagePath(TemplateCommon.dao, config.getPackageName()));
        map.put("Columns", configService.getMapperColumns(table, config.getPrefix()));
        map.put("insertColumns", configService.getInsertColumns(table));
        map.put("insertValues", configService.getInsertValues(table));
        map.put("updateColumns", configService.getUpdateColumns(table));

        fileName = ReplaceUtils.replace(fileName, map);

        logger.info("创建mybatis文本:\n:" + fileName);
        fileWrite(TemplateCommon.mapper, table.getTableName(), fileName);
        logger.info(">>>>>结束创建Mapper<<<<<");

    }

    @Override
    public boolean generateCode(UserData userData)
    {
        try
        {
            List<Table> tableList = userData.getTableList();
            Config config = userData.getConfig();
            String classDescription = "系统用户管理";
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
}
