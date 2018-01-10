package com.example.lebarto.wifidirecttest.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.example.lebarto.wifidirecttest.R;
import com.example.lebarto.wifidirecttest.WiFiDirectBroadcastReceiver;
import com.example.lebarto.wifidirecttest.WiFiP2pService;
import com.example.lebarto.wifidirecttest.presenter.MainPresenter;

import java.util.List;

public class MainActivity extends AppCompatActivity
    implements MainPage, WifiP2pManager.ConnectionInfoListener {
    EditText editText;
    private static final String TAG = "MainActivity";
    private final IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver receiver = null;
    private WifiP2pManager manager;
    private MainPresenter presenter;
    private DevicesAdapter adapter;

    private PullRefreshLayout pullRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView list = findViewById(R.id.list);
        pullRefreshLayout = findViewById(R.id.refresh_devices);

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
        adapter = new DevicesAdapter(presenter);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        presenter.startUpdate();
        pullRefreshLayout.setRefreshing(true);
        pullRefreshLayout.setOnRefreshListener(() -> {
            adapter.clear();
            presenter.startUpdate();});
        findViewById(R.id.button).setOnClickListener(v -> {
            presenter.startCalculate();
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

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        presenter.connectionInfoAvailable(info);
    }


    @Override
    public void addService(WiFiP2pService wiFiP2pService) {
        pullRefreshLayout.setRefreshing(false);
        adapter.add(wiFiP2pService);
    }

    @Override
    public void serviceConnected(WiFiP2pService wiFiP2pService) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCalculated(Integer result) {
        Toast.makeText(this, "Result:" + result, Toast.LENGTH_SHORT).show();
    }
}
