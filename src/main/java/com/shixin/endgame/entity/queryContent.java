package com.shixin.endgame.entity;


import com.shixin.endgame.common.QueryType;

import java.io.Serializable;

public class queryContent<T> implements Serializable {
    public String columnName;
    public QueryType queryType;// range like normal
    public T content;
    public T start;
    public T end;
    public String opr;// and or
}
