package com.cmbchina.code_generator.entity;

import lombok.Data;

@Data
public class TableColumns {

    String tableSchema;

    String tableName;

    String tableComment;

    String columnName;

    String columnComment;

    String ordinalPosition;

    String columnDefault;

    String isNullable;

    String dataType;

    String characterMaximumLength;

    String numericPrecision;

    String numericScale;

    String columnType;

    String columnKey;

    String extra;
}
