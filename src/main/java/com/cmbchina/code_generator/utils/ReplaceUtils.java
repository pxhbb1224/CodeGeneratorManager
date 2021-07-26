package com.cmbchina.code_generator.utils;

import java.util.Map;

/**
 * 替换工具类
 * @author JohnDeng
 * @dateTime 2019年5月30日下午5:14:45
 */
public class ReplaceUtils {

    /**
     * 根据文本，循环替换 key,value
     * @author Bin
     * @param text
     * @param map
     * @return
     */
    public static String replace(String text, Map<String, Object> map) {
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                text = text.replace("${" + entry.getKey() + "}", String.valueOf(entry.getValue()));
            }
        }
        return text;
    }

}
