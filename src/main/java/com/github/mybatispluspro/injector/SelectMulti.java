package com.github.mybatispluspro.injector;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.scripting.xmltags.*;

import java.util.Collections;

/**
 * @author niuzhenhao
 * @date 2020/9/11 18:00
 * @desc
 */

public class SelectMulti extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = " \n ${query.sql} \n";

        SqlNode sqlNode = new MixedSqlNode(Collections.singletonList(new TextSqlNode(sql)));
        SqlSource sqlSource = new DynamicSqlSource(configuration, sqlNode);
        return this.addSelectMappedStatementForTable(mapperClass, "selectMulti", sqlSource, tableInfo);
    }
}
