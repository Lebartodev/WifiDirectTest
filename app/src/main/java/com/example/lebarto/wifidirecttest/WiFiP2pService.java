package com.example.lebarto.wifidirecttest;

import android.net.wifi.p2p.WifiP2pDevice;

/**
 * Created by lebarto on 01.01.2018.
 */

public class WiFiP2pService {
    private WifiP2pDevice device;

    public WifiP2pDevice getDevice() {
        return device;
    }

    public void setDevice(WifiP2pDevice device) {
        this.device = device;
    }
}
