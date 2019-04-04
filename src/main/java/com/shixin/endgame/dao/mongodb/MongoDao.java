package com.shixin.endgame.dao.mongodb;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;


public class MongoDao {

    private MongoTemplate mongoTemplate;

    public MongoDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    //获取集合的元数据

    /*
    * Mapreduce中单条数据和多条数据的处理不一样，当map中的数量大于1时，会执行reduce工作，
    * 等于1 时，不执行reduce的工作*/
    public List<Map<String,Object>> getMetaData(String collectName){
        //MapReduceOptions options = MapReduceOptions.options();
        String map = "function() { for(var key in this){emit(key,{count: 1,type:typeof(this[key])});}}";
        String reduce = "function(key,values){var total=0; var typeSet = new Set();var types=\"\"; for(var i in values){ total+=values[i].count; typeSet.add(values[i].type); } for(var s of typeSet){ types=types+s+'  '; } return {count:total,type:types};}";
        //String finalize ="function (key, value) { return {count:NumberInt(value.count),type:value.type};}";
       // options.finalizeFunction(finalize);

        MapReduceResults<Map> reduceResults =  mongoTemplate.mapReduce(collectName,map,reduce,Map.class);

        /*必须有参数：inputCollectionName, mapFunction, reduceFunction, entityClass
        * 对应的代码写在JS文件中，这里传路径过去*/
        List<Map<String,Object>> mapList = new ArrayList<>();
        for(Iterator it = reduceResults.iterator();it.hasNext();){
            mapList.add((Map)it.next());
        }
        reduceResults.forEach(System.out::println);
        return mapList;
    }

    //获取集合中的数据
    public List<Map> findMongoData(String collectName){
        System.out.println("MongoDBDao:");
        System.out.println(mongoTemplate.findAll(Map.class));
        return mongoTemplate.findAll(Map.class,collectName);
    }
    //获取所有集合名
    public Set<String> getTableName(){
        return mongoTemplate.getCollectionNames();
    }

    public List searchByCondition(String collectName,String key,String op,String value){
        Query query =new Query();

        if(op.equals("=")){
            query.addCriteria(Criteria.where(key).is(value));
        }
        else if(op.equals(">")){
            query.addCriteria(Criteria.where(key).gt(Integer.parseInt(value)));
        }
        else if(op.equals(">=")){
            query.addCriteria(Criteria.where(key).gte(Integer.parseInt(value)));
        }
        else if(op.equals("<")){
            query.addCriteria(Criteria.where(key).lt(Integer.parseInt(value)));
        }
        else if(op.equals("<=")){
            query.addCriteria(Criteria.where(key).lte(Integer.parseInt(value)));
        }
        else if(op.equals("IN")){
            query.addCriteria(Criteria.where(key).in(value.split(",")));
        }
        else{
            return null;
        }

        System.out.println(mongoTemplate.find(query,Map.class,collectName));

        return mongoTemplate.find(query,Map.class,collectName);
    }

}
