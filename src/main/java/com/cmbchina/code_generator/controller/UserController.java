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
import java.util.UUID;
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
     * 从数据库中同步DataMap的内容（进入项目主界面时）
     * @return
     */
    @GetMapping("/updateData")
    public Result updateData() {
        try {
            userDao.updateData();
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(e);
        }
    }

    /**
     * 获取所有项目配置信息
     * @return
     */
    @GetMapping("/getConfig")
    public Result getConfig() {
        try {
            return Result.success(userDao.getConfig());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(e);
        }
    }

    /**
     * 发送所有项目列表信息
     * @return
     */
    @GetMapping("/sendProjectData")
    public Result sendProjectData()
    {
        try {
            userDao.updateData();
            return Result.success(userDao.getDataMap().formatMap());
        } catch (Exception e)
        {
            e.printStackTrace();
            return Result.fail(e);
        }
    }

    /**
     * 根据tableId发送数据库表的信息（描述、生成时间）
     * @param object
     * @return
     */
    @PostMapping("/getInfo")
    public Result getInfo(@RequestBody JSONObject object) {
        try {
            String tableId = object.getString("tableId");
            return Result.success(userDao.getInfo(tableId));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(e);
        }
    }

    /**
     * 根据tableId发送数据库表具体的字段信息
     * @param object
     * @return
     */
    @PostMapping("/sendOneTable")
    public Result sendOneTable(@RequestBody JSONObject object) {
        try {
            String tableId = object.getString("tableId");
            return Result.success(userDao.getDataMap().getTable(tableId));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(e);
        }
    }

    /**
     * 根据projectId发送项目具体配置信息
     * @param object
     * @return
     */
    @PostMapping("/sendConfig")
    public Result sendConfig(@RequestBody JSONObject object) {
        try {
            String projectId = object.getString("projectId");
            return Result.success(userDao.getDataMap().getConfig(projectId));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(e);
        }
    }

    /**
     * 根据projectId发送项目信息
     * @param object
     * @return
     */
    @PostMapping("/sendProject")
    public Result sendProject(@RequestBody JSONObject object){
        try {
            String projectId = object.getString("projectId");
            JSONObject response = new JSONObject();
            response.put("config", userService.findConfigById(projectId));
            response.put("count", userService.findTableCountById(projectId));
            return Result.success(response);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(e);
        }
    }

    /**
     * 根据projectId发送项目下实体列表信息
     * @param object
     * @return
     */
    @PostMapping("/sendTable")
    public Result sendTable(@RequestBody JSONObject object) {

        try {
            String projectId = object.getString("projectId");
            return Result.success(userDao.getUserData(projectId).getTableList());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(e);
        }
    }

    /**
     * 将tableId对应的table共享给projectId对应的project
     * isIndependent为0代表不独立，非0代表独立
     * @param object
     * @return
     */
    @PostMapping("/shareTable")
    public Result shareTable(@RequestBody JSONObject object)
    {
        System.out.println("---------------------------------");
        try{
            String projectId = object.getString("projectId");
            String tableId = object.getString("tableId");
            int isIndependent = object.getIntValue("isIndependent");
            String res = "";
            boolean isRight = true;
            if(isIndependent == 0) {
                res += "共享项目：";
                if (userDao.isConfigExists(projectId))
                    if (userDao.isTableExists(tableId) && userDao.isExistsInTable(tableId)) {
                        Table table = new Table();
                        table.setTableId(tableId);
                        if (userDao.addTable(projectId, table)) {
                            res += "项目和表联系添加成功！";
                            if (userDao.insertProject(projectId, tableId))
                                res += "项目表记录添加成功！";
                            else {
                                isRight = false;
                                res += "项目表记录添加失败！";
                            }

                        } else {
                            isRight = false;
                            res += "项目与表联系添加失败！";
                        }

                    } else {
                        isRight = false;
                        res += "表不存在！";
                    }

                else {
                    isRight = false;
                    res += "项目不存在！";
                }
            }
            else
            {
                res += "引入项目：";
                Table table = userDao.getDataMap().getTable(tableId);
                if(table != null)
                {
                    table.setTableId(UUID.randomUUID().toString().replace("-", ""));
                    if (userDao.addTable(projectId, table)) {
                        res += "项目结构添加表成功！";
                        if (userDao.createTable(table)) {
                            res += "数据库创建表成功！";
                            if (userDao.insertProject(projectId, tableId)) {
                                res += "项目表记录添加成功！";
                            } else {
                                res += "项目表记录添加失败！";
                                isRight = false;
                                if(userDao.dropTable(tableId, false))
                                {
                                    res += "数据库回滚删除表成功！";
                                    if (userDao.deleteTable(projectId, tableId)) {
                                        res += "回滚删除项目结构表成功！";
                                    } else {
                                        res += "回滚删除项目结构表失败！";
                                    }
                                }
                                else
                                {
                                    res += "数据库回滚删除表失败！";
                                }
                            }
                        } else {
                            res += "数据库创建表失败！";
                            isRight = false;
                            if (userDao.deleteTable(projectId, tableId)) {
                                res += "回滚删除项目结构表成功！";
                            } else {
                                res += "回滚删除项目结构表失败！";
                            }
                        }
                    }
                }
                else
                {
                    res += "表不存在！";
                    isRight = false;
                }
            }
            if(isRight)
                return Result.success(res);
            else
                return Result.fail(res);
        } catch (Exception e)
        {
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
    public Result receiveTable(@RequestBody Table table, @RequestParam(value = "projectId")String projectId)
    {
        try {
            boolean isRight = true;
            System.out.println("项目ID " + projectId);
            String tableName = table.getTableName();
            System.out.println("表名 " + tableName);
            List<Attribute> attributes = table.getProperties();
            for(Attribute a : attributes)
            {
                System.out.println("字段 " + a);
            }
            String comment = table.getComment();
            System.out.println("注释 " + comment);
            String res = "";
            String tableId = table.getTableId();
            if(tableId == null || tableId.trim().length() == 0) {
                tableId = UUID.randomUUID().toString().replace("-", "");
                table.setTableId(tableId);
                if (userDao.addTable(projectId, table)) {
                    res += "项目结构添加表成功！";
                    if (userDao.createTable(table)) {
                        res += "数据库创建表成功！";
                        if (userDao.insertProject(projectId, tableId)) {
                            res += "项目表记录添加成功！";
                        } else {
                            res += "项目表记录添加失败！";
                            isRight = false;
                            if(userDao.dropTable(tableId, false))
                            {
                                res += "数据库回滚删除表成功！";
                                if (userDao.deleteTable(projectId, tableId)) {
                                    res += "回滚删除项目结构表成功！";
                                } else {
                                    res += "回滚删除项目结构表失败！";
                                }
                            }
                            else
                            {
                                res += "数据库回滚删除表失败！";
                            }
                        }
                    } else {
                        res += "数据库创建表失败！";
                        isRight = false;
                        if (userDao.deleteTable(projectId, tableId)) {
                            res += "回滚删除项目结构表成功！";
                        } else {
                            res += "回滚删除项目结构表失败！";
                        }
                    }
                }
            }
            else
            {
                if(userDao.replaceTable(table))
                {
                    res += "修改表成功！";
                }
                else
                {
                    res += "修改表失败！";
                    isRight = false;
                }

            }
            if(isRight)
                return Result.success(res);
            else
                return Result.fail(res);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(e);
        }
    }

    /**
     * @Title:receiveConfig
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
            String projectId = config.getProjectId();
            if(projectId == null || projectId.trim().length() == 0) {
                projectId = UUID.randomUUID().toString().replace("-", "");
                config.setProjectId(projectId);
            }
            else
                System.out.println("项目id" + projectId);
            if(userDao.setConfig(projectId, config))
                return Result.success("项目设定成功！");
            else
                return Result.fail("项目设定失败！");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(e);
        }
    }

    /**
     * @Title:deleteTable
     * @Description:根据项目名寻找项目并删除对应表
     * @Param:tableName
     * @return:com.cmbchina.code_generator.model.Result
     * @Author:Bin
     */
    @PostMapping("/deleteTable")
    public Result deleteTable(@RequestBody JSONObject object)
    {
        System.out.println("---------------------------------");
        try{
            System.out.println("delete table...");
            boolean isRight = true;
            String projectId = object.getString("projectId");
            String tableId = object.getString("tableId");
            System.out.println("项目ID " + projectId);
            System.out.println("表ID " + tableId);
            String res = "";
            if(userDao.deleteTable(projectId, tableId))
            {
                res += "项目删除表成功！";
                if(userDao.dropTable(tableId, false))
                {
                    res += "数据库删除表相关项成功！";
                }
                else
                {
                    isRight = false;
                    res += "数据库删除表相关项失败！";
                }
            }
            else
            {
                isRight = false;
                res += "项目删除表失败！";
            }
            if(isRight)
                return Result.success(res);
            else
                return Result.fail(res);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(e);
        }
    }

    /**
     * 删除项目结构及其相关表
     * @param:object
     * @return
     */
    @PostMapping(value="deleteProject")
    public Result deleteProject(@RequestBody JSONObject object)
    {
        try {
            System.out.println("delete project...");
            String projectId = object.getString("projectId");
            if(userDao.deleteProject(projectId))
                return Result.success();
            else
                return Result.fail();
        } catch (Exception e)
        {
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
    @PostMapping("/generated")
    public void generatedCode(@RequestBody String projectName)
    {
        generatorService.generateCode(userDao.getUserData(projectName));
    }
    @PostMapping("/generate")
    public void generateCode(@RequestBody JSONObject object,
                             HttpServletResponse response) throws IOException {
        String projectId = object.getString("projectId");
        try{
            if(generatorService.generateCode(userDao.getUserData(projectId))){
                generatorService.downloadCode(userDao.getUserData(projectId).getConfig().getProjectName(), response);
//                return Result.success();
            }
        }catch(Exception e) {
            e.printStackTrace();
//            return Result.fail(e);
        }
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
