package com.example.lebarto.wifidirecttest.model;

import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.util.Log;

import com.example.lebarto.wifidirecttest.WiFiP2pService;
import com.example.lebarto.wifidirecttest.actions.Action;
import com.example.lebarto.wifidirecttest.actions.MapOperation;
import com.example.lebarto.wifidirecttest.actions.WordCount;
import com.example.lebarto.wifidirecttest.util.ActionUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by lebarto on 09.01.2018.
 */

public class MainModel {
    public static final String TXTRECORD_PROP_AVAILABLE = "available";
    public static final String SERVICE_INSTANCE = "_wifidemotest";
    public static final String SERVICE_REG_TYPE = "_presence._tcp";
    private static final String TAG = "MainModel";
    private GroupOwnerSocketHandler groupOwnerSocketHandler;

    public Observable<WiFiP2pService> discoverService(WifiP2pManager manager,
        WifiP2pManager.Channel channel, WifiP2pDnsSdServiceRequest serviceRequest) {

        Map<String, String> record = new HashMap<>();
        record.put(TXTRECORD_PROP_AVAILABLE, "visible");

        WifiP2pDnsSdServiceInfo service = WifiP2pDnsSdServiceInfo.newInstance(
            SERVICE_INSTANCE, SERVICE_REG_TYPE, record);
        manager.addLocalService(channel, service, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                Log.d(TAG, "Added Local Service");
            }

            @Override
            public void onFailure(int error) {
                Log.d(TAG, "Failed to add a service");
            }
        });

        return Observable.create(e -> {
                manager.setDnsSdResponseListeners(channel,
                    (instanceName, registrationType, srcDevice) -> {

                        WiFiP2pService wiFiP2pService = new WiFiP2pService();
                        wiFiP2pService.setDevice(srcDevice);
                        e.onNext(wiFiP2pService);
                    }, (fullDomainName, txtRecordMap, srcDevice) -> {

                    });

                manager.addServiceRequest(channel, serviceRequest,
                    new WifiP2pManager.ActionListener() {

                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Added service discovery request");
                        }

                        @Override
                        public void onFailure(int arg0) {
                            Log.d(TAG, "Failed adding service discovery request");
                        }
                    });
                manager.discoverServices(channel, new WifiP2pManager.ActionListener() {

                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "Service discovery initiated");
                    }

                    @Override
                    public void onFailure(int arg0) {
                        Log.d(TAG, "Service discovery failed");
                    }
                });
            }

        );
    }

    public Single<WiFiP2pService> connectP2p(WiFiP2pService service,
        WifiP2pDnsSdServiceRequest serviceRequest, WifiP2pManager manager,
        WifiP2pManager.Channel channel) {

        return Single.create(e -> {
            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = service.getDevice().deviceAddress;
            config.groupOwnerIntent = 0;
            config.wps.setup = WpsInfo.PBC;

            if (serviceRequest != null) {
                manager.removeServiceRequest(channel, serviceRequest,
                    new WifiP2pManager.ActionListener() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onFailure(int arg0) {
                        }
                    });
            }

            manager.connect(channel, config, new WifiP2pManager.ActionListener() {

                @Override
                public void onSuccess() {
                    e.onSuccess(service);
                    Log.d(TAG, "Connecting to service");
                }

                @Override
                public void onFailure(int errorCode) {
                    Log.d(TAG, "Failed connecting to service");
                }
            });
        });
    }

    public Single<Integer> calculate() {
        return Single.create(e -> {
            if (groupOwnerSocketHandler == null) {
                return;
            }
            groupOwnerSocketHandler.setListener(e::onSuccess);
            Action action = ActionUtil.fromTextFile("/storage/emulated/0/bible.txt")
                .add(new MapOperation(
                    (MapOperation.SAM & Serializable) s1 -> WordCount
                        .proccess(((List<String>) s1).get(0))))
                //.add(new MapOperation((MapOperation.SAM & Serializable) s1 -> ((Integer) s1) * 0))
                .collect();
            groupOwnerSocketHandler.sendAction(action);
        });
    }

    public void connectionInfoAviable(WifiP2pInfo p2pInfo, ClientListener listener) {
        Thread handler = null;

        if (p2pInfo.isGroupOwner) {
            Log.d(TAG, "Connected as group owner");
            try {
                groupOwnerSocketHandler = new GroupOwnerSocketHandler(listener);
                groupOwnerSocketHandler.start();
            } catch (IOException e) {
                Log.d(TAG,
                    "Failed to create a server thread - " + e.getMessage());
                return;
            }
        } else {
            Log.d(TAG, "Connected as peer");
            handler = new ClientSocketHandler(p2pInfo.groupOwnerAddress);
            handler.start();
        }
    }
}
