package com.chou.android.network.subscribe;

import com.chou.android.network.bean.BaseResponse;
import com.chou.android.network.bean.ShowListRequestBean;
import com.chou.android.network.bean.ShowVideoListBean;
import com.chou.android.network.utils.RetrofitFactory;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

/**
 * Created by 眼神 on 2018/3/27.
 */

public class MovieSubscribe {

    /**
     * 获取秀场列表页面
     */
    public static void getShowList(DisposableObserver<BaseResponse<ShowVideoListBean>> subscriber, int page, int userId) {
        ShowListRequestBean bean = new ShowListRequestBean();
        bean.setPage(page);
        bean.setUser_id(userId);
        Observable observable =  RetrofitFactory.getInstance().getHttpApi().getShowListData(bean);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }
}
