package com.chou.android.choumediaplayer.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import com.chou.android.network.AppUtils;
import com.danikula.videocache.HttpProxyCacheServer;
import java.util.ArrayList;

public class App extends Application {

    private HttpProxyCacheServer proxy;
    public static Context appContext;
    public static App app;
    public static ArrayList<Activity> allActivities = new ArrayList<Activity>();


    @Override public void onCreate() {
        super.onCreate();
        app = this;
        AppUtils.init(app);
    }


    public static App getApp() {
        return (App) appContext;
    }


    public static void addActivity(Activity activity) {
        allActivities.add(activity);
    }

    public static void delActivity(Activity activity) {
        allActivities.remove(activity);
    }

    public static void clearAllActivity() {
        for (Activity activity : allActivities) {
            activity.finish();
        }
        allActivities.clear();
        Process.killProcess(Process.myPid());
        System.exit(0);
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
