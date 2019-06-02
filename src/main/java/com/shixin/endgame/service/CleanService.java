package com.shixin.endgame.service;
//负责数据清洗

import com.shixin.endgame.dao.mongodb.MongoDao;
import com.shixin.endgame.dao.mysql.MysqlMapper;
import com.shixin.endgame.dao.postgresql.PostgresqlMapper;
import com.shixin.endgame.entity.ConditionTable;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class CleanService {

    private MysqlMapper mysqlMapper;
    private PostgresqlMapper postgresqlMapper;
    private MongoDao mongoDao;

    public boolean delDuplicatedData(String dbType, String tableName, String columnsName, String id, Object dbConnect) {

        if (dbType.equals("MySQL")) {
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) dbConnect;
            SqlSession session = sqlSessionFactory.openSession();
            mysqlMapper = session.getMapper(MysqlMapper.class);
            String column = columnsName.split(",")[0];
            long startTime=System.currentTimeMillis();   //获取开始时间
            mysqlMapper.delDuplicatedData(tableName, columnsName, column, id);
            long endTime=System.currentTimeMillis(); //获取结束时间
            System.out.println("MySQL去重运行时间： "+(endTime-startTime)+"ms");

            return true;
        } else if (dbType.equals("PostgreSQL")) {
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) dbConnect;
            SqlSession session = sqlSessionFactory.openSession();
            postgresqlMapper = session.getMapper(PostgresqlMapper.class);
            String[] strs = columnsName.split(",");
            for (int i = 0; i < strs.length; i++) {
                strs[i] = "\"" + strs[i] + "\"";
            }
            String newColumnsName = StringUtils.join(strs, ",");
            postgresqlMapper.delDuplicatedData(tableName, newColumnsName, strs[0], id);
            return true;
        } else if (dbType.equals("MongoDB")) {
            MongoDbFactory mongoDbFactory = (MongoDbFactory) dbConnect;
            mongoDao = new MongoDao(mongoDbFactory);
            List<String> keysName = Arrays.asList(columnsName.split(","));
            long startTime=System.currentTimeMillis();   //获取开始时间
            mongoDao.delDuplicatedData(tableName, keysName);
            long endTime=System.currentTimeMillis(); //获取结束时间
            System.out.println("MongoDB去重运行时间： "+(endTime-startTime)+"ms");
            return true;
        } else {
            System.out.println("error");
        }
        return false;
    }

    //删除缺失项
    public boolean delDataByNull(String dbType, String tableName, String columnsName, String method, Object dbConnect) {
        if (dbType.equals("MySQL")) {
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) dbConnect;
            SqlSession session = sqlSessionFactory.openSession();
            mysqlMapper = session.getMapper(MysqlMapper.class);
            List<String> column = Arrays.asList(columnsName.split(","));
            if (method.equals("and")) // 可用mybatis动态if
                mysqlMapper.delDataByNullAnd(tableName, column);
            else
                mysqlMapper.delDataByNullOr(tableName, column);
            return true;
        } else if (dbType.equals("PostgreSQL")) {
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) dbConnect;
            SqlSession session = sqlSessionFactory.openSession();
            postgresqlMapper = session.getMapper(PostgresqlMapper.class);
            List<String> column = Arrays.asList(columnsName.split(","));
            if (method.equals("and")) // 可用mybatis动态if
                postgresqlMapper.delDataByNullAnd(tableName, column);
            else
                postgresqlMapper.delDataByNullOr(tableName, column);
            return true;
        } else if (dbType.equals("MongoDB")) {
            MongoDbFactory mongoDbFactory = (MongoDbFactory) dbConnect;
            mongoDao = new MongoDao(mongoDbFactory);
            List<String> keysName = Arrays.asList(columnsName.split(","));
            mongoDao.delDataByNull(tableName, method, keysName);
            return true;
        } else {
            System.out.println("error");
        }
        return false;
    }

    //删除一列数据
    public boolean deleteTableColumn(String dbType, String tableName, String columnName, Object dbConnect) {
        if (dbType.equals("MySQL")) {
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) dbConnect;
            SqlSession session = sqlSessionFactory.openSession();
            mysqlMapper = session.getMapper(MysqlMapper.class);
            mysqlMapper.deleteTableColumn(tableName, columnName);
            return true;
        } else if (dbType.equals("PostgreSQL")) {
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) dbConnect;
            SqlSession session = sqlSessionFactory.openSession();
            postgresqlMapper = session.getMapper(PostgresqlMapper.class);
            postgresqlMapper.deleteTableColumn(tableName, columnName);
            return true;
        } else if (dbType.equals("MongoDB")) {
            MongoDbFactory mongoDbFactory = (MongoDbFactory) dbConnect;
            mongoDao = new MongoDao(mongoDbFactory);
            return mongoDao.deleteKey(tableName, columnName);
        } else {
            System.out.println("error");
        }
        return false;
    }

    //按条件删除一列数据
    public boolean deleteByCondition(ConditionTable conditionTable, Object dbConnect) {
        if (conditionTable.getDbType().equals("MySQL")) {
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) dbConnect;
            SqlSession session = sqlSessionFactory.openSession();
            mysqlMapper = session.getMapper(MysqlMapper.class);
            System.out.println(conditionTable.getConditions().toString());
            mysqlMapper.deleteByCondition(conditionTable.getTableName(), conditionTable.getConditions());
            return true;
        } else if (conditionTable.getDbType().equals("PostgreSQL")) {
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) dbConnect;
            SqlSession session = sqlSessionFactory.openSession();
            postgresqlMapper = session.getMapper(PostgresqlMapper.class);
            List<String> condtionList = conditionTable.getConditions();
            List<String> afterModify = new ArrayList<>();
            for (String con : condtionList) {
                int posSpace = con.indexOf(' ');
                String s1 = con.substring(0, posSpace);
                String s2 = con.substring(posSpace, con.length());
                con = "\"" + s1 + "\"" + s2;
                afterModify.add(con);
            }
            System.out.println(afterModify);
            postgresqlMapper.deleteByCondition(conditionTable.getTableName(), afterModify);
            return true;
        } else if (conditionTable.getDbType().equals("MongoDB")) {
            MongoDbFactory mongoDbFactory = (MongoDbFactory) dbConnect;
            mongoDao = new MongoDao(mongoDbFactory);
            List<String> condtionList = conditionTable.getConditions();
            List<Map<String, String>> conMap = new ArrayList<>();
            for (String con : condtionList) {
                String[] strs = con.split(" ");
                System.out.println(strs);
                Map<String, String> map = new HashMap<>();
                map.put("keyName", strs[0]);
                map.put("op", strs[1]);
                map.put("value", strs[2]);
                conMap.add(map);
            }
            mongoDao.deleteByCondition(conditionTable.getTableName(), conMap);
            return true;
        } else {
            System.out.println("error");
        }
        return false;
    }

    //修改字段类型  没有MongoDB
    public boolean updateColumnType(String dbType, String tableName, String column, String oldType, String newType, Object dbConnect) {
        if (dbType.equals("MySQL")) {
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) dbConnect;
            SqlSession session = sqlSessionFactory.openSession();
            mysqlMapper = session.getMapper(MysqlMapper.class);
            /*
             *
             * 将所有的值进行类型检查
             *
             * 将oldType做对照
             * */
            mysqlMapper.updateColumnType(tableName, column, newType);
            return true;

        } else if (dbType.equals("PostgreSQL")) {
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) dbConnect;
            SqlSession session = sqlSessionFactory.openSession();
            postgresqlMapper = session.getMapper(PostgresqlMapper.class);
            postgresqlMapper.updateColumnType(tableName, column, newType);
            //不同类型有不同的长度，搞成输入式的不搞这种选择的。
            return true;

        } else if (dbType.equals("MongoDB")) {
            MongoDbFactory mongoDbFactory = (MongoDbFactory) dbConnect;
            mongoDao = new MongoDao(mongoDbFactory);
        } else {
            System.out.println("error");
        }
        return false;
    }


    //剪切字符串
    public boolean cutString(String dbType, String tableName, String columnName, String priKey, String opType, String beginKey, String endKey, Object dbConnect) {
        List<Map<String, Object>> columnData = null;

        if (dbType.equals("MySQL")) {
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) dbConnect;
            SqlSession session = sqlSessionFactory.openSession();
            mysqlMapper = session.getMapper(MysqlMapper.class);
            columnData = mysqlMapper.getColumnData(tableName, columnName, priKey);
        } else if (dbType.equals("PostgreSQL")) {
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) dbConnect;
            SqlSession session = sqlSessionFactory.openSession();
            postgresqlMapper = session.getMapper(PostgresqlMapper.class);
            columnData = postgresqlMapper.getColumnData(tableName, columnName, priKey);
        } else if (dbType.equals("MongoDB")) {
            MongoDbFactory mongoDbFactory = (MongoDbFactory) dbConnect;
            mongoDao = new MongoDao(mongoDbFactory);
            columnData = mongoDao.getDataByKey(tableName, columnName);
        } else {
            return false;
        }


        System.out.println("columnData:   " + columnData);
        if (opType.equals("pos")) {
            for (Map<String, Object> mo : columnData) {
                if (mo.containsKey(columnName)) {
                    String str = (String) mo.get(columnName);

                    int begin = Integer.parseInt(beginKey);
                    if (begin > str.length() || begin < 0) {
                        begin = 0;
                        System.out.println("起始位置超出范围");
                    }
                    int end = Integer.parseInt(endKey);
                    if (end > str.length() || end < 0) {
                        end = str.length();
                        System.out.println("结束位置超出范围");
                    }
                    mo.replace(columnName, str.substring(begin, end));
                }
            }
        } else {
            for (Map<String, Object> mo : columnData) {
                if (mo.containsKey(columnName)) {
                    String str = (String) mo.get(columnName);
                    int begin = str.indexOf(beginKey) == -1 ? 0 : str.indexOf(beginKey);
                    int end = str.lastIndexOf(endKey) == -1 ? str.length() : str.indexOf(endKey);
                    mo.replace(columnName, str.substring(begin + beginKey.length(), end));
                }
            }
        }
        System.out.println("columnData222:   " + columnData);
        if (dbType.equals("MySQL")) {
            for (Map<String, Object> mo : columnData) {
                if (mo.containsKey(columnName)) {
                    mysqlMapper.setColumnData(tableName, columnName, (String) mo.get(columnName), priKey, mo.get(priKey).toString());
                }
            }
            return true;
        } else if (dbType.equals("PostgreSQL")) {
            for (Map<String, Object> mo : columnData) {
                if (mo.containsKey(columnName)) {
                    postgresqlMapper.setColumnData(tableName, columnName, (String) mo.get(columnName), priKey, "'" + mo.get(priKey).toString() + "'");
                }
            }
            return true;
        } else if (dbType.equals("MongoDB")) {
            //只有包含才替换
            for (Map<String, Object> mo : columnData) {
                if (mo.containsKey(columnName)) {
                    mongoDao.setDataByKey(tableName, columnName, (String) mo.get(columnName), (String) mo.get("_id"));
                }
            }

            return true;
        }
        return false;
    }

    //字段补全
    public boolean completFiled(String dbType, String tableName, String columnName, String completType, String defaultValue, Object dbConnect) {
        Double compleValue;
        if (dbType.equals("MySQL")) {
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) dbConnect;
            SqlSession session = sqlSessionFactory.openSession();
            mysqlMapper = session.getMapper(MysqlMapper.class);
            if (completType.equals("average")) {
                compleValue = mysqlMapper.getAverage(tableName, columnName);
                //  System.out.println("average:  " + compleValue);

            } else if (completType.equals("mode")) {
                List<Map<String, Object>> mp = mysqlMapper.getMode(tableName, columnName);
                System.out.println("mode:  " + mp.toString());
                compleValue = Double.parseDouble(mp.get(0).get(columnName).toString());

            } else if (completType.equals("median")) {
                List<Double> number = mysqlMapper.getColumnDouble(tableName, columnName);
                //  System.out.println("source:  "+number);
                //  System.out.println("source:  "+number.size());
                while (number.contains(null)) {
                    number.remove(null);
                }
                Collections.sort(number);
                //  System.out.println("target:  "+number);
                //  System.out.println("target:  "+number.size());

                int size = number.size();
                if (size % 2 == 0) {
                    System.out.println("ou数个：  " + size / 2);
                    compleValue = (number.get((size >> 1)) + number.get(((size >> 1) - 1))) / 2;
                } else {
                    //      System.out.println("奇数个：  " + size/2);
                    compleValue = number.get(size >> 1);
                }
                System.out.println("median:  " + compleValue);
            } else {
                compleValue = Double.parseDouble(defaultValue);
                System.out.println("customize:  " + compleValue);
            }
            mysqlMapper.completFiled(tableName, columnName, compleValue);
            return true;
        } else if (dbType.equals("PostgreSQL")) {
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) dbConnect;
            SqlSession session = sqlSessionFactory.openSession();
            postgresqlMapper = session.getMapper(PostgresqlMapper.class);
            if (completType.equals("average")) {
                compleValue = postgresqlMapper.getAverage(tableName, columnName);
                System.out.println("average:  " + compleValue);

            } else if (completType.equals("mode")) {
                List<Map<String, Object>> mp = postgresqlMapper.getMode(tableName, columnName);
                System.out.println("mode:  " + mp.toString());
                compleValue = Double.parseDouble(mp.get(0).get(columnName).toString());

            } else if (completType.equals("median")) {
                List<Double> number = postgresqlMapper.getColumnDouble(tableName, columnName);
                System.out.println("source:  " + number);
                System.out.println("source:  " + number.size());
                while (number.contains(null)) {
                    number.remove(null);
                }
                Collections.sort(number);
                System.out.println("target:  " + number);
                System.out.println("target:  " + number.size());

                int size = number.size();
                if (size % 2 == 0) {
                    System.out.println("ou数个：  " + size / 2);
                    compleValue = (number.get((size >> 1)) + number.get(((size >> 1) - 1))) / 2;
                } else {
                    System.out.println("奇数个：  " + size / 2);
                    compleValue = number.get(size >> 1);
                }
                System.out.println("median:  " + compleValue);
            } else {
                compleValue = Double.parseDouble(defaultValue);
                System.out.println("customize:  " + compleValue);
            }
            postgresqlMapper.completFiled(tableName, columnName, compleValue);
            return true;

        } else if (dbType.equals("MongoDB")) {
            MongoDbFactory mongoDbFactory = (MongoDbFactory) dbConnect;
            MongoDao mongoDao = new MongoDao(mongoDbFactory);

            if (completType.equals("average")) {
                List<Map> data = mongoDao.getDataByKey(tableName, columnName);
                Double sum = 0.0;
                System.out.println("data: average :" + data);
                for (Map num : data) {
                    if (num.get(columnName) == null) {
                        System.out.println(num);
                        System.out.println(num.get(columnName));
                        continue;
                    }
                    sum += Double.parseDouble(num.get(columnName).toString());
                }
                compleValue = sum / data.size();
                System.out.println("平均数：  " + compleValue);
            } else if (completType.equals("mode")) {
                compleValue = mongoDao.getMode(tableName, columnName);
                System.out.println("众数：  " + compleValue);
            } else if (completType.equals("median")) {

                List<Map> data = mongoDao.getDataByKey(tableName, columnName);
                List<Double> numbers = new ArrayList<>();
                for (Map num : data) {
                    if (num.get(columnName) == null) {
                        System.out.println(num);
                        continue;
                    }
                    numbers.add(Double.parseDouble(num.get(columnName).toString()));
                }
                System.out.println(numbers);

                Collections.sort(numbers);


                int size = numbers.size();
                if (size % 2 == 0) {
                    compleValue = (numbers.get((size >> 1)) + numbers.get(((size >> 1) - 1))) / 2;
                } else {
                    compleValue = numbers.get(size >> 1);
                }
                System.out.println("中位数：  " + compleValue);
            } else {
                compleValue = Double.parseDouble(defaultValue);
                System.out.println("customize:  " + compleValue);
            }
            mongoDao.completFiled(tableName, columnName, compleValue);
            //  mysqlMapper.completFiled(tableName,columnName,compleValue);
            return true;

        } else {
            System.out.println("error");
        }
        return false;
    }


    //校验数据类型
    public List<Map<String, Object>> dataVerify(String dbType, String tableName, String columnName, String priKey, String regularExpress, Object dbConnect) {
        List<Map<String, Object>> origin = null;
        List<Map<String, Object>> notMatchList = new ArrayList<>();

        if (dbType.equals("MySQL")) {
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) dbConnect;
            SqlSession session = sqlSessionFactory.openSession();
            mysqlMapper = session.getMapper(MysqlMapper.class);
            origin = mysqlMapper.getColumnData(tableName, columnName, priKey);
        } else if (dbType.equals("PostgreSQL")) {
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) dbConnect;
            SqlSession session = sqlSessionFactory.openSession();
            postgresqlMapper = session.getMapper(PostgresqlMapper.class);
            origin = postgresqlMapper.getColumnData(tableName, columnName, priKey);
        } else if (dbType.equals("MongoDB")) {
            MongoDbFactory mongoDbFactory = (MongoDbFactory) dbConnect;
            mongoDao = new MongoDao(mongoDbFactory);
            origin = mongoDao.getDataByKey(tableName, columnName);
        } else {
            System.out.println("error");
            return null;
        }

        //统计数字 ^\d+(\.\d+)?
        System.out.println(regularExpress);
        for (Map<String, Object> o : origin) {
            System.out.println("子元素： " + o);
            if (o.get(columnName) == null) {
                System.out.println("为空：  " + o.get(priKey));
                o.put(columnName, "null");
                notMatchList.add(o);
            } else if (!Pattern.matches(regularExpress, o.get(columnName).toString())) {
                System.out.println("不匹配：  " + o.get(priKey) + "   " + o.get(columnName));
                notMatchList.add(o);
            }
        }
        System.out.println(notMatchList);
        return notMatchList;
    }

    public boolean saveUpdateDate(String dbType, String tableName, String columnName, String priKey, List<Map<String, Object>> data, Object dbConnect) {
        if (dbType.equals("MySQL")) {
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) dbConnect;
            SqlSession session = sqlSessionFactory.openSession();
            mysqlMapper = session.getMapper(MysqlMapper.class);
            for (Map<String, Object> mp : data) {
                if (mp.get("targetdata") != null) {
                    mysqlMapper.setColumnData(tableName, columnName, mp.get("targetdata").toString(), priKey, mp.get("id").toString());
                }
            }
            return true;
        } else if (dbType.equals("PostgreSQL")) {
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) dbConnect;
            SqlSession session = sqlSessionFactory.openSession();
            postgresqlMapper = session.getMapper(PostgresqlMapper.class);
            for (Map<String, Object> mp : data) {
                if (mp.get("targetdata") != null) {
                    postgresqlMapper.setColumnData(tableName, columnName, mp.get("targetdata").toString(), priKey, mp.get("id").toString());
                }
            }
            return true;
        } else if (dbType.equals("MongoDB")) {
            MongoDbFactory mongoDbFactory = (MongoDbFactory) dbConnect;
            mongoDao = new MongoDao(mongoDbFactory);
            for (Map<String, Object> mp : data) {
                if (mp.get("targetdata") != null) {
                    mongoDao.setDataByKey(tableName, columnName, mp.get("targetdata").toString(), mp.get("id").toString());
                }
            }
            return true;


        } else {
            System.out.println("error");
        }
        return false;

    }

    public List<Map<String, Object>> replaceString(String dbType, String tableName, String columnName, String priKey, String regularExpress, String targetString, Object dbConnect) {
        List<Map<String, Object>> origin = null;
        if (dbType.equals("MySQL")) {
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) dbConnect;
            SqlSession session = sqlSessionFactory.openSession();
            mysqlMapper = session.getMapper(MysqlMapper.class);
            //原始数据
            origin = mysqlMapper.getColumnData(tableName, columnName, priKey);
        } else if (dbType.equals("PostgreSQL")) {
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) dbConnect;
            SqlSession session = sqlSessionFactory.openSession();
            postgresqlMapper = session.getMapper(PostgresqlMapper.class);
            origin = postgresqlMapper.getColumnData(tableName, columnName, priKey);
        } else if (dbType.equals("MongoDB")) {
            MongoDbFactory mongoDbFactory = (MongoDbFactory) dbConnect;
            mongoDao = new MongoDao(mongoDbFactory);
            origin = mongoDao.getDataByKey(tableName, columnName);
        } else {
            System.out.println("error");
            return null;
        }
        System.out.println(regularExpress);
        for (Map<String, Object> o : origin) {
            System.out.println("子元素： " + o);
            if (o.get(columnName) == null) {
                System.out.println("为空：  " + o.get(priKey));
                o.put(columnName, "null");
            } else {
                String replace = o.get(columnName).toString().replaceAll(regularExpress, targetString);
                o.put("afterReplace", replace);
            }
        }
        System.out.println(origin);
        return origin;
    }
}
