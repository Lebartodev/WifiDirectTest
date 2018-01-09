package com.example.lebarto.wifidirecttest.model;

import android.util.Log;

import com.example.lebarto.wifidirecttest.actions.WordCount;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ChatManager implements Runnable {

    private Socket socket = null;

    public ChatManager(Socket socket) {
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
                    // Read from the InputStream
                    String readMessage = iStream.readUTF();

                    write(String.valueOf(WordCount.proccess(readMessage)));
                    Log.d(TAG, readMessage);
                    // Send the obtained bytes to the UI Activity
                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
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

    public void write(String buffer) {
        try {
            oStream.writeUTF(buffer);
            oStream.flush();
        } catch (IOException e) {
            Log.e(TAG, "Exception during write", e);
        }
    }
}