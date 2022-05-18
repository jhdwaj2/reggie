package com.lyc.reggie.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 2022/5/15
 * Author: 3378
 * Description: 通用返回结果类,服务端响应的数据最终都会封装成此对象
 */
@Data
public class R<T> {

    private Integer code;   //状态码,1标识成功,0标识失败
    private String msg;     //提示消息
    private T data;         //封装的的数据
    private Map map=new HashMap();  //动态数据


    public static <T> R<T> success(T object){
        R<T> r=new R<>();
        r.data=object;
        r.code=1;
        return r;
    }

    public static <T> R<T> error(String msg){
        R<T> r=new R<>();
        r.msg=msg;
        r.code=0;
        return r;
    }

    public R<T> add(String key,Object value){
        this.map.put(key,value);
        return this;
    }

}
