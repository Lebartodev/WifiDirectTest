package com.example.lebarto.wifidirecttest.presenter;

import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;

import com.example.lebarto.wifidirecttest.WiFiP2pService;
import com.example.lebarto.wifidirecttest.model.MainModel;
import com.example.lebarto.wifidirecttest.view.MainPage;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lebarto on 08.01.2018.
 */

public class MainPresenter {
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private MainPage mPage;
    private MainModel model = new MainModel();
    WifiP2pDnsSdServiceRequest serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();

    public MainPresenter(WifiP2pManager manager, WifiP2pManager.Channel channel,
        MainPage mPage) {
        this.manager = manager;
        this.channel = channel;
        this.mPage = mPage;
    }

    public void startUpdate(){
        model.discoverService(manager,channel,serviceRequest).subscribe(wiFiP2pService -> {
                mPage.addService(wiFiP2pService);
            });

    }

    public void connectP2p(WiFiP2pService item) {
        model.connectP2p(item,serviceRequest,manager,channel).subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe(wiFiP2pService -> {
            mPage.serviceConnected(wiFiP2pService);
        });

    }

    public void connectionInfoAvailable(WifiP2pInfo info) {
        model.connectionInfoAviable(info);
    }

    public void startCalculate() {
        model.calculate().subscribeOn(Schedulers.io()).observeOn(
            AndroidSchedulers.mainThread()).subscribe(result -> {
            mPage.onCalculated(result);
        });
    }
}
