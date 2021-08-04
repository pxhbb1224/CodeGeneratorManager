package com.cmbchina.code_generator.controller;

import com.alibaba.fastjson.JSONObject;
import com.cmbchina.code_generator.dao.UserDao;
import com.cmbchina.code_generator.entity.*;
import com.cmbchina.code_generator.model.Result;
import com.cmbchina.code_generator.service.TemplateGeneratorService;
import com.cmbchina.code_generator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    @Autowired
    private TemplateGeneratorService generatorService;

    /**
     * @Title:findAll
     * @Description:返回user表中所有值
     * @Param:
     * @return:com.cmbchina.code_generator.model.Result
     * @Author:Bin
     */
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
     * @Title:receiveTable
     * @Description:接收并存储表结构，建立数据库表
     * @Param:table
     * @Param:projectName
     * @return:com.cmbchina.code_generator.model.Result
     * @Author:Bin
     */
    @PostMapping("/table")
    public Result receiveTable(@RequestBody Table table, String projectName)
    {
        try {
            String tableName = table.getTableName();
            System.out.println("表名" + tableName);
            List<Attribute> attributes = table.getProperties();
            for(Attribute a : attributes)
            {
                System.out.println("字段" + a);
            }
            String generateTime = table.getGenerateTime();
            System.out.println("生成时间" + generateTime);
            String res = "";
            if(userDao.addTable(projectName, table))
            {
                res += "项目添加表成功！";
                if(userDao.createTable(table))
                {
                    res += "数据库创建表成功！";
                }
                else
                {
                    res += "数据库创建表失败！";
                    if(userDao.deleteTable(projectName, tableName))
                        res += "回滚删除表成功！";
                    else
                        res += "回滚删除表失败！";
                }
            }
            else
            {
                res += "项目添加表失败！";
            }
            return Result.success(res);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(e);
        }
    }

    /**
     * @TitlereceiveConfig
     * @Description:接收并存储配置结构
     * @Param:config
     * @return:com.cmbchina.code_generator.model.Result
     * @Author:Bin
     */
    @PostMapping("/config")
    public Result receiveConfig(@RequestBody Config config)
    {
        try {
            String packageName = config.getPackageName();
            System.out.println("包名" + packageName);
            String authorName = config.getAuthorName();
            System.out.println("作者名" + authorName);
            /*String moduleName = config.getModuleName();
            System.out.println("模块名" + moduleName);
            String frontEndPath = config.getFrontEndPath();
            System.out.println("前端名" + frontEndPath);
            String interfaceName = config.getInterfaceName();
            System.out.println("接口名" + interfaceName);*/
            String prefix = config.getPrefix();
            System.out.println("前缀名" + prefix);
            int needCovered = config.getNeedCovered();
            System.out.println((needCovered == 1?"":"不") + "会覆盖");
            String description = config.getDescription();
            System.out.println("项目描述" + description);
            String projectName = config.getProjectName();
            System.out.println("项目名" + projectName);
            return Result.success(userDao.setConfig(projectName, config)?"项目设定成功！":"项目设定失败！");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(e);
        }
    }

    /**
     * @Title:deleteTable
     * @Description:根据项目名寻找项目并根据其信息生成对应代码
     * @Param:tableName
     * @return:com.cmbchina.code_generator.model.Result
     * @Author:Bin
     */
    @PostMapping("/delete")
    public Result deleteTable(@RequestParam(name = "name") String tableName)
    {
        try{
            return Result.success(userDao.dropTable(tableName));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(e);
        }
    }
    /**
     * @Title:generateCode
     * @Description:根据项目名寻找项目并根据其信息生成对应代码
     * @Param:projectName
     * @return:com.cmbchina.code_generator.model.Result
     * @Author:Bin
     */
    @PostMapping("/generate")
    public void generateCode(@RequestBody JSONObject object,
                             HttpServletResponse response) throws IOException {
//        if(generatorService.generateCode(userDao.getUserData(projectName)));
        String projectName = object.getString("projectName");
        System.out.println(projectName);
        generatorService.downloadCode(projectName, response);
//        }
//        try
//        {
//            return Result.success();
//        }catch(Exception e)
//        {
//            e.printStackTrace();
//            return Result.fail(e);
//        }
    }
    /**
     * @Title:testMap
     * @Description:输出项目存储结构
     * @Param:
     * @return:com.cmbchina.code_generator.model.Result
     * @Author:Bin
     */
    @GetMapping(value="/test")
    public Result testMap()
    {
        try {
            userDao.printDataMap();
            return Result.success();
        }catch(Exception e)
        {
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
