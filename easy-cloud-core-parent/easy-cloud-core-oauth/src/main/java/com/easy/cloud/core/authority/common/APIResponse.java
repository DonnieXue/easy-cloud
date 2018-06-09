package com.easy.cloud.core.authority.common;


/**
 * @author 无心
 * 接口统一返回封装类
 */
public class APIResponse<T> {


    private Integer code;

    private String message;

    private T data;


    /**
     * 提供对外个体转换方法
     * @return
     */
    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }


    /**
     * 提供构造方法 可写为单例 待考虑
     * @return
     */
    public static APIResponse build() {
        return new APIResponse();
    }


    /**
     * 提供属性注入方法
     */
    public void code(Integer code) {
        this.code = code;
    }

    public void message(String message) {
        this.message = message;
    }

    public void data(T data) {
        this.data = data;
    }


}
