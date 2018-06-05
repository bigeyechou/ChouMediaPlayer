package com.chou.android.choumediaplayer.app;

import android.app.Application;
import android.content.Context;
import com.danikula.videocache.HttpProxyCacheServer;

public class App extends Application {

    private HttpProxyCacheServer proxy;

    @Override public void onCreate() {
        super.onCreate();
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
