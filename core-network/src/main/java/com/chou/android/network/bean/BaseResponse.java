package com.chou.android.network.bean;

/**
 * @author : zgz
 * @time :  2018/7/10 0010 13:51
 * @describe :
 **/
public class BaseResponse<T> {
    private int code;
    private String msg;
    private T obj;


    public int getCode() {
        return code;
    }


    public void setCode(int code) {
        this.code = code;
    }


    public String getMsg() {
        return msg;
    }


    public void setMsg(String msg) {
        this.msg = msg;
    }


    public T getObj() {
        return obj;
    }


    public void setObj(T obj) {
        this.obj = obj;
    }

}
