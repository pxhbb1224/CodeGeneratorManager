package com.cmbchina.code_generator.service.impl;

import com.cmbchina.code_generator.mapper.UserMapper;
import com.cmbchina.code_generator.entity.User;
import com.cmbchina.code_generator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }
}
