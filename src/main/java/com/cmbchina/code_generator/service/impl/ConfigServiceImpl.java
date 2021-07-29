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
        if (tableName != null&&tableName.length() > 0&&tableName.trim().length() > 0) {
            if(prefix != null&&prefix.length() > 0&&prefix.trim().length() > 0){
                String s="";
                String [] splits=tableName.split(prefix);
                for(int i=1;i<splits.length;i++){
                    s+=splits[i];
                }
                return FormatNameUtils.formatNameCamelCase(s, false);
            }
        }
        return "";
    }

    @Override
    public String getClassName(String tableName, String prefix) {
        if (prefix != null&&prefix.length() > 0&&prefix.trim().length() > 0) {
            tableName = tableName.replace(prefix, "");
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
    public String getEntityData(String tableName) {

    }
}
