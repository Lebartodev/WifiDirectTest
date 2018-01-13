package com.example.lebarto.wifidirecttest.view;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lebarto.wifidirecttest.MainService;
import com.example.lebarto.wifidirecttest.R;
import com.example.lebarto.wifidirecttest.WiFiP2pService;
import com.example.lebarto.wifidirecttest.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lebarto on 01.01.2018.
 */

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ArticleVH> {

    private List<WiFiP2pService> items = new ArrayList<>();

    public DevicesAdapter() {
    }

    public void setItems(
        List<WiFiP2pService> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public ArticleVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_device, parent, false);
        return new ArticleVH(v);
    }

    @Override
    public void onBindViewHolder(ArticleVH holder, int position) {
        final WiFiP2pService item = items.get(position);

        holder.name.setText(item.getDevice().deviceName);
        holder.status
            .setText(item.isConnected() ? "Connected" : getDeviceStatus(item.getDevice().status));
        holder.mainView.setOnClickListener(v -> {

            Intent intent = new Intent(MainService.INTENT_CONNECT_TO_SERVICE);
            intent.putExtra(MainService.DATA_CONNECT_TO_SERVICE, item);
            holder.mainView.getContext().sendBroadcast(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void clear() {
        items = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void connectItem(Parcelable parcelableExtra) {
        WifiP2pDevice device = (WifiP2pDevice) parcelableExtra;
        for (WiFiP2pService item : items) {
            if (item.getDevice().deviceAddress.equals(device.deviceAddress)) {
                item.setConnected(true);
                notifyDataSetChanged();
            }
        }
    }

    class ArticleVH extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView status;
        private View mainView;

        public ArticleVH(View itemView) {

            super(itemView);

            this.mainView = itemView;
            name = (TextView) itemView.findViewById(R.id.name);
            status = (TextView) itemView.findViewById(R.id.status);
        }
    }

    public static String getDeviceStatus(int statusCode) {
        switch (statusCode) {
        case WifiP2pDevice.CONNECTED:
            return "Connected";
        case WifiP2pDevice.INVITED:
            return "Invited";
        case WifiP2pDevice.FAILED:
            return "Failed";
        case WifiP2pDevice.AVAILABLE:
            return "Available";
        case WifiP2pDevice.UNAVAILABLE:
            return "Unavailable";
        default:
            return "Unknown";
        }
    }
}
