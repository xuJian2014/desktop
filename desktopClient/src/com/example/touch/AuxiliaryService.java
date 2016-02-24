package com.example.touch;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.touch.EasyTouchView;
import com.example.touch.EasyTouchView.ServiceListener;

public class AuxiliaryService extends Service implements ServiceListener {
    private Intent mIntent;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        // ����serviceʱһ�� ʵ��һ��TableShowView�����ҵ������fun()��������ע�ᵽwindowManager��
        super.onCreate();
        new EasyTouchView(this, this).initTouchViewEvent();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mIntent = intent;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void OnCloseService(boolean isClose) {
        stopService(mIntent);
    }
}