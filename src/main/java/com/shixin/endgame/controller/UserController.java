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
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
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
    public Map<String,Object> addDatabse(@RequestParam String dbType,@RequestParam String dbUrl,@RequestParam String dbPort,@RequestParam String dbName,@RequestParam String userName,@RequestParam String userPassword) throws Exception {
        DBinfo dBinfo=new DBinfo();
        dBinfo.setDbType(dbType);
        dBinfo.setDbUrl(dbUrl);
        dBinfo.setDbPort(dbPort);
        dBinfo.setDbName(dbName);
        dBinfo.setUserName(userName);
        dBinfo.setUserPassword(userPassword);
        conndbService.setDataSource(dBinfo);

        HashMap<String,Object> addDBname = new HashMap<>() ;

        addDBname.put("title",dbName);
        addDBname.put("children",queryService.getTableName(dbType,dbName, conndbService.getSqlsessionFactory(dbType,dbName)));


       /* List<Map<String,Object>> dbnameList = new ArrayList<>();
        dbnameList.add(addDBname);*/

        HashMap<String,Object> addDBtype= new HashMap<>();

        addDBtype.put("title",dbType);
        addDBtype.put("children",addDBname);
        // 这边只传回数据库的类型


       /* List<Map<String,Object>> dbtypeList = new ArrayList<>();
        dbtypeList.add(addDBtype);*/

      /*  HashMap<String,Object> sendtofront= new HashMap<>();
        sendtofront.put("children",dbtypeList);

        List<Map<String,Object>> sendtofrontList = new ArrayList<>();
        sendtofrontList.add(sendtofront);*/

        return addDBtype;

        /**
        * @Param  notice
         * 将返回的json信息，加上数据库类型，数据库名，重新包装再返回
         *
         * 现在已经添加到前台，要是这个时候，从别处，修改了，数据表，应该怎么办
         *      添加一个刷新的按钮
        * */

    }

    //获取数据库中的表名
    @GetMapping("/gettable")
    public List<Map<String,Object>> getTableName(@RequestParam String dbType,@RequestParam String dbName) {
        System.out.println("dbType:"+dbType);
        System.out.println("dbName:"+dbName);
        //这里应该写到Service层中
        return queryService.getTableName(dbType,dbName, conndbService.getSqlsessionFactory(dbType,dbName));

    }


    //返回对应表中的信息  gettabledata
    @GetMapping("/gettabledata")
    public List<Map<String,Object>> getTableData(@RequestParam String dbType,@RequestParam String dbName,@RequestParam String tableName){
        return queryService.getTableData(dbType,tableName,conndbService.getSqlsessionFactory(dbType,dbName));
}

    //返回表中元数据 gettablemetadata
    @GetMapping("/gettablemetadata")
    public List<Map<String,Object>> getTableMetaData(@RequestParam String dbType,@RequestParam String dbName,@RequestParam String tableName){
        return queryService.getTableMetaData(dbType,dbName,tableName,conndbService.getSqlsessionFactory(dbType,dbName));
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

    @Configuration
    public class CORSconfiguration extends WebMvcConfigurerAdapter
    {
        @Override
        public void addCorsMappings(CorsRegistry registry){
            registry
                    .addMapping("/**")
                    .allowedMethods("*")
                    .allowedOrigins("*")
                    .allowedHeaders("*");
        }
    }
}
