package com.example.lebarto.wifidirecttest;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.util.Log;

/**
 * A BroadcastReceiver that notifies of important wifi p2p events.
 */
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {
    private static String TAG = "BroadcastReceiver";

    private WifiP2pManager manager;
    private Channel channel;
    private Service service;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel,
        Service service) {
        super();
        this.manager = manager;
        this.channel = channel;
        this.service = service;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, action);
        if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            if (manager == null) {
                return;
            }

            NetworkInfo networkInfo = (NetworkInfo) intent
                .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {
                Log.d(TAG,
                    "Connected to p2p network. Requesting network details");
                manager.requestConnectionInfo(channel, (ConnectionInfoListener) service);
            } else {
                // It's a disconnect
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
            .equals(action)) {

            WifiP2pDevice device = (WifiP2pDevice) intent
                .getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
            Log.d(TAG, "Device status -" + device.status);
        }
    }
}
