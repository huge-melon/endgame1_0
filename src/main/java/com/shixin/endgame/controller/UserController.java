package com.shixin.endgame.controller;

import com.shixin.endgame.dao.mongodb.MongoDao;
import com.shixin.endgame.entity.ConditionTable;
import com.shixin.endgame.entity.DBinfo;
import com.shixin.endgame.entity.RegularRequest;
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
            System.out.println("MongoGetTable: "+ data);
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

    @GetMapping("/updateColumnType")
    public boolean updateColumnType(@RequestParam String dbType,@RequestParam String dbName,@RequestParam String tableName,@RequestParam String column,@RequestParam String oldType,@RequestParam String newType){
        return cleanService.updateColumnType(dbType,tableName,column,oldType,newType,conndbService.getSqlsessionFactory(dbType,dbName));
    }

    @GetMapping("/cutString")
    public boolean cutString(@RequestParam String dbType,@RequestParam String dbName,@RequestParam String tableName,@RequestParam String columnName,@RequestParam String priKey,@RequestParam String opType,@RequestParam String beginKey,@RequestParam String endKey){
        return cleanService.cutString(dbType,tableName,columnName,priKey,opType,beginKey,endKey,conndbService.getSqlsessionFactory(dbType,dbName));
    }

    //补全字段
    @GetMapping("/completFiled")
    public boolean completFiled(@RequestParam String dbType,@RequestParam String dbName,@RequestParam String tableName,@RequestParam String columnName,@RequestParam String completType,@RequestParam String defaultValue){
        return cleanService.completFiled(dbType,tableName,columnName,completType,defaultValue,conndbService.getSqlsessionFactory(dbType,dbName));
    }

   /* @GetMapping("/dataVerify")
    public List<Map<String,Object>> dataVerify(@RequestParam String dbType,@RequestParam String dbName,@RequestParam String tableName,@RequestParam String columnName,@RequestParam String priKey,@RequestParam String regularExpress){
        System.out.println("data+++ "+ regularExpress);
        return cleanService.dataVerify(dbType,tableName,columnName,priKey,regularExpress,conndbService.getSqlsessionFactory(dbType,dbName));
    }  */

    @PostMapping("/dataVerify")
    public List<Map<String,Object>> dataVerify(@RequestBody RegularRequest regularRequest){
        System.out.println("data+++ "+ regularRequest);
        return cleanService.dataVerify(regularRequest.getDbType(),regularRequest.getTableName(),regularRequest.getColumnName(),regularRequest.getPriKey(),regularRequest.getRegularExpress(),conndbService.getSqlsessionFactory(regularRequest.getDbType(),regularRequest.getDbName()));
    }

    @PostMapping("/replaceString")
    public List<Map<String,Object>> replaceString(@RequestBody RegularRequest regularRequest){
        return cleanService.replaceString(regularRequest.getDbType(),regularRequest.getTableName(),regularRequest.getColumnName(),regularRequest.getPriKey(),regularRequest.getRegularExpress(),regularRequest.getTargetString(),conndbService.getSqlsessionFactory(regularRequest.getDbType(),regularRequest.getDbName()));
    }

    @PostMapping("/saveUpdateDate")
    public boolean saveUpdateDate(@RequestBody Map<String,Object> receiveData){
        String dbType = (String)receiveData.get("dbType");
        String dbName = (String)receiveData.get("dbName");
        String tableName = (String)receiveData.get("tableName");
        String columnName = (String)receiveData.get("columnName");
        String priKey = (String)receiveData.get("priKey");
        List<Map<String,Object>> data = (List<Map<String, Object>>) receiveData.get("data");
        System.out.println(receiveData);

        return cleanService.saveUpdateDate(dbType,tableName,columnName,priKey,data,conndbService.getSqlsessionFactory(dbType,dbName));

    }

    @PostMapping("/rdbToRdb")
    public boolean rdbToRdb(@RequestBody Map<String,Object> receiveData){
        String sourceDbType = (String)receiveData.get("sourceDbType");
        String sourceDbName = (String)receiveData.get("sourceDbName");
        String sourceTableName = (String)receiveData.get("sourceTableName");
        List<String> sourceColumnList = (List<String>) receiveData.get("sourceColumnList");

        String targetDbType = (String)receiveData.get("targetDbType");
        String targetDbName = (String)receiveData.get("targetDbName");
        String targetTableName = (String)receiveData.get("targetTableName");
        List<String> targetColumnList = (List<String>) receiveData.get("targetColumnList");

        return storeService.rdbToRdb(sourceDbType,sourceTableName,sourceColumnList,targetDbType,targetTableName,targetColumnList,conndbService.getSqlsessionFactory(sourceDbType,sourceDbName),conndbService.getSqlsessionFactory(targetDbType,targetDbName));

    }

    @PostMapping("/rdbToMongo")
    public boolean rdbToMongo(@RequestBody Map<String,Object> receiveData){
        String sourceDbType = (String)receiveData.get("sourceDbType");
        String sourceDbName = (String)receiveData.get("sourceDbName");
        String sourceTableName = (String)receiveData.get("sourceTableName");
        List<String> sourceColumnList = (List<String>) receiveData.get("sourceColumnList");

        String targetDbType = (String)receiveData.get("targetDbType");
        String targetDbName = (String)receiveData.get("targetDbName");
        String targetCollectionName = (String)receiveData.get("targetCollectionName");
        return storeService.rdbToMongo(sourceDbType,sourceTableName,sourceColumnList,targetCollectionName,conndbService.getSqlsessionFactory(sourceDbType,sourceDbName),(MongoTemplate) conndbService.getDbSessionMapping(targetDbType,targetDbName));
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
