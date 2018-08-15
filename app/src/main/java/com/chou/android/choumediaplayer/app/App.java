package com.chou.android.choumediaplayer.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import com.chou.android.network.AppUtils;
import com.danikula.videocache.HttpProxyCacheServer;
import java.util.ArrayList;

public class App extends Application {

    private HttpProxyCacheServer proxy;
    public static Context appContext;
    public static App app;

    @Override public void onCreate() {
        super.onCreate();
        app = this;
        AppUtils.init(app);
    }

    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
            .maxCacheSize(1024 * 1024 * 1024) // 缓存 1 Gb
            .maxCacheFilesCount(20) // 最大缓存20个文件
            .build();
    }

}
