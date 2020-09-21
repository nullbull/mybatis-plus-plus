package com.github.mybatisplusplus.condition.single;

/**
 * @author niuzhenhao
 * @date 2020/9/15 11:17
 * @desc
 */

public class RightLike extends SingleCondition{

    public RightLike(String column, String table, String value, int param) {
        super(column, table, value + PERCENT_SIGN, param);
        this.condition = LIKE;
    }
}
