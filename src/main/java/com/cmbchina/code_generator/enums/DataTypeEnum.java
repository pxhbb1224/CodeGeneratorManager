package com.cmbchina.code_generator.enums;

/**
 * 数据类型枚举
 * @author Bin
 */
public enum DataTypeEnum {
    /**
     * 字符串
     */
    CHAR("char","String"),
    /**
     * 字符串
     */
    VARCHAR("varchar","String"),
    /**
     *
     */
    BLOB("blob","byte[]"),
    /**
     *
     */
    TEXT("text","String"),
    /**
     *
     */
    INTEGER("integer","Long"),
    /**
     *
     */
    TINYINT("tinyint","Integer"),
    /**
     *
     */
    SMALLINT("varchar","Integer"),
    /**
     *
     */
    MEDIUMINT("mediumint","Integer"),
    /**
     *
     */
    BIT("bit","Boolean"),
    /**
     *
     */
    BIGINT("varchar","BigInteger"),
    /**
     *
     */
    FLOAT("float","Float"),
    /**
     *
     */
    DOUBLE("double","Double"),
    /**
     *
     */
    DECIMAL("decimal","java.math.BigDecimal"),
    /**
     * 字符串
     */
    BOOLEAN("varchar","Integer"),
    /**
     * 日期
     */
    DATE("date","Date"),
    /**
     * 时间
     */
    DATETIME("datetime","java.util.Date"),
    /**
     * 整形
     */
    INT("int","Integer"),
    /**
     * 时间戳
     */
    TIMESTAMP("timestamp","java.util.Date");

    private final String mySqlDataType;
    private final String javaDataType;

    public static String getJavaDataTypeByMysqlDataType(String mySqlDataType) {
        for (DataTypeEnum e : DataTypeEnum.values()) {
            if (e.getMySqlDataType().equals((mySqlDataType))) {
                return e.getJavaDataType();
            }
        }
        return "";
    }


    private DataTypeEnum(String mySqlDataType, String javaDataType) {
        this.mySqlDataType = mySqlDataType;
        this.javaDataType = javaDataType;
    }
    public String getMySqlDataType() {
        return mySqlDataType;
    }
    public String getJavaDataType() {
        return javaDataType;
    }
}


