package com.github.mybatispluspro.core;

/**
 * @author niuzhenhao
 * @date 2020/9/9 17:42
 * @desc
 */

public class F<T> {

    public static <T> IGet<T> f(IGet<T> f) {
        return f;
    }
}
