package com.example.lebarto.wifidirecttest.model;

import android.util.Log;

import com.example.logic_model.model.Action;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

public class ChatManager implements Runnable {

    private final String hostAddress;
    private Socket socket = null;

    public ChatManager(Socket socket, String hostAddress) {
        this.hostAddress = hostAddress;
        this.socket = socket;
    }

    private ObjectInputStream iStream;
    private ObjectOutputStream oStream;
    private static final String TAG = "ChatHandler";

    @Override
    public void run() {
        try {

            iStream = new ObjectInputStream(socket.getInputStream());
            oStream = new ObjectOutputStream(socket.getOutputStream());

            while (true) {
                try {
                    if (socket.isConnected()) {
                        Action action = (Action) iStream.readObject();
                        write(action.process());
                    } else {
                        reconnect();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    reconnect();
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void reconnect() {
        if (socket != null) {
            try {

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        while (true) {
            try {

                Thread.sleep(1000);
                socket = new Socket();
                socket.bind(null);
                socket.connect(new InetSocketAddress(hostAddress,
                    4545), 5000);
                Log.d(TAG, "reconnecting");
                run();
            } catch (InterruptedException e) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(Object buffer) {
        try {
            oStream.writeObject(buffer);
            oStream.flush();
        } catch (IOException e) {
            Log.e(TAG, "Exception during write", e);
        }
    }
}