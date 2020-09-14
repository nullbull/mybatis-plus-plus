package com.github.mybatispluspro.condition.single;

/**
 * @author niuzhenhao
 * @date 2020/9/14 17:23
 * @desc
 */

public class Lt extends SingleCondition {
    public Lt(String column, String table, Object value, int param) {
        super(column, table, value, param);
        this.condition = LT;
    }
}
