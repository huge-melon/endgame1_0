package com.shixin.endgame.dao.mongodb;

import com.alibaba.fastjson.JSON;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.*;

import javax.websocket.RemoteEndpoint;
import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;


public class MongoDao {

    private MongoTemplate mongoTemplate;

    public MongoDao(MongoDbFactory mongoDbFactory) {
        this.mongoTemplate = new MongoTemplate(mongoDbFactory);
    }

    //获取集合的元数据

    /*
     * Mapreduce中单条数据和多条数据的处理不一样，当map中的数量大于1时，会执行reduce工作，
     * 等于1 时，不执行reduce的工作*/
    public List<Map<String, Object>> getMetaData(String collectName) {
        //MapReduceOptions options = MapReduceOptions.options();
        String map = "function() { for(var key in this){emit(key,{count: 1,type:typeof(this[key])});}}";
        String reduce = "function(key,values){var total=0; var typeSet = new Set();var types=\"\"; for(var i in values){ total+=values[i].count; typeSet.add(values[i].type); } for(var s of typeSet){ types=types+s+'  '; } return {count:total,type:types};}";
        //String finalize ="function (key, value) { return {count:NumberInt(value.count),type:value.type};}";
        // options.finalizeFunction(finalize);

        MapReduceResults<Map> reduceResults = mongoTemplate.mapReduce(collectName, map, reduce, Map.class);

        /*必须有参数：inputCollectionName, mapFunction, reduceFunction, entityClass
         * 对应的代码写在JS文件中，这里传路径过去*/
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Iterator it = reduceResults.iterator(); it.hasNext(); ) {
            mapList.add((Map) it.next());
        }
        reduceResults.forEach(System.out::println);
        return mapList;
    }

    //获取集合中的数据
    public List<Map> findMongoData(String collectName) {
        System.out.println("MongoDBDao:");
        System.out.println(mongoTemplate.findAll(Map.class));
        System.out.println(mongoTemplate.findAll(Map.class, collectName));
        return mongoTemplate.findAll(Map.class, collectName);
    }

    //获取所有集合名
    public Set<String> getTableName() {
        return mongoTemplate.getCollectionNames();
    }

    public List searchByCondition(String collectName, String key, String op, String value) {
        Query query = new Query();

        if (op.equals("=")) {
            query.addCriteria(Criteria.where(key).is(value));
        } else if (op.equals(">")) {
            query.addCriteria(Criteria.where(key).gt(Integer.parseInt(value)));
        } else if (op.equals(">=")) {
            query.addCriteria(Criteria.where(key).gte(Integer.parseInt(value)));
        } else if (op.equals("<")) {
            query.addCriteria(Criteria.where(key).lt(Integer.parseInt(value)));
        } else if (op.equals("<=")) {
            query.addCriteria(Criteria.where(key).lte(Integer.parseInt(value)));
        } else if (op.equals("IN")) {
            query.addCriteria(Criteria.where(key).in(value.split(",")));
        } else {
            return null;
        }

        System.out.println(mongoTemplate.find(query, Map.class, collectName));

        return mongoTemplate.find(query, Map.class, collectName);
    }

    //插入数据
    public boolean insertData(String collectionName, List<Map<String, Object>> data) {
        for (Map<String, Object> mp : data) {
            if (mp == null) {
                continue;
            }
            String line = JSON.toJSONString(mp);
            Document doc = Document.parse(line);
            mongoTemplate.insert(doc, collectionName);
        }
        return true;
    }

    public boolean delDuplicatedData(String collectionName, List<String> keysName) {
        //  Aggregation aggregation = newAggregation(group("userName","address","class").count().as("count").addToSet("_id").as("dups"),match(Criteria.where("count").gt(1)));

     /*   Fields myfields =fields().and("userName");
        myfields=myfields.and("address");*/
        Fields myfields = fields();
        for (String key : keysName) {
            myfields = myfields.and(key);
        }
        Aggregation aggregation = newAggregation(group(myfields).count().as("count").addToSet("_id").as("dups"), match(Criteria.where("count").gt(1)));

        AggregationResults<Map> aggregationResults = mongoTemplate.aggregate(aggregation, collectionName, Map.class);

        for (Iterator<Map> iterator = aggregationResults.iterator(); iterator.hasNext(); ) {
            Map<String, Object> data = iterator.next();
            List<ObjectId> dups = (List<ObjectId>) data.get("dups");
            System.out.println("before:  " + dups);
            System.out.println(data);
            dups.remove(0);
            System.out.println("after:  " + dups);
            for (ObjectId id : dups) {
                Query query = new Query();
                query.addCriteria(Criteria.where("_id").is(id));
                System.out.println(mongoTemplate.find(query, Map.class, collectionName));
                mongoTemplate.remove(query, collectionName);
            }
        }
        return true;
    }

    public boolean delDataByNull(String collectionName, String opType, List<String> keysName) {

        BasicDBList basicDBList = new BasicDBList();

        for (String key : keysName) {
            basicDBList.add(new BasicDBObject(key, null));
        }
        BasicDBObject obj = new BasicDBObject();
        if (opType.equals("and")) {
            obj.put("$and", basicDBList);
        } else {
            obj.put("$or", basicDBList);
        }
        Query query = new BasicQuery(obj.toJson());

      /*  Query query = new Query();
        Criteria criteria = new Criteria();

        if (opType.equals("and")){
            for (String key:keysName){
                criteria.and(key).is(null);
                //criteria=criteria.orOperator(Criteria.where(key).is(null),Criteria.where(key).is(""));
            }
        }
        else {

        }
        query.addCriteria(criteria);*/

        mongoTemplate.remove(query, collectionName);
        return true;
    }

    public boolean deleteKey(String collectionName, String keyName) {
        Query query = new Query(Criteria.where(""));
        Update update = new Update();
        update.unset(keyName);
        mongoTemplate.updateMulti(query, update, collectionName);
        return true;
    }

    public boolean deleteByCondition(String collectionName, List<Map<String, String>> conditions) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        for (Map<String, String> con : conditions) {
            if (con.get("op").equals("=")) {
                criteria.and(con.get("keyName")).is(con.get("value"));
            } else if (con.get("op").equals(">")) {
                criteria.and(con.get("keyName")).gt(con.get("value"));
                //query.addCriteria(Criteria.where(key).gt(Integer.parseInt(value)));
            } else if (con.get("op").equals(">=")) {
                criteria.and(con.get("keyName")).gte(con.get("value"));
            } else if (con.get("op").equals("<")) {
                criteria.and(con.get("keyName")).lt(con.get("value"));
            } else if (con.get("op").equals("<=")) {
                criteria.and(con.get("keyName")).lte(con.get("value"));
            } else if (con.get("op").equals("IN")) {
                criteria.and(con.get("keyName")).in(con.get("value").split(","));
            } else {
                System.out.println("非法操作符");
                continue;
            }
        }
        query.addCriteria(criteria);
        System.out.println("按条件删除输出： " + mongoTemplate.find(query, Map.class, collectionName));
        mongoTemplate.remove(query, Map.class, collectionName);

        return true;
    }

    // 查出来再重新赋值 String -> Integer
