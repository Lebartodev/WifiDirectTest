package com.example.lebarto.wifidirecttest.model;

import android.util.Log;

import com.example.logic_model.model.Action;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class GroupOwnerSocketHandler extends Thread implements ServerBase {
    private ServerSocket socket = null;
    private static final String TAG = "GroupOwnerSocketHandler";
    private List<Client> clients = new ArrayList<>();
    private OnDoneListener listener;
    private ClientListener clientListener;

    private int doneCount = 0;

    public GroupOwnerSocketHandler(ClientListener clientListener) throws IOException {
        this.clientListener = clientListener;
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
        clients = new ArrayList<>();
        while (true) {
            try {
                Client client = new Client(socket.accept(), this);
                client.start();
                clients.add(client);
                clientListener.onClientAdded(clients.size());
                Log.d(TAG, "Launching the I/O handler");
            } catch (IOException e) {
                clientListener.onClientAdded(clients.size());
                try {
                    if (socket != null && !socket.isClosed()) {
                        socket.close();
                    }
                    Log.e(TAG, "run: ", e);
                } catch (IOException ioe) {
                    Log.e(TAG, "run: ", ioe);
                }
                e.printStackTrace();
                break;
            }
        }
    }

    public void sendAction(Action action) {
        doneCount = 0;
        try {
            for (int i = 0; i < clients.size(); i++) {
                clients.get(i).getOos().writeObject(action);
                clients.get(i).getOos().flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    @Override
    public void connectionReset(Client client) {
        clients.remove(client);
    }

    public void setListener(OnDoneListener listener) {
        this.listener = listener;
    }

    public int getClientsSize() {
        return clients.size();
    }
}