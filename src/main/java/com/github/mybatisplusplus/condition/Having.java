package com.github.mybatisplusplus.condition;

import com.github.mybatisplusplus.core.IStep;

/**
 * @author niuzhenhao
 * @date 2020/9/14 17:39
 * @desc
 */

public class Having extends IStep {


    public Having(String column, String table, Object ... param) {
        super(column, table);
    }

    @Override
    public String toSql() {
        //todo 暂不支持
        return null;
    }
}
