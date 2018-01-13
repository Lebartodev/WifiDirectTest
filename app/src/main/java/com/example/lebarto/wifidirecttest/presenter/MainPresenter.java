package com.example.lebarto.wifidirecttest.presenter;

import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;

import com.example.lebarto.wifidirecttest.WiFiP2pService;
import com.example.lebarto.wifidirecttest.model.ClientListener;
import com.example.lebarto.wifidirecttest.model.MainModel;
import com.example.lebarto.wifidirecttest.view.WifiDirectListener;

/**
 * Created by lebarto on 08.01.2018.
 */

public class MainPresenter {
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private WifiDirectListener listener;
    private MainModel model = new MainModel();
    private WifiP2pDnsSdServiceRequest serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();

    public MainPresenter(WifiP2pManager manager, WifiP2pManager.Channel channel,
        WifiDirectListener listener) {
        this.manager = manager;
        this.channel = channel;
        this.listener = listener;
    }

    public void startUpdate() {
        model.discoverService(manager, channel, serviceRequest).subscribe(wiFiP2pService -> {
            listener.addService(wiFiP2pService);
        });
    }

    public void connectP2p(WiFiP2pService item) {
        model.connectP2p(item, serviceRequest, manager, channel).subscribe(wiFiP2pService -> {
            listener.serviceConnected(wiFiP2pService);
        });
    }

    public void connectionInfoAvailable(WifiP2pInfo info, ClientListener listener) {
        model.connectionInfoAviable(info, listener);
    }

    public void startCalculate() {
        model.calculate().subscribe(result -> {
            listener.onCalculated(result);
        });
    }
}
