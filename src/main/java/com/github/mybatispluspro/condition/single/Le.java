package com.github.mybatispluspro.condition.single;

/**
 * @author niuzhenhao
 * @date 2020/9/11 13:28
 * @desc
 */

public class Le extends SingleCondition {

    public Le(String column, String table, Object value, int param) {
        super(column, table, value, param);
        this.condition = LE;
    }
}
