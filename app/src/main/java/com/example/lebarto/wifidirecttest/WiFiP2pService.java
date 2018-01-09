package com.example.lebarto.wifidirecttest;

import android.net.wifi.p2p.WifiP2pDevice;

/**
 * Created by lebarto on 01.01.2018.
 */

public class WiFiP2pService {
    private WifiP2pDevice device;
    private String instanceName = null;
    private String serviceRegistrationType = null;

    public WifiP2pDevice getDevice() {
        return device;
    }

    public void setDevice(WifiP2pDevice device) {
        this.device = device;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getServiceRegistrationType() {
        return serviceRegistrationType;
    }

    public void setServiceRegistrationType(String serviceRegistrationType) {
        this.serviceRegistrationType = serviceRegistrationType;
    }
}
