package com.github.mybatispluspro.core;

/**
 * @author niuzhenhao
 * @date 2020/9/11 12:26
 * @desc
 */

public class Ge extends SingleCondition {


    public Ge(String column, String table, Object value, int param) {
        super(column, table, value, param);
        this.condition = GE;
    }
}
