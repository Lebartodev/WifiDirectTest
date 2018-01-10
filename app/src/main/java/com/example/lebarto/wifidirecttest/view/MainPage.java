package com.example.lebarto.wifidirecttest.view;

import com.example.lebarto.wifidirecttest.WiFiP2pService;

import java.util.List;

/**
 * Created by lebarto on 02.01.2018.
 */

public interface MainPage {

    void serviceConnected(WiFiP2pService wiFiP2pService);

    void onCalculated(Integer result);

    void addService(WiFiP2pService wiFiP2pService);
}
