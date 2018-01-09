package com.example.lebarto.wifidirecttest.model;

import android.util.Log;

import com.example.lebarto.wifidirecttest.model.Client;
import com.example.lebarto.wifidirecttest.model.ServerBase;
import com.example.lebarto.wifidirecttest.view.MainPage;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class GroupOwnerSocketHandler extends Thread implements ServerBase {
    private ServerSocket socket = null;
    private static final String TAG = "GroupOwnerSocketHandler";
    private List<Client> clients = new ArrayList<>();
    private MainPage mainPage;

    public GroupOwnerSocketHandler(MainPage mainPage) throws IOException {
        this.mainPage = mainPage;
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

    public void setText(String text) {
        for (Client client : clients) {
            try {
                client.getOos().writeUTF(text);
                client.getOos().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClientDone(int count) {
        mainPage.onTextCalculated(count);
    }
}