package com.example.lebarto.wifidirecttest.view;

import android.net.wifi.p2p.WifiP2pDevice;
import android.support.v7.widget.RecyclerView;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lebarto.wifidirecttest.R;
import com.example.lebarto.wifidirecttest.WiFiP2pService;

import java.util.List;

/**
 * Created by lebarto on 01.01.2018.
 */

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ArticleVH> {

    private List<WiFiP2pService> items;
    private DeviceClickListener deviceClickListener;

    public interface DeviceClickListener {
        void connectP2p(WiFiP2pService service);
    }

    public DevicesAdapter(List<WiFiP2pService> items,
        DeviceClickListener deviceClickListener) {
        this.items = items;
        this.deviceClickListener = deviceClickListener;
    }

    public void add(WiFiP2pService item) {
        items.add(item);
    }

    @Override
    public ArticleVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_device, parent, false);
        return new ArticleVH(v);
    }

    @Override
    public void onBindViewHolder(ArticleVH holder, int position) {
        final WiFiP2pService item = items.get(position);

        holder.name
            .setText(item.getDevice().deviceName);
        holder.status.setText(getDeviceStatus(item.getDevice().status));
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceClickListener.connectP2p(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
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