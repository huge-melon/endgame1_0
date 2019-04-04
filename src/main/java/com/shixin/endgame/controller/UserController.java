package com.shixin.endgame.controller;

import com.shixin.endgame.dao.mongodb.MongoDao;
import com.shixin.endgame.entity.ConditionTable;
import com.shixin.endgame.entity.DBinfo;
import com.shixin.endgame.service.CleanService;
import com.shixin.endgame.service.ConndbService;
import com.shixin.endgame.service.QueryService;
import com.shixin.endgame.service.StoreService;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.*;

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
    // 添加数据库连接功能
    @PostMapping("/adddb")
    public Map<String,Object> addDatabse(@RequestBody DBinfo dBinfo) throws Exception {
        conndbService.setDataSource(dBinfo);
        HashMap<String,Object> addDBname = new HashMap<>() ;
        HashMap<String,Object> addDBtype= new HashMap<>();

        if(dBinfo.getDbType().equals("MongoDB")){
            MongoTemplate mongoTemplate = (MongoTemplate) conndbService.setDataSource(dBinfo);
            MongoDao mongoDao = new MongoDao(mongoTemplate);
          //  List data = mongoDao.findMongoData("runoob");
            Set colsName = mongoDao.getTableName();
            List<Map<String,Object>> collections = new ArrayList<>();
            for (Object col: colsName) {
                Map<String,Object> table =new HashMap<>();
                table.put("TABLE_NAME",col);
                collections.add(table);
            }
            addDBname.put("title",dBinfo.getDbName());
            addDBname.put("children",collections);
            addDBtype.put("title",dBinfo.getDbType());
            addDBtype.put("children",addDBname);
            return addDBtype;
        }

        addDBname.put("title",dBinfo.getDbName());
        addDBname.put("children",queryService.getTableName(dBinfo.getDbType(),dBinfo.getDbName(), conndbService.getSqlsessionFactory(dBinfo.getDbType(),dBinfo.getDbName())));
        addDBtype.put("title",dBinfo.getDbType());
        addDBtype.put("children",addDBname);
        return addDBtype;
        /**
        * @Param  notice
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
        if(dbType.equals("MongoDB")){
            System.out.println("*************************************");
            MongoTemplate mongoTemplate = (MongoTemplate) conndbService.getDbSessionMapping(dbType,dbName);
            MongoDao mongoDao = new MongoDao(mongoTemplate);
            List data = mongoDao.findMongoData(tableName);

            for(Iterator it = data.iterator();it.hasNext();){
                HashMap<String,Object> o =(HashMap<String, Object>) it.next();
                String id= o.get("_id").toString();
                o.replace("_id",id);
            }
            return data;

        }
        else
            return queryService.getTableData(dbType,tableName,conndbService.getSqlsessionFactory(dbType,dbName));
    }


    //返回表中元数据 gettablemetadata
    @GetMapping("/gettablemetadata")
    public List<Map<String,Object>> getTableMetaData(@RequestParam String dbType,@RequestParam String dbName,@RequestParam String tableName){
        if(dbType.equals("MongoDB")){
            MongoTemplate mongoTemplate = (MongoTemplate) conndbService.getDbSessionMapping("MongoDB",dbName);
            MongoDao mongoDao = new MongoDao(mongoTemplate);
            return mongoDao.getMetaData(tableName);
        }
        else{
            return queryService.getTableMetaData(dbType,dbName,tableName,conndbService.getSqlsessionFactory(dbType,dbName));
        }
    }

    //http://localhost:8080/test/delDuplicatedData?dbType=MySQL&dbName=test1&tableName=users&columnsName=userName,user_sex,hometown&id=id

    //去除重复的数据
    @GetMapping("/delDuplicatedData")
    public boolean delDuplicatedData(@RequestParam String dbType,@RequestParam String dbName,@RequestParam String tableName,@RequestParam String columnsName,@RequestParam String id){
        return cleanService.delDuplicatedData(dbType,tableName,columnsName,id,conndbService.getSqlsessionFactory(dbType,dbName));
    }
    @GetMapping("/delDataByNull")
    public boolean delDataByNull(@RequestParam String dbType,@RequestParam String dbName,@RequestParam String tableName,@RequestParam String columnsName,@RequestParam String method){
        return cleanService.delDataByNull(dbType,tableName,columnsName,method,conndbService.getSqlsessionFactory(dbType,dbName));
    }

    @GetMapping("/deleteTableColumn")
    public boolean deleteTableColumn(@RequestParam String dbType,@RequestParam String dbName,@RequestParam String tableName,@RequestParam String columnName){
            return cleanService.deleteTableColumn(dbType,tableName,columnName,conndbService.getSqlsessionFactory(dbType,dbName));
    }

    @PostMapping("/deleteByCondition")
    public boolean deleteByCondition(@RequestBody ConditionTable conditionTable){
        System.out.println(conditionTable.toString());
        return cleanService.deleteByCondition(conditionTable,conndbService.getSqlsessionFactory(conditionTable.getDbType(),conditionTable.getDbName()));
    }

    @PostMapping("/OK")
    public boolean OK(@RequestParam String dbType,@RequestParam String dbName){
        System.out.println("OK   "+dbType+"  "+dbName);
        return true;
    }

    @GetMapping("/mongoSearch")
    public List<Map<String,Object>> searchByCondition(@RequestParam String dbName,@RequestParam String collectionName,@RequestParam String key_field,@RequestParam String operation,@RequestParam String keyWord){
        System.out.println(dbName+" : "+collectionName+" : " + key_field+" : "+operation+" : "+key_field);
        MongoTemplate mongoTemplate = (MongoTemplate) conndbService.getDbSessionMapping("MongoDB",dbName);
        MongoDao mongoDao = new MongoDao(mongoTemplate);
        List data = mongoDao.searchByCondition(collectionName,key_field,operation,keyWord);
        for(Iterator it = data.iterator();it.hasNext();){
            HashMap<String,Object> o =(HashMap<String, Object>) it.next();
            String id= o.get("_id").toString();
            o.replace("_id",id);
        }
        return data;
    }



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
