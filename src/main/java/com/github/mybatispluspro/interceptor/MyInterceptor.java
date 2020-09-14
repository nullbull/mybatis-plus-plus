package com.github.mybatispluspro.interceptor;

import com.github.mybatispluspro.core.Query;
import lombok.experimental.Accessors;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * @author niuzhenhao
 * @date 2020/9/14 10:42
 * @desc
 */
@Accessors(chain = true)
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class MyInterceptor implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler handler = realTarget(invocation.getTarget());
        MetaObject metaObject = MetaObject.forObject(handler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        MappedStatement statement = (MappedStatement)metaObject.getValue("delegate.mappedStatement");
        String statementId = statement.getId();

        //如果是多表关联查询
        if (statementId.endsWith("selectMulti")) {
            //获取查询参数
            BoundSql boundSql = handler.getBoundSql();
            Object object = boundSql.getParameterObject();
            if (object instanceof MapperMethod.ParamMap) {
                Query query = (Query) ((MapperMethod.ParamMap) object).get("query");
                Class resultType = query.getResultType();
                //获取 resultType，替换为自定义类
                List<ResultMap> resultMaps = statement.getResultMaps();
                ResultMap resultMap = resultMaps.get(0);
                ResultMap build = new ResultMap.Builder(statement.getConfiguration(), resultMap.getId(), resultType, new ArrayList<>()).build();
                metaObject.setValue("delegate.mappedStatement.resultMaps", Collections.singletonList(build));
            }
        }


        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }

    @SuppressWarnings("unchecked")
    private static <T> T realTarget(Object target) {
        if (Proxy.isProxyClass(target.getClass())) {
            MetaObject metaObject = SystemMetaObject.forObject(target);
            return realTarget(metaObject.getValue("h.target"));
        }
        return (T) target;
    }


}
