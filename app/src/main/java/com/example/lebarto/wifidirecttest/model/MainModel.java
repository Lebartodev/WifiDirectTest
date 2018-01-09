package com.example.lebarto.wifidirecttest.model;

import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
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

    private Observable<WiFiP2pService> discoverService(WifiP2pManager manager,
        WifiP2pManager.Channel channel) {
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

                    // A service has been discovered. Is this our app?

                    if (instanceName.equalsIgnoreCase(SERVICE_INSTANCE)) {

                        WiFiP2pService wiFiP2pService = new WiFiP2pService();
                        wiFiP2pService.setDevice(srcDevice);
                        wiFiP2pService.setInstanceName(instanceName);
                        wiFiP2pService.setServiceRegistrationType(registrationType);
                        e.onNext(wiFiP2pService);
                    }
                }, new WifiP2pManager.DnsSdTxtRecordListener() {

                    @Override
                    public void onDnsSdTxtRecordAvailable(
                        String fullDomainName, Map<String, String> record,
                        WifiP2pDevice device) {
                        Log.d(TAG,
                            device.deviceName + " is "
                                + record.get(TXTRECORD_PROP_AVAILABLE));
                    }
                });
        });
    }

    public Single<WiFiP2pService> connectP2p(WiFiP2pService service,
        WifiP2pDnsSdServiceRequest serviceRequest, WifiP2pManager manager,
        WifiP2pManager.Channel channel) {

        return Single.create(e -> {
            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = service.getDevice().deviceAddress;
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

    public void onConnectionInfoAvailable(WifiP2pInfo p2pInfo) {
        Thread handler = null;

        if (p2pInfo.isGroupOwner) {
            Log.d(TAG, "Connected as group owner");
            //Toast.makeText(this, "Connected as group owner", Toast.LENGTH_SHORT).show();
            try {
                groupOwnerSocketHandler =
                    new GroupOwnerSocketHandler(this);

                findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringBuilder stringBuilder = new StringBuilder();
                        try {
                            // Get the object of DataInputStream
                            FileInputStream is;
                            BufferedReader reader;
                            final File file = new File("/storage/emulated/0/bible.txt");

                            if (file.exists()) {
                                is = new FileInputStream(file);
                                reader = new BufferedReader(new InputStreamReader(is));
                                String line;
                                for (int i = 0; i < 1000; i++) {
                                    line = reader.readLine();
                                    stringBuilder.append(line);
                                }

                                Log.d(TAG, "onClick: " + System.currentTimeMillis());
                            }
                        } catch (IOException e) {
                        }
                        groupOwnerSocketHandler.setText(
                            stringBuilder.toString().substring(0, stringBuilder.length() / 2));

                        onTextCalculated(WordCount
                            .proccess(
                                stringBuilder.toString().substring(0, stringBuilder.length() / 2)));
                    }
                });
                groupOwnerSocketHandler.start();
            } catch (IOException e) {
                Log.d(TAG,
                    "Failed to create a server thread - " + e.getMessage());
                return;
            }
        } else {
            Log.d(TAG, "Connected as peer");
            Toast.makeText(this, "Connected as peer", Toast.LENGTH_SHORT).show();
            handler = new ClientSocketHandler(
                p2pInfo.groupOwnerAddress);
            handler.start();
        }
    }
}
