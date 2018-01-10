package com.example.lebarto.wifidirecttest.model;

import android.util.Log;

import com.example.lebarto.wifidirecttest.actions.Action;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class GroupOwnerSocketHandler extends Thread implements ServerBase {
    private ServerSocket socket = null;
    private static final String TAG = "GroupOwnerSocketHandler";
    private List<Client> clients = new ArrayList<>();
    private OnDoneListener listener;

    private int doneCount = 0;

    public GroupOwnerSocketHandler() throws IOException {
        try {
            socket = new ServerSocket(4545);
            Log.d("GroupOwnerSocketHandler", "Socket Started");
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Client client = new Client(socket.accept(), this);
                client.start();
                clients.add(client);
                Log.d(TAG, "Launching the I/O handler");
            } catch (IOException e) {
                try {
                    if (socket != null && !socket.isClosed()) {
                        socket.close();
                    }
                } catch (IOException ioe) {

                }
                e.printStackTrace();
                break;
            }
        }
    }

    public void sendAction(Action action) {
        doneCount = 0;
        for (Client client : clients) {
            try {
                client.getOos().writeObject(action);
                client.getOos().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClientDone(int count) {
        doneCount++;
        if (doneCount == clients.size()) {
            if (listener != null) {
                listener.onTextCalculated(count);
            }
        }
    }

    public void setListener(OnDoneListener listener) {
        this.listener = listener;
    }

    public int getClientsSize() {
        return clients.size();
    }
}