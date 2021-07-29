package com.cmbchina.code_generator.entity;

import lombok.Data;
import java.util.List;

@Data
public class UserData {

    private List<Table> tableList;
    private Config config;
}
