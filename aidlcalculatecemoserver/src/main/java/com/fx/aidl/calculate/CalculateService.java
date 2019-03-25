package com.fx.aidl.calculate;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * FileName: CalculateService
 * Author: fengxue
 * Date: 2019/3/25 10:18 AM
 * Description: ${DESCRIPTION}
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */

public class CalculateService extends Service {

    private static final String TAG = "CalculateService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogE("onBind()");
        return mBinder;
    }

    @Override
    public void onCreate() {
        LogE("onCreate()");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogE("onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogE("onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        LogE("onDestroy()");
        super.onDestroy();
    }

    public void LogE(String str) {
        Log.e(TAG, "-------------------" + str + "---------------------");
    }

    private final CalculateInterface.Stub mBinder=new CalculateInterface.Stub() {

        @Override
        public double doCalculate(double a, double b) throws RemoteException {
            LogE("远程计算中");
            Calculate calculate=new Calculate();
            double answer=calculate.calculateSum(a,b);
            return answer;
        }
    };
}
