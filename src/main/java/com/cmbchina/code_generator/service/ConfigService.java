package com.cmbchina.code_generator.service;

import com.cmbchina.code_generator.entity.Table;

public interface ConfigService {

    /**
     * 别名
     *
     * @author JohnDeng
     * @datatime 2019年1月16日下午5:08:02
     * @return
     */
    String getAliasName(String className, String prefix);


    /**
     * 类名
     *
     * @author JohnDeng
     * @datatime 2019年1月16日下午5:08:24
     * @return
     */
    String getClassName(String tableName, String prefix);

    /**
     * 主键
     *
     * @author JohnDeng
     * @datatime 2019年1月16日下午5:08:35
     * @return
     */
    String getPrimaryKeyDataType(Table table);

    /**
     * 获取实体set方法
     *
     * @author JohnDeng
     * @datatime 2019年1月17日上午10:06:35
     * @param ColumnName
     * @param dataType
     * @return
     */
    String getEntitySetFunction(String ColumnName, String dataType);

    /**
     * 获取实体get方法
     *
     * @author JohnDeng
     * @datatime 2019年1月17日上午10:07:32
     * @param ColumnName
     * @param dataType
     * @return
     */
    String getEntityGetFunction(String ColumnName, String dataType);

    /**
     * 创建属性
     *
     * @author JohnDeng
     * @datatime 2019年1月17日上午11:01:54
     * @param ColumnName
     * @param dataType
     * @return
     */
    String getProperty(String ColumnName, String dataType);

    /**
     * 获取实体数据
     *
     * @author JohnDeng
     * @datatime 2019年1月17日上午11:13:13
     * @param table
     * @return
     */
    String getEntityData(Table table);

    /**
     * 获取属性注释
     * @author JohnDeng
     * @datatime 2019年1月21日下午5:42:56
     * @return
     */
    String  getPropertyComments(String comments);

    String getMapperColumns( Table table, String prefix);

    String getInsertColumns(Table table);

    String getInsertValues(Table table);

    String getUpdateColumns(Table table);

}
