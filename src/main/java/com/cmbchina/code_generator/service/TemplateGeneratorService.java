package com.cmbchina.code_generator.service;

import com.cmbchina.code_generator.entity.Config;
import com.cmbchina.code_generator.entity.Table;
import com.cmbchina.code_generator.entity.Relation;
import com.cmbchina.code_generator.entity.UserData;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public interface TemplateGeneratorService {

    /**
     * 创建实体模板
     * @author Bin
     * @param table
     * @param config
     * @param classDescription
     */
    void createEntityTemplate(Table table, Config config, String classDescription);
    /**
     * 创建DAO模板
     * @author Bin
     * @param table
     * @param config
     * @param classDescription
     */
    void createDaoTemplate(Table table, Config config,String classDescription);

    /**
     * 创建service模板
     * @author Bin
     * @param table
     * @param config
     * @param classDescription
     */
    void createServiceTemplate(Table table, Config config,String classDescription);

    /**
     * 创建service实现类模板
     * @author Bin
     * @param table
     * @param config
     * @param classDescription
     */
    void createServiceImplTemplate(Table table, Config config, String classDescription) ;

    /**
     * 创建控制器模板
     * @author Bin
     * @param table
     * @param config
     * @param classDescription
     */
    void createControllerTemplate(Table table, Config config, String classDescription);

    /**
     * 创建mybaits-mapper模板
     * @author Bin
     * @param table
     * @param config
     * @param classDescription
     */
    void createMapperTemplate(Table table, Config config, String classDescription);

    /**
     * 生成代码
     * @param userData
     * @return
     */
    boolean generateCode(UserData userData);

    /**
     * 下载代码
     * @param projectName 项目名
     * @return
     */
    void downloadCode(String projectName, HttpServletResponse response) throws IOException;
}
