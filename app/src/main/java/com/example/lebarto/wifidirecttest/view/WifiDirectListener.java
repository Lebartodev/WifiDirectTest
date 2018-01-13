package com.example.lebarto.wifidirecttest.view;

import com.example.lebarto.wifidirecttest.WiFiP2pService;

/**
 * Created by lebarto on 13.01.2018.
 */

public interface WifiDirectListener {
    void addService(WiFiP2pService wiFiP2pService);

    void serviceConnected(WiFiP2pService wiFiP2pService);

    void onCalculated(Integer result);
}
