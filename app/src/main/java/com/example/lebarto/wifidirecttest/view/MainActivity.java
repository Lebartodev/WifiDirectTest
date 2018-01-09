package com.example.lebarto.wifidirecttest.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.example.lebarto.wifidirecttest.R;
import com.example.lebarto.wifidirecttest.WiFiDirectBroadcastReceiver;
import com.example.lebarto.wifidirecttest.WiFiP2pService;
import com.example.lebarto.wifidirecttest.model.GroupOwnerSocketHandler;
import com.example.lebarto.wifidirecttest.presenter.MainPresenter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
    implements DevicesAdapter.DeviceClickListener, MainPage {
    EditText editText;
    private static final String TAG = "MainActivity";
    private final IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver receiver = null;
    private WifiP2pDnsSdServiceRequest serviceRequest;
    private WifiP2pManager manager;
    private RecyclerView list;
    private DevicesAdapter adapter =
        new DevicesAdapter(new ArrayList<WiFiP2pService>(), this);

    private GroupOwnerSocketHandler groupOwnerSocketHandler;

    private PullRefreshLayout pullRefreshLayout;
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        pullRefreshLayout = findViewById(R.id.refresh_devices);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter
            .addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter
            .addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        editText = findViewById(R.id.text);

        presenter = new MainPresenter(manager, channel, this);
        presenter.startRegistrationAndDiscovery();
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startRegistrationAndDiscovery();
            }
        });
    }

    @Override
    public void onTextCalculated(final int count) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Count:" + count, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onTextCalculated: " + System.currentTimeMillis());
            }
        });
    }

    @Override
    public void connectP2p(WiFiP2pService service) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = service.device.deviceAddress;
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
                appendStatus("Connecting to service");
            }

            @Override
            public void onFailure(int errorCode) {
                appendStatus("Failed connecting to service");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public void appendStatus(String status) {
        Log.d(TAG, "appendStatus: " + status);
    }
}
