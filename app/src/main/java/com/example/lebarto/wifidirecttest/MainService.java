package com.example.lebarto.wifidirecttest;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.IBinder;

import com.example.lebarto.wifidirecttest.model.ClientListener;
import com.example.lebarto.wifidirecttest.presenter.MainPresenter;
import com.example.lebarto.wifidirecttest.view.WifiDirectListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainService extends Service
    implements WifiP2pManager.ConnectionInfoListener, WifiDirectListener {
    private Map<Integer, WiFiP2pService> items = new HashMap<>();
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver receiver = null;
    private WifiP2pManager manager;
    public static final String INTENT_START_COMPUTING =
        "com.example.lebarto.wifidirecttest.INTENT_START_COMPUTING";
    public static final String DATA_START_COMPUTING =
        "com.example.lebarto.wifidirecttest.DATA_START_COMPUTING";

    public static final String INTENT_CONNECT_TO_SERVICE =
        "com.example.lebarto.wifidirecttest.CONNECT_TO_SERVICE";
    public static final String DATA_CONNECT_TO_SERVICE =
        "com.example.lebarto.wifidirecttest.CONNECT_TO_SERVICE_data";

    public static final String INTENT_CONNECTED_TO_SERVICE =
        "com.example.lebarto.wifidirecttest.CONNECTED_TO_SERVICE";
    public static final String DATA_CONNECTED_TO_SERVICE =
        "com.example.lebarto.wifidirecttest.CONNECTED_TO_SERVICE_data";

    public static final String INTENT_REQUEST_LIST =
        "com.example.lebarto.wifidirecttest.INTENT_REQUEST_LIST";
    public static final String INTENT_RESPONSE_LIST =
        "com.example.lebarto.wifidirecttest.INTENT_RESPONSE_LIST";
    public static final String DATA_RESPONSE_LIST =
        "com.example.lebarto.wifidirecttest.INTENT_RESPONSE_LIST_data";

    public static final String INTENT_CALCULATED =
        "com.example.lebarto.wifidirecttest.INTENT_CALCULATED";
    public static final String DATA_CALCULATED =
        "com.example.lebarto.wifidirecttest.INTENT_CALCULATED_data";

    public static final String INTENT_CLIENTS =
        "com.example.lebarto.wifidirecttest.INTENT_CLIENTS";
    public static final String DATA_CLIENTS =
        "com.example.lebarto.wifidirecttest.DATA_CLIENTS";

    private MainPresenter presenter;

    private BroadcastReceiver getListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            presenter.startUpdate();
        }
    };

    private BroadcastReceiver connectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            presenter.connectP2p(intent.getParcelableExtra(DATA_CONNECT_TO_SERVICE));
        }
    };

    private BroadcastReceiver computingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            presenter.startCalculate();
        }
    };

    public MainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        presenter = new MainPresenter(manager, channel, this);
        WiFiDirectBroadcastReceiver receiver =
            new WiFiDirectBroadcastReceiver(manager, channel, this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter
            .addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter
            .addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        registerReceiver(receiver, intentFilter);

        registerReceiver(getListReceiver, new IntentFilter(INTENT_REQUEST_LIST));
        registerReceiver(connectReceiver, new IntentFilter(INTENT_CONNECT_TO_SERVICE));
        registerReceiver(computingReceiver, new IntentFilter(INTENT_START_COMPUTING));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        presenter.connectionInfoAvailable(info, size -> {
            Intent intent = new Intent(INTENT_CLIENTS);
            intent.putExtra(DATA_CLIENTS, size);
            sendBroadcast(intent);
        });
    }

    @Override
    public void addService(WiFiP2pService wiFiP2pService) {
        items.put(wiFiP2pService.getDevice().deviceAddress.hashCode(), wiFiP2pService);
        Intent intent = new Intent(INTENT_RESPONSE_LIST);
        intent.putExtra(DATA_RESPONSE_LIST, new ArrayList<>(items.values()));
        sendBroadcast(intent);
    }

    @Override
    public void serviceConnected(WiFiP2pService wiFiP2pService) {
        Intent intent = new Intent(INTENT_CONNECTED_TO_SERVICE);
        intent.putExtra(DATA_CONNECTED_TO_SERVICE, wiFiP2pService.getDevice());
        sendBroadcast(intent);
    }

    @Override
    public void onCalculated(Integer result) {
        Intent intent = new Intent(INTENT_CALCULATED);
        intent.putExtra(DATA_CALCULATED, result);
        sendBroadcast(intent);
    }
}
