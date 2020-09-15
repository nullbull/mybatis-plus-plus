package com.github.mybatispluspro.condition.single;

/**
 * @author niuzhenhao
 * @date 2020/9/15 11:15
 * @desc
 */

public class Like extends SingleCondition {

    public Like(String column, String table, String value, int param) {
        super(column, table, value, param);
        this.condition = LIKE;
    }


}
