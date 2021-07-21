package com.cmbchina.code_generator.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class User {
    int id;        // 对应数据库里面的id

    String name;   // 对应数据库里面的name

    int age;        // 对应数据库里面的年龄

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Timestamp updateTime;
}
