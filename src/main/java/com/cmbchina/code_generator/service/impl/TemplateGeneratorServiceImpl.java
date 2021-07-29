package com.cmbchina.code_generator.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cmbchina.code_generator.common.TemplateCommon;
import com.cmbchina.code_generator.config.GlobalConfig;
import com.cmbchina.code_generator.service.TemplateGeneratorService;
import com.cmbchina.code_generator.utils.DateTimeUtils;
import com.cmbchina.code_generator.utils.FileUtils;
import com.cmbchina.code_generator.utils.FormatNameUtils;
import com.cmbchina.code_generator.utils.ReplaceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

public class TemplateGeneratorServiceImpl implements TemplateGeneratorService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GlobalConfig codeGeneratorConfig;

    /**
     * 通用的替换
     *
     * @author Bin
     * @param tableName
     * @param classDescription
     * @return
     */
    private Map<String, Object> replaceMap(String tableName, String classDescription) {
        Map<String, Object> map = new HashMap<>();
        map.put("classDescription", classDescription);
        map.put("author", codeGeneratorConfig.getAuthor());
        map.put("datatime", DateTimeUtils.getDateTime());
        //map.put("className", tableColumnsService.getClassName(tableName));
        //map.put("primaryKey", tableColumnsService.getPrimaryKeyDataType(tableName));
        return map;
    }

    /**
     * 获取包路径
     *
     * @author Bin
     * @param name
     * @return
     */
    private String getPackgePath(String name) {
        if (TemplateCommon.entity.equals(name)) {
            return codeGeneratorConfig.getPackgePath() + "." + TemplateCommon.entity;
        } else if (TemplateCommon.dao.equals(name)) {
            return codeGeneratorConfig.getPackgePath() + "." + TemplateCommon.dao;
        } else if (TemplateCommon.service.equals(name)) {
            return codeGeneratorConfig.getPackgePath() + "." + TemplateCommon.service;
        } else if (TemplateCommon.serviceImpl.equals(name)) {
            return codeGeneratorConfig.getPackgePath() + "." + TemplateCommon.service + "." + TemplateCommon.impl;
        } else if (TemplateCommon.controller.equals(name)) {
            return codeGeneratorConfig.getPackgePath() + "." + TemplateCommon.controller;
        }
        return "";
    }

    /**
     * 获取模板路径和名称
     *
     * @author JohnDeng
     * @datatime 2019年5月26日下午11:12:02
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
     *
     * @author JohnDeng
     * @dateTime 2019年5月30日下午5:35:15
     * @param name
     * @param tableName
     * @return
     */
    /*private String getWriteFileName(String name, String tableName) {
        if (TemplateCommon.entity.equals(name)) {
            return tableColumnsService.getClassName(tableName) + TemplateCommon.javaEntitySuffix;
        } else if (TemplateCommon.dao.equals(name)) {
            return tableColumnsService.getClassName(tableName) + TemplateCommon.javaDaoSuffix;
        } else if (TemplateCommon.service.equals(name)) {
            return tableColumnsService.getClassName(tableName) + TemplateCommon.javaServiceSuffix;
        } else if (TemplateCommon.serviceImpl.equals(name)) {
            return tableColumnsService.getClassName(tableName) + TemplateCommon.javaServiceImplSuffix;
        } else if (TemplateCommon.controller.equals(name)) {
            return tableColumnsService.getClassName(tableName) + TemplateCommon.javaControllerImplSuffix;
        }else if (TemplateCommon.mapper.equals(name)) {
            return tableColumnsService.getClassName(tableName) + TemplateCommon.xmlMapperImplSuffix;
        }
        return "";
    }*/

    /**
     * 生成文件
     * @author Bin
     * @param name
     * @param tableName
     * @param fileName
     */
    /*private void fileWrite(String name, String tableName, String fileName) {
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

    }*/

    /**
     * 创建实体模板
     * @author Bin
     * @param tableName
     * @param classDescription
     */
    @Override
    public void createEntityTemplate(String tableName, String classDescription)
    {

    }

    /**
     * 创建DAO模板
     * @author Bin
     * @param tableName
     * @param classDescription
     */
    @Override
    public void createDaoTemplate(String tableName, String classDescription)
    {

    }

    /**
     * 创建service模板
     * @author Bin
     * @param tableName
     * @param classDescription
     */
    @Override
    public void createServiceTemplate(String tableName, String classDescription)
    {

    }

    /**
     * 创建service实现类模板
     * @author Bin
     * @param tableName
     * @param classDescription
     */
    @Override
    public void createServiceImplTemplate(String tableName, String classDescription)
    {

    }

    /**
     * 创建控制器模板
     * @author Bin
     * @param tableName
     * @param classDescription
     */
    @Override
    public void createControllerTemplate(String tableName, String classDescription)
    {

    }

    /**
     * 创建mybaits-mapper模板
     * @author Bin
     * @param tableName
     * @param classDescription
     */
    @Override
    public void createMapperTemplate(String tableName, String classDescription)
    {

    }

}
