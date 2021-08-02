package com.cmbchina.code_generator.service.impl;

import java.util.List;

import javax.annotation.Resource;
import com.cmbchina.code_generator.config.GlobalConfig;
import com.cmbchina.code_generator.entity.Attribute;
import com.cmbchina.code_generator.entity.Table;
import com.cmbchina.code_generator.enums.DataTypeEnum;

import com.cmbchina.code_generator.service.ConfigService;
import com.cmbchina.code_generator.utils.FormatNameUtils;
import org.springframework.stereotype.Service;

@Service
public class ConfigServiceImpl implements ConfigService{

    @Resource
    public GlobalConfig jdbcTableConfig;

    @Override
    public String getAliasName(String tableName, String prefix)
    {
        if (tableName != null && tableName.trim().length() > 0) {
            if(prefix != null && prefix.trim().length() > 0){
                if (tableName.toLowerCase().matches("^" + prefix.toLowerCase() + ".*"))
                {
                    tableName = tableName.substring(prefix.length());//截取前缀后面的字符串
                }
                return FormatNameUtils.formatNameCamelCase(tableName, false);
            }
            return FormatNameUtils.formatNameCamelCase(tableName, false);
        }
        return "";
    }

    @Override
    public String getClassName(String tableName, String prefix) {
        if (tableName != null&&tableName.trim().length() > 0) {
            if(prefix != null&&prefix.trim().length() > 0){
                if (tableName.toLowerCase().matches("^" + prefix.toLowerCase() + ".*"))
                {
                    tableName = tableName.substring(prefix.length());//截取前缀后面的字符串
                }
                return FormatNameUtils.formatNameCamelCase(tableName, true);
            }
            return FormatNameUtils.formatNameCamelCase(tableName, true);
        }
        return "";
    }

    @Override
    public String getPrimaryKeyDataType(Table table)
    {
        List<Attribute> list = table.getProperties();
        if (list != null&&list.size() > 0) {
            for (Attribute a : list) {
                boolean isPK = a.getIsPrimary() == 1 ? true : false;
                if (isPK) {
                    return DataTypeEnum.getJavaDataTypeByMysqlDataType(a.getType());
                }
            }
        }
        return null;
    }

    @Override
    public String getEntityData(Table table) {
        List<Attribute> list = table.getProperties();
        StringBuilder propertyString = new StringBuilder();
        StringBuilder functionString = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (Attribute a : list) {
                propertyString.append(getPropertyComments(a.getComment()));
                propertyString.append(getProperty(a.getName(), a.getType()));
                functionString.append(getEntitySetFunction(a.getName(), a.getType()));
                functionString.append(getEntityGetFunction(a.getName(), a.getType()));
            }
            propertyString.append(functionString.toString());
        }
        return propertyString.toString();
    }


    @Override
    public String getEntitySetFunction(String columnName, String dataType) {
        StringBuilder sb = new StringBuilder();
        if (columnName != null && columnName.length() > 0 && dataType != null && dataType.length() > 0) {
            sb.append("    public void set");
            sb.append(FormatNameUtils.formatNameCamelCase(columnName, true));
            sb.append("(");
            sb.append(DataTypeEnum.getJavaDataTypeByMysqlDataType(dataType));
            sb.append(" ");
            sb.append(FormatNameUtils.formatNameCamelCase(columnName, false));
            sb.append(") {\n ");
            sb.append("    	this.");
            sb.append(FormatNameUtils.formatNameCamelCase(columnName, false));
            sb.append(" = ");
            sb.append(FormatNameUtils.formatNameCamelCase(columnName, false));
            sb.append(";\n");
            sb.append("     }\n\n ");

        }
        return sb.toString();
    }

    @Override
    public String getEntityGetFunction(String columnName, String dataType) {
        StringBuilder sb = new StringBuilder();
        if (columnName != null && columnName.length() > 0 && dataType != null && dataType.length() > 0) {
            sb.append("    public ");
            sb.append(DataTypeEnum.getJavaDataTypeByMysqlDataType(dataType));
            sb.append(" get");
            sb.append(FormatNameUtils.formatNameCamelCase(columnName, true));
            sb.append("() { \n");
            sb.append("        return ");
            sb.append(FormatNameUtils.formatNameCamelCase(columnName, false));
            sb.append(";\n");
            sb.append("     }\n\n ");
        }
        return sb.toString();
    }

    @Override
    public String getProperty(String columnName, String dataType) {
        StringBuilder sb = new StringBuilder();
        if (columnName != null && columnName.length() > 0 && dataType != null && dataType.length() > 0)
        {
            sb.append("    private ");
            sb.append(DataTypeEnum.getJavaDataTypeByMysqlDataType(dataType));
            sb.append(" ");
            sb.append(FormatNameUtils.formatNameCamelCase(columnName, false));
            sb.append(";\n");
        }
        return sb.toString();
    }

    @Override
    public String getPropertyComments(String comments) {
        return "    /**\n" +
                "     *" +
                "     " + comments + "\n" +
                "     */\n";
    }


    @Override
    public String getMapperColumns(Table table, String prefix)
    {
        StringBuilder columns = new StringBuilder();
        boolean flag = false;
        for (Attribute a : table.getProperties()) {
            if (flag) {
                columns.append(",\n");
            }
            columns.append("		");
            columns.append(getAliasName(table.getTableName(), prefix));
            columns.append(".");
            columns.append(a.getName());
            columns.append(" AS ");
            columns.append(FormatNameUtils.formatNameCamelCase(a.getName(), false));
            flag = true;
        }
        return columns.toString();
    }

    @Override
    public String getInsertColumns(Table table)
    {
        StringBuilder insertColumns = new StringBuilder();
        boolean flag = false;
        for (Attribute a : table.getProperties()) {
            if (flag) {
                insertColumns.append(",\n");
            }
            insertColumns.append("			");
            insertColumns.append(a.getName());
            flag = true;
        }
        return insertColumns.toString();
    }

    @Override
    public String getInsertValues(Table table)
    {
        StringBuilder insertValues = new StringBuilder();
        boolean flag = false;
        for (Attribute a : table.getProperties()) {
            if (flag) {
                insertValues.append(",\n");
            }
            insertValues.append("			");
            insertValues.append("#{");
            insertValues.append(FormatNameUtils.formatNameCamelCase(a.getName(), false));
            insertValues.append("}");
            flag = true;
        }
        return insertValues.toString();
    }

    @Override
    public String getUpdateColumns(Table table)
    {
        StringBuilder updateValues = new StringBuilder();
        boolean flag = false;

        for (Attribute a : table.getProperties()) {
            if (flag) {
                updateValues.append("\n");
            }
            updateValues.append("			");
            updateValues.append("<if test=\"" + FormatNameUtils.formatNameCamelCase(a.getName(), false) + "!=null\">");
            updateValues.append(a.getName());
            updateValues.append("=#{");
            updateValues.append(FormatNameUtils.formatNameCamelCase(a.getName(), false));
            updateValues.append("},</if>");
            flag = true;
        }
        return updateValues.toString();
    }
}
