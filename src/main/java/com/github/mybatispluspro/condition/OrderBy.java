package com.github.mybatispluspro.condition;

import com.github.mybatispluspro.core.IStep;

/**
 * @author niuzhenhao
 * @date 2020/9/15 14:45
 * @desc
 */

public class OrderBy extends IStep {

    private String order;

    public OrderBy(String column, String table, String order) {
        super(column, table);
        this.order = order;
    }

    @Override
    public String toSql() {
        return ORDER_BY + table + "." + column + " " + order ;
    }
}
