package com.cmbchina.code_generator.utils;

import com.cmbchina.code_generator.entity.Table;
import com.cmbchina.code_generator.entity.Attribute;

import java.util.List;
import java.util.ArrayList;
/**
 * 名称转换工具类
 * @author Bin
 */
public class FormatNameUtils {

    /**
     * 去掉下划线，格式名称，驼峰写法，首字符根据capitalized决定大小写
     * 格式化name User->user UserRole->userRole
     * @param columnName,capitalized
     * @return
     * @author Bin
     */
    public static String formatNameCamelCase(String columnName, boolean capitalized) {
        String arr[] = columnName.split("_");
        columnName = "";
        int index = 0;
        if(!capitalized)
        {
            if(arr[0].length() > 0)
                columnName += arr[0].substring(0, 1).toLowerCase() + arr[0].substring(1);
            index++;
        }
        for (; index < arr.length; index++) {
            if(arr[index].length() > 0)
                columnName += arr[index].substring(0, 1).toUpperCase() + arr[index].substring(1);
        }
        return columnName;
    }

    /**
     * 将数据库表结构转化为创建语句
     * @param table
     * @return String
     * @author Bin
     */
    public static String formatToCreateSql(Table table)
    {
        String res = "create table ";
        List<String> foreignList = new ArrayList<>();
        res += table.getTableName() + "_" + table.getTableId();
        res += "(";
        for(Attribute a: table.getProperties())
        {
            res += a.getName() + " " + a.getType();
            if(a.getLength() > 0)
            {
                res += "(";
                res += String.valueOf(a.getLength());
                if(a.getType().equals("float")|| a.getType().equals("decimal")||a.getType().equals("double"))
                    res += "," + a.getPrecision();
                res += ")";
            }
            if(a.getIsPrimary() == 1)
                res += " " + "primary key";
            else
            {
                if(a.getIsUnique() == 1)
                    res += " " + "unique";
                if(a.getIsNotNull() == 1)
                    res += " " + "not null";
            }
            if(a.getComment() != null && a.getComment().trim().length() > 0)
                res += " " + "comment" + " '" + a.getComment() + "'";
            res += ",";
            /*if(a.getForeignKey().length() > 0)
                foreignList.add(a.getName() + "/" + a.getForeignKey());*/
        }
        for(String str : foreignList)
        {
            String[] temp = str.split("/");
            res += "constraint" + " " + temp[0] + "_" + temp[1].substring(0, temp[1].indexOf(" "))
                    + " foreign key" + "(" + temp[0] + ")" + " references" + " " + temp[1] + ",";
        }
        res  = res.substring(0, res.length() - 1) + ")";
        if(table.getComment() != null && table.getComment().trim().length() > 0)
            res += "comment = " + "'" + table.getComment() + "'";
        return res;
    }

    /**
     * 将数据库表结构转化为删除语句
     * @param tableName
     * @return String
     * @author Bin
     */
    public static String formatToDropSql(String tableName) {
        String res = "";
        if (tableName != null && tableName.trim().length() > 0) {
            res += "drop table if exists" + " " + tableName;

        }
        return res;
    }
}
