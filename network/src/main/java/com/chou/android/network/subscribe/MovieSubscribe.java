package com.chou.android.network.subscribe;

import com.chou.android.network.utils.RetrofitFactory;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import java.util.HashMap;
import java.util.Map;
import okhttp3.ResponseBody;

/**
 * Created by 眼神 on 2018/3/27.
 */

public class MovieSubscribe {

    /**
     * 获取视频列表页面
     */
    public static void getShowList(DisposableObserver<ResponseBody> subscriber, String page) {
        Map<String,String> map = new HashMap<>();
        map.put("page",page);
        map.put("user_id","32");
        Observable observable =  RetrofitFactory.getInstance().getHttpApi().getShowListData(map);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

}
