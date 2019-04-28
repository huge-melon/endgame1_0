package com.shixin.endgame.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MyMapper {
    List<Map<String, Object>> getData(@Param("table_name") String tableName);

    List<Map<String, Object>> getTables();
}
