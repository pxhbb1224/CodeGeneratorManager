package com.cmbchina.code_generator.utils;

import com.cmbchina.code_generator.model.Table;
import com.cmbchina.code_generator.model.Attribute;

import java.util.List;
import java.util.ArrayList;
/**
 * 名称转换工具类
 * @author Bin
 */
public class FormatNameUtils {

    /**
     * 去掉下划线，格式名称，驼峰写法，首字符小写
     * 格式化name User->user UserRole->userRole
     * @param ColumnName
     * @return
     * @author Bin
     */
    public static String formatNameCamelbak(String ColumnName) {
        String arr[] = ColumnName.split("_");
        ColumnName = "";
        ColumnName += arr[0].substring(0, 1).toLowerCase() + arr[0].substring(1);
        for (int i = 1; i < arr.length; i++) {
            ColumnName += arr[i].substring(0, 1).toUpperCase() + arr[i].substring(1);
        }
        return ColumnName;
    }

    /**
     * 去掉下划线，格式名称，驼峰写法，首字符小写
     * 格式化name User->user UserRole->userRole
     * @param table
     * @return String
     * @author Bin
     */
    public static String formatToSql(Table table)
    {
        int length = table.getAttributeNum();
        String res = "create table ";
        List<String> foreignList = new ArrayList<>();
        res += table.getTableName();
        res += "(";
        for(Attribute a: table.getTable())
        {
            res += a.getNameId() + " " + a.getTypeName();
            if(a.getLength().size() > 0)
            {
                res += "(";
                for(int i : a.getLength())
                {
                    res += String.valueOf(i) + ",";
                }
                res = res.substring(0, res.length() - 1) + ")" ;
            }
            if(a.isPrimary())
                res += " " + "primary key";
            else
            {
                if(a.isUnique())
                    res += " " + "unique";
                if(a.isNotNull())
                    res += " " + "not null";
            }

            res += ",";
            if(a.getForeignKey().length() > 0)
                foreignList.add(a.getNameId() + "/" + a.getForeignKey());
        }
        for(String str : foreignList)
        {
            String[] temp = str.split("/");
            res += "constraint" + " " + temp[0] + "_" + temp[1].substring(0, temp[1].indexOf(" "))
                    + " foreign key" + "(" + temp[0] + ")" + " references" + " " + temp[1] + ",";
        }
        res  = res.substring(0, res.length() - 1) + ")";
        return res;
    }
}
