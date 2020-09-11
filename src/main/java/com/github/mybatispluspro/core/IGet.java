package com.github.mybatispluspro.core;

import java.io.Serializable;

/**
 * @author niuzhenhao
 * @date 2020/9/8 18:29
 * @desc
 */

@FunctionalInterface
public interface IGet<T> extends Serializable {

    Object get(T source);

}
