package com.cmbchina.code_generator.service;

import com.cmbchina.code_generator.entity.Config;
import com.cmbchina.code_generator.entity.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    Config findConfigById(String projectId);

    int findTableCountById(String projectId);
}
