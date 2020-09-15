package com.github.mybatispluspro.condition.single;

import com.github.mybatispluspro.core.IStep;

/**
 * @author niuzhenhao
 * @date 2020/9/11 12:31
 * @desc
 */

public abstract class SingleCondition extends IStep {

    protected Object value;
    
    protected int param;

    protected String condition;
    
    public SingleCondition(String column, String table, Object value, int param) {
        super(column, table);
        this.value = value;
        this.param = param;
    }

    @Override
    public String toSql() {
        return  table + "." + column + condition + "#{query.param[" + param + "]}";
    }
}
