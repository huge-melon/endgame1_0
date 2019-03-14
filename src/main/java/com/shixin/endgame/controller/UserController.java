package com.shixin.endgame.controller;

import com.shixin.endgame.config.MysqlConfig;
import com.shixin.endgame.entity.DBinfo;
import com.shixin.endgame.dao.mysql.MysqlMapper;
import com.shixin.endgame.service.CleanService;
import com.shixin.endgame.service.ConndbService;
import com.shixin.endgame.service.QueryService;
import com.shixin.endgame.service.StoreService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private HashMap<String,SqlSession> dbSessionMap;

    @Autowired
    private CleanService cleanService;//清洗
    @Autowired
    private StoreService storeService;//存储
    @Autowired
    private QueryService queryService;//查询

    @Autowired
    private ConndbService conndbService;//连接数据库
    //负责数据库连接
    // 通用接口，内部根据dbType调用不同的方法，
    //将sqlsession存放到HashMap中


    //初始化MAP，map应该放到，conndbService中？？？？？？？？？？？？？？？？？？
    public UserController(){

    }


    /*@PostMapping("/gettable")
    public Map<String,Object> getTable(@RequestBody Map<String,String> paramMap){
        String dbType=paramMap.getOrDefault("dbType","");
       // dbType="mysql";
        List<Map<String,Object>> result=queryService.getTables(dbType);
        Map map=new HashMap();
        map.put("data",result);
        return map;

    }*/


    // 添加数据库连接功能
    @GetMapping("/adddb")
    public boolean addDatabse(@RequestParam String dbType,@RequestParam String dbUrl,@RequestParam String dbPort,@RequestParam String dbName,@RequestParam String userName,@RequestParam String userPassword) throws Exception {
        DBinfo dBinfo=new DBinfo();
        dBinfo.setDbType(dbType);
        dBinfo.setDbUrl(dbUrl);
        dBinfo.setDbPort(dbPort);
        dBinfo.setDbName(dbName);
        dBinfo.setUserName(userName);
        dBinfo.setUserPassword(userPassword);
        conndbService.setDataSource(dBinfo);

        return true;
    }

    //获取数据库中的表名
    @GetMapping("/gettable")
    public List<Map<String,Object>> getTable(@RequestParam String dbType,@RequestParam String dbName) {
        System.out.println("dbType:"+dbType);
        System.out.println("dbName:"+dbName);
        //这里应该写到Service层中
        return queryService.getTables(dbType,dbName, conndbService.getSqlsessionFactory(dbType,dbName));

    }

    @GetMapping("/showtabledata")
    public List<Map<String,Object>> getTableData(@RequestParam String dbType,@RequestParam String dbName,@RequestParam String tableName){


        return queryService.getTableData(dbType,dbName,tableName,conndbService.getSqlsessionFactory(dbType,dbName));
    }


/*
    @RequestMapping("/mytest")
    public List<Map<String,Object>> getTable1(){
        String dbType="mysql";
        List<Map<String,Object>> result=queryService.getTables(dbType);
        Map map=new HashMap();
        map.put("data",result);
        return result;
    }

   *//* @RequestMapping("/gettable")
    public List<String> getTable2(){
        String dbType="mysql";
        List<String> result=queryService.getTables(dbType);

        for (String name:result
             ) {
            System.out.println(name);

        }
        return result;
    }
*//*
    @RequestMapping("/hello")
    public String Hello(){
        return "Hello World1";
    }

    @RequestMapping("/selectall")
    public  List<String> selectAll() throws Exception {
        System.out.println("selectALL");
        return queryService.selectALlUser();
    }*/
}
