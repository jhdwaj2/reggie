package com.lyc.reggie.common;

/**
 * Date: 2022/5/18
 * Author: 3378
 * Description:
 */
public class BaseContent {

    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }

}
