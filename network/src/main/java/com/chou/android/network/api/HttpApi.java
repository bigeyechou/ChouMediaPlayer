package com.chou.android.network.api;

import io.reactivex.Observable;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by 眼神 on 2018/3/27.
 *
 * 存放所有的Api
 */

public interface HttpApi {

    @POST("v2/video/show")
    Observable<ResponseBody> getShowListData(@QueryMap Map<String,String> map);
}
