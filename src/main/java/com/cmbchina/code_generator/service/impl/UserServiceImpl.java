package com.cmbchina.code_generator.service.impl;

import com.cmbchina.code_generator.dao.UserDao;
import com.cmbchina.code_generator.entity.Config;
import com.cmbchina.code_generator.entity.ProjectData;
import com.cmbchina.code_generator.mapper.UserMapper;
import com.cmbchina.code_generator.entity.User;
import com.cmbchina.code_generator.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    private ProjectData projectData;


    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    public Config findConfigById(String projectId) {
        return userMapper.findConfigById(projectId);
    }

    public int findTableCountById(String projectId) {
        return userMapper.findTableCountById(projectId);
    }

}
