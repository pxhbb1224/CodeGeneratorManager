package com.cmbchina.code_generator.service;

import com.cmbchina.code_generator.entity.Table;

public interface ConfigService {

    /**
     * 别名
     * @author Bin
     * @return
     */
    String getAliasName(String className, String prefix);


    /**
     * 类名
     * @author Bin
     * @return
     */
    String getClassName(String tableName, String prefix);

    /**
     * 主键类型
     * @author Bin
     * @return
     */
    String getPrimaryKeyDataType(Table table);

    /**
     * 主键名
     * @author Bin
     * @return
     */
    String getPrimaryKeyName(Table table);

    /**
     * 获取实体set方法
     * @author Bin
     * @param ColumnName
     * @param dataType
     * @return
     */
    String getEntitySetFunction(String ColumnName, String dataType);

    /**
     * 获取实体get方法
     * @author Bin
     * @param ColumnName
     * @param dataType
     * @return
     */
    String getEntityGetFunction(String ColumnName, String dataType);

    /**
     * 创建属性
     * @author Bin
     * @param ColumnName
     * @param dataType
     * @return
     */
    String getProperty(String ColumnName, String dataType);

    /**
     * 获取实体数据
     * @author Bin
     * @param table
     * @return
     */
    String getEntityData(Table table);

    /**
     * 获取属性注释
     * @author Bin
     * @return
     */
    String  getPropertyComments(String comments);

    String getMapperColumns(Table table, String prefix);

    String getInsertColumns(Table table);

    String getInsertValues(Table table);

    String getUpdateColumns(Table table);

}
