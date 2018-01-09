package com.example.lebarto.wifidirecttest.presenter;

import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.lebarto.wifidirecttest.R;
import com.example.lebarto.wifidirecttest.WiFiP2pService;
import com.example.lebarto.wifidirecttest.actions.WordCount;
import com.example.lebarto.wifidirecttest.model.ClientSocketHandler;
import com.example.lebarto.wifidirecttest.model.GroupOwnerSocketHandler;
import com.example.lebarto.wifidirecttest.model.MainModel;
import com.example.lebarto.wifidirecttest.view.MainPage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lebarto on 08.01.2018.
 */

public class MainPresenter {
    public static final String TXTRECORD_PROP_AVAILABLE = "available";
    public static final String SERVICE_INSTANCE = "_wifidemotest";
    public static final String SERVICE_REG_TYPE = "_presence._tcp";

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



}
