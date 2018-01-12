package com.example.lebarto.wifidirecttest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.lebarto.wifidirecttest.presenter.MainPresenter;

public class MainService extends Service {

    MainPresenter presenter=new MainPresenter();

    public MainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
