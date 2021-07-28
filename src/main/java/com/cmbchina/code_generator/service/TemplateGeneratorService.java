package com.cmbchina.code_generator.service;

public interface TemplateGeneratorService {
    /**
     * 创建实体模板
     * @author Bin
     * @param tableName
     * @param classDescription
     */
    void createEntityTemplate(String tableName, String classDescription);
    /**
     * 创建DAO模板
     * @author Bin
     * @param tableName
     * @param classDescription
     */
    void createDaoTemplate(String tableName, String classDescription);

    /**
     * 创建service模板
     * @author Bin
     * @param tableName
     * @param classDescription
     */
    void createServiceTemplate(String tableName, String classDescription);

    /**
     * 创建service实现类模板
     * @author Bin
     * @param tableName
     * @param classDescription
     */
    void createServiceImplTemplate(String tableName, String classDescription) ;

    /**
     * 创建控制器模板
     * @author Bin
     * @param tableName
     * @param classDescription
     */
    void createControllerTemplate(String tableName, String classDescription);

    /**
     * 创建mybaits-mapper模板
     * @author Bin
     * @param tableName
     * @param classDescription
     */
    void createMapperTemplate(String tableName, String classDescription);

}
