package com.ylfcf.ppp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * 预加载进程服务
 * Created by Administrator on 2017/9/15.
 */

public class PreloadProcessService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
