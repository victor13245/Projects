package com.example.mini_app.bt_utilities;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.mini_app.BTActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedThread extends Thread {

    private static final String TAG = "ConnectedLogs";
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private String valueRead;
    private boolean keepReading = true; // Flag to control reading loop

    public ConnectedThread(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // stabileste canalele de input/output
        try {
            tmpIn = socket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating input stream", e);
        }
        try {
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating output stream", e);
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public String getValueRead(){
        return valueRead;
    }

    public void run() {
        byte[] buffer = new byte[1024];
        int bytes = 0; // bytes returned from read()


        while (keepReading) {
            try {
                int readByte = mmInStream.read();
                if (readByte != -1) {
                    buffer[bytes] = (byte) readByte;
                    // daca bitul primit e /n atunci e capat de mesaj
                    if (buffer[bytes] == '\n') {
                        String readMessage = new String(buffer, 0, bytes);
                        Log.e(TAG, readMessage);
                        valueRead = readMessage;
                        bytes = 0;
                        BTActivity.handler.obtainMessage(BTActivity.MESSAGE_READ, readMessage).sendToTarget();
                    } else {
                        bytes++;
                    }
                }
            } catch (IOException e) {
                Log.d(TAG, "Input stream was disconnected", e);
                break;
            }
        }
    }


    public void cancel() {
        keepReading = false;
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }
}

