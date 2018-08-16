package com.chou.android.network.bean;

/**
 * @author : zgz
 * @time :  2018/7/5 0005 15:34
 * @describe :
 **/
public class ShowListRequestBean {
    private int user_id;
    private int page;


    public int getUser_id() {
        return user_id;
    }


    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }


    public int getPage() {
        return page;
    }


    public void setPage(int page) {
        this.page = page;
    }
}
