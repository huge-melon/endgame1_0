package com.shixin.endgame.service;

import com.shixin.endgame.dao.mysql.MysqlMapper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QueryService {
    //@Autowired
    MysqlMapper mysqlMapper;
   // @Autowired @Qualifier("mysqlDataSource")
    PooledDataSource mysqlDataSource;

   // @Autowired @Qualifier("mysqlSqlSessionFactory")
    SqlSessionFactory mysqlSqlSessionFactory;

    //@Autowired @Qualifier("oracleSqlSessionFactory")
  //  SqlSessionFactory oralsqlSessionFactory;

    private final Logger logger= LoggerFactory.getLogger(this.getClass());

     /**
      * 获取表中数据
      * @param tableName
      * @param dbType
      * @return
      */
     public List<Map<String,Object>> getTableData(String dbType ,String tableName,SqlSessionFactory sqlSessionFactory){
         SqlSession session=sqlSessionFactory.openSession();
         if(dbType.equals("MySQL")){
             MysqlMapper mysqlMapper= session.getMapper(MysqlMapper.class);
             return mysqlMapper.getTableData(tableName);
         }
         else if(dbType.equals("Oracle")){

         }
         else{
             System.out.println("error");
         }
         return null;
     }

     /**
     * 获取数据库中的表
     * @return
     */
     public List<Map<String,Object>> getTables(String dbType, String dbName,SqlSessionFactory sqlSessionFactory){

         SqlSession session=sqlSessionFactory.openSession();
         if(dbType.equals("MySQL")) {
             MysqlMapper mysqlMapper=session.getMapper(MysqlMapper.class);
             return mysqlMapper.getTables(dbName);
         }
         else if(dbType=="Oracle"){

         }
         else{
             System.out.println("error");
         }
         return null;
     }

     public List<String> selectALlUser() throws Exception {
          return mysqlMapper.selectAllUser();

     }

     private PooledDataSource getDatasource(String dbType) {
         if(dbType.equals("mysql")){
             return mysqlDataSource;
         }
         else
             return null;
     }

     private MysqlMapper getMapper(String dbType){
         if(dbType.equals("mysql")){
             return mysqlMapper;
         }
         else
             return null;
     }

}

/* *//**
 * 设置数据库的用户名和密码
 *
 * @param urlIP
 * @param urlPort
 * @param dbType
 * @param userName
 * @param passWord
 * @param urlSID
 * @return
 *//*

     public boolean setDataSource(String dbType,String userName,String passWord,String urlIP,String urlPort,String urlSID){
         PooledDataSource pooledDataSource=getDatasource(dbType);
         pooledDataSource.setUsername(userName);
         pooledDataSource.setPassword(passWord);
         if(dbType.equals("mysql")){
             pooledDataSource.setUrl("jdbc:mysql://"+urlIP+":"+urlPort+"/"+urlSID);
             logger.debug(pooledDataSource.getUrl());
             return true;
         }
         else
             return false;

         *//*
 *else if(dbType.equals("oracle"))
 * pooledDataSource.setUrl("jdbc:oracle:thin:@\"+urlIP+\":\"+urlPort+\":\"+urlSID");
 *//*
     }

     public Map getDataSource(String dbType){
         PooledDataSource pooledDataSource=getDatasource(dbType);

         Map map=new HashMap();
         try {
             String userName = pooledDataSource.getUsername();
             String passWord = pooledDataSource.getPassword();
             String urlPrimitive = pooledDataSource.getUrl();
             if (dbType.equals("mysql")) {

                 String[] url=urlPrimitive.split("//");//分割原始的url字符串
                 String[] urlTemp=url[1].split(":");//分割剩余的ip部分的url
                 String[] urlFinal=urlTemp[1].split("/");//mysql指定的数据库
                 if (userName==null && passWord ==null &&url.length==2)
                 {
                     map.put("userName","");
                     map.put("passWord","");
                     map.put("urlIP","");//url中的ip地址
                     map.put("urlPort","");//port
                     map.put("urlSID","");//SID
                 }
                 else if (url.length>0&&urlFinal.length>0){
                     map.put("userName",userName);
                     map.put("passWord",passWord);
                     map.put("urlIP",urlTemp[0]);//url中的ip地址
                     map.put("urlPort",urlFinal[0]);//port
                     map.put("urlSID",urlFinal[1]);//数据库名
                 }

             }
             return map;//返回数据库中的用户名和密码
         }  catch (NullPointerException e){
             logger.error("username is null");
             map.put("userName","");
             map.put("passWord","");
             map.put("urlIP","");//url中的ip地址
             map.put("urlPort","");//port
             map.put("urlSID","");//SID
             return  map;//返回数据库中的用户名和密码
         }

     }*/