package com.chou.android.network.api;

import com.chou.android.network.bean.BaseResponse;
import com.chou.android.network.bean.ShowListRequestBean;
import com.chou.android.network.bean.ShowVideoListBean;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by 眼神 on 2018/3/27.
 *
 * 存放所有的Api
 */

public interface HttpApi {
    /**
     * 秀场列表
     * @param bean
     * @return
     */
    @POST("v2/video/show")
    Observable<BaseResponse<ShowVideoListBean>> getShowListData(@Body ShowListRequestBean bean);


}
