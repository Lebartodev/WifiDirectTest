package com.example.lebarto.wifidirecttest.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.baoyz.widget.PullRefreshLayout;
import com.example.lebarto.wifidirecttest.MainService;
import com.example.lebarto.wifidirecttest.R;
import com.example.lebarto.wifidirecttest.WiFiP2pService;

import java.util.List;

public class MainActivity extends AppCompatActivity
    implements MainPage {
    EditText editText;
    private static final String TAG = "MainActivity";
    private DevicesAdapter adapter;

    private PullRefreshLayout pullRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, MainService.class));
        RecyclerView list = findViewById(R.id.list);
        pullRefreshLayout = findViewById(R.id.refresh_devices);

        editText = findViewById(R.id.text);

        adapter = new DevicesAdapter();
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        pullRefreshLayout.setOnRefreshListener(() -> {
            adapter.clear();
            sendBroadcast(new Intent(MainService.INTENT_REQUEST_LIST));
        });
        findViewById(R.id.button).setOnClickListener(v -> {
            sendBroadcast(new Intent(MainService.INTENT_START_COMPUTING));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //receiver = new WiFiDirectBroadcastReceiver(manager, channel, this);
        registerReceiver(listResponseReceiver, new IntentFilter(MainService.INTENT_RESPONSE_LIST));
        registerReceiver(connectReceiver,
            new IntentFilter(MainService.INTENT_CONNECTED_TO_SERVICE));
        registerReceiver(calculatedReceiver, new IntentFilter(MainService.INTENT_CALCULATED));
        registerReceiver(clientsReceiver, new IntentFilter(MainService.INTENT_CLIENTS));
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregisterReceiver(receiver);
        unregisterReceiver(listResponseReceiver);
        unregisterReceiver(connectReceiver);
        unregisterReceiver(calculatedReceiver);
        unregisterReceiver(clientsReceiver);
    }

    private BroadcastReceiver listResponseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pullRefreshLayout.setRefreshing(false);
            adapter.setItems(
                (List<WiFiP2pService>) intent.getSerializableExtra(MainService.DATA_RESPONSE_LIST));
        }
    };

    private BroadcastReceiver connectReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            adapter.connectItem(intent.getParcelableExtra(MainService.DATA_CONNECTED_TO_SERVICE));
        }
    };

    private BroadcastReceiver clientsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(MainActivity.this,
                "Clients size:" + intent.getIntExtra(MainService.DATA_CLIENTS, 0),
                Toast.LENGTH_SHORT).show();
        }
    };

    private BroadcastReceiver calculatedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            new MaterialDialog.Builder(context)
                .title("Result")
                .content(String.valueOf(intent.getIntExtra(MainService.DATA_CALCULATED, 0)))
                .positiveText("Ok")
                .show();
        }
    };
}
