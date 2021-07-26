package com.cmbchina.code_generator.utils;

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
}
