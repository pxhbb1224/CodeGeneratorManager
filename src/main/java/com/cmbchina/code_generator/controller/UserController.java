package com.cmbchina.code_generator.controller;

import com.cmbchina.code_generator.dao.UserDao;
import com.cmbchina.code_generator.model.Result;
import com.cmbchina.code_generator.service.UserService;
import com.cmbchina.code_generator.entity.User;
import com.cmbchina.code_generator.utils.FormatNameUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import com.cmbchina.code_generator.model.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    static Map<Integer,User> map = new ConcurrentHashMap<>();

    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;
    @GetMapping("/createTable")
    public Result createTable()
    {
        try {
            Table table = new Table();
            return Result.success(userDao.createTable(table));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(e);
        }
    }
    @GetMapping("/findAll")
    public Result findAll() {
        try {
            return Result.success(userService.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(e);
        }
    }

    /**
     * @Title: getList
     * @Description:  获取用户列表
     * @Param: []
     * @return: java.util.List<com.cmbchina.code_generator.entity.User>
     * @Author: Bin
     */

    @ApiOperation(value = "获取用户列表")
    @GetMapping(value = "")
    public List<User> getList()
    {
        List<User> list = new ArrayList<>(map.values());
        return list;
    }
    /**
     * @Title: postUser
     * @Description:  根据user创建用户
     * @Param: [user]
     * @return: java.lang.String
     * @Author: Bin
     */
    @ApiOperation(value = "创建用户" , notes = "根据user对象创建用户")
    @ApiImplicitParam(name = "user",value = "用户详情实体类",required = true,dataType = "User")
    @PostMapping(value = "")
    public String postUser(@RequestBody User user)
    {
        map.put(user.getId(), user);
        return "添加成功！";
    }
    /**
     * @Title: getUserById
     * @Description:  根据用户id获取用户基本信息
     * @Param: [id]
     * @return: com.cmbchina.entity.User
     * @Author: Bin
     */
    @ApiOperation(value = "获取用户详情",notes = "根据url的id来获取用户基本信息")
    @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Integer",paramType = "path")
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public User getUserById(@PathVariable Integer id) {
        return  map.get(id);
    }

    /**
     * @Title: putUser
     * @Description:  根据用户id来指定更新对象，进行用户的信息更新
     * @Param: [id, user]
     * @return: java.lang.String
     * @Author: Bacy
     * @Date: 2018/4/24
     */
    @ApiOperation(value = "更新用户信息",notes = "根据url的id来指定对象，并且根据传过来的user进行用户基本信息更新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, paramType = "path", dataType = "Integer"),
            @ApiImplicitParam(name = "user", value = "用户详情实体类user", required = true, dataType = "User")
    })
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public String putUser(@PathVariable Integer id,@RequestBody User user) {
        User u = map.get(id);
        u.setAge(user.getAge());
        u.setName(user.getName());
        map.put(id,u);

        return "用户基本信息已经更新成功~~~";

    }
    /**
     * @Title: delUser
     * @Description:  根据用户id，删除用户
     * @Param: [id]
     * @return: java.lang.String
     * @Author: Bacy
     * @Date: 2018/4/24
     */
    @ApiOperation(value = "删除用户",notes = "根据url的id来指定对象，进行用户信息删除")
    @ApiImplicitParam(name = "id",value = "用户id",required = true,dataType = "Integer",paramType = "path")
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public String delUser(@PathVariable Integer id) {
        map.remove(id);
        return "用户ID为："+ id + " 的用户已经被移除系统~~";

    }
}
