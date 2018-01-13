package com.example.lebarto.wifidirecttest;

import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by lebarto on 01.01.2018.
 */

public class WiFiP2pService implements Parcelable {
    private WifiP2pDevice device;
    private boolean connected;

    public WiFiP2pService() {
    }

    protected WiFiP2pService(Parcel in) {
        device = in.readParcelable(WifiP2pDevice.class.getClassLoader());
        connected = in.readByte() != 0;
    }

    public static final Creator<WiFiP2pService> CREATOR = new Creator<WiFiP2pService>() {
        @Override
        public WiFiP2pService createFromParcel(Parcel in) {
            return new WiFiP2pService(in);
        }

        @Override
        public WiFiP2pService[] newArray(int size) {
            return new WiFiP2pService[size];
        }
    };

    public WifiP2pDevice getDevice() {
        return device;
    }

    public void setDevice(WifiP2pDevice device) {
        this.device = device;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(device, flags);
        dest.writeByte((byte) (connected ? 1 : 0));
    }
}
