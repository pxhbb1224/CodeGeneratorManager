package com.cmbchina.code_generator.controller;

import com.cmbchina.code_generator.model.Result;
import com.cmbchina.code_generator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/findAll")
    public Result findAll() {
        try {
            return Result.success(userService.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(e);
        }
    }
}