/*    public boolean updateColumnType(){
        String keyName = "passWord";

        Query query =new Query();
        query.addCriteria(Criteria.where(""));

        List<Map> data = mongoTemplate.find(query,Map.class,"user");
        for(Map mp:data){
            System.out.println(mp.get(keyName));
            Update update = new Update();
            Query query1 = new Query();
            update.set(keyName,Integer.parseInt(mp.get(keyName).toString()));
            query1.addCriteria(Criteria.where("_id").is(mp.get("_id")));
            mongoTemplate.updateMulti(query1,update,"user");
        }

        System.out.println(mongoTemplate.find(query,Map.class,"user"));




        return true;
    }*/

    public List getDataByKey(String collectionName, String key) {
        BasicDBObject basicDBObject = new BasicDBObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put(key, true);
        Query query = new BasicQuery(basicDBObject.toJson(), fieldsObject.toJson());

        System.out.println(mongoTemplate.find(query, Map.class, collectionName));
        List data = mongoTemplate.find(query, Map.class, collectionName);
        // 将ObjectId类型的转换为 String
        for (Iterator it = data.iterator(); it.hasNext(); ) {
            HashMap<String, Object> o = (HashMap<String, Object>) it.next();
            String id = o.get("_id").toString();
            o.replace("_id", id);
        }
        return data;
    }

    public boolean setDataByKey(String collectionName, String key, String value, String _id) {

        Query query = new Query(Criteria.where("_id").is(_id));

        System.out.println(mongoTemplate.find(query, Map.class, collectionName));

        Update update = new Update();
        update.set(key, value);
        System.out.println(key + ":  " + value);
        mongoTemplate.updateFirst(query, update, collectionName);
        return true;
    }

    public boolean cutString() {
        String keyName = "userName";
        BasicDBObject basicDBObject = new BasicDBObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        //basicDBObject.put("userName","shixin");
        fieldsObject.put(keyName, true);
        String newValue = "feng";

//        Update update =new Update();
//        update.set(keyName,newValue);
//        ObjectId a = null;
//        Query query1 =new Query(Criteria.where("_id").is(a));
        Query query = new BasicQuery(basicDBObject.toJson(), fieldsObject.toJson());

        System.out.println(mongoTemplate.find(query, Map.class, "user"));

        return true;
    }

    //众数
    public Double getMode(String collectionName, String keyName) {
        Aggregation aggregation = newAggregation(group(keyName).count().as("count"));

        double count = -1;
        Double result = 0.0;
        AggregationResults<Map> aggregationResults = mongoTemplate.aggregate(aggregation, collectionName, Map.class);
        for (Iterator<Map> iterator = aggregationResults.iterator(); iterator.hasNext(); ) {
            Map<String, Object> map = iterator.next();
            System.out.println(map);
            if ((Integer) map.get("count") > count) {
                if (map.get("_id") == null) {
                    continue;
                }
                result = Double.parseDouble(map.get("_id").toString());
                count = (Integer) map.get("count");
            }
        }
        System.out.println("众数：   " + result);
        return result;
    }

    public boolean completFiled(String collectionName, String keyName, Double data) {
        Query query = new Query();
        query.addCriteria(Criteria.where(keyName).is(null));
        Update update = new Update();
        update.set(keyName, data);
        mongoTemplate.updateMulti(query, update, collectionName);
        System.out.println("补全的位置：  " + mongoTemplate.find(query, Map.class, collectionName));
        return true;
    }

//    //中位数
//    public Double getMedian(String collectionName,String keyName){
//
//    }

/*    public List dataVerify(String collectionName,String keyName,String regularExpress){
        BasicDBObject basicDBObject = new BasicDBObject();
        BasicDBObject fieldsObject = new BasicDBObject();
        fieldsObject.put(keyName,true);
        Query query =new BasicQuery(basicDBObject.toJson(),fieldsObject.toJson());

        System.out.println(mongoTemplate.find(query,Map.class,collectionName));
    }*/

}
