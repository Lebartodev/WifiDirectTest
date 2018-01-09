package com.example.lebarto.wifidirecttest.model;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by lebarto on 01.01.2018.
 */

public class Client extends Thread {
    private Socket socket;
    private ServerBase serverBase;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public Client(Socket socket, ServerBase serverBase) {
        this.serverBase = serverBase;
        this.socket = socket;
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {
                try {
                    String readMessage = ois.readUTF();
                    serverBase.onClientDone(Integer.parseInt(readMessage));
                } catch (IOException e) {
                    Log.e("Client", "disconnected", e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObjectOutputStream getOos() {
        return oos;
    }
}
