package com.example.mini_app.bt_utilities;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;


// Creez conexiunea cu placuta
public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private static final String TAG = "ConnectLogs";
    public static Handler handler;
    private final static int ERROR_READ = 0;

    @SuppressLint("MissingPermission")
    public ConnectThread(BluetoothDevice device, UUID MY_UUID, Handler handler) {

        BluetoothSocket tmp = null;
        this.handler=handler;

        try {

            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }
        mmSocket = tmp;
    }

    @SuppressLint("MissingPermission")
    public void run() {

        try {
            mmSocket.connect();
        } catch (IOException connectException) {
            handler.obtainMessage(ERROR_READ, "Unable to connect to the BT device").sendToTarget();
            Log.e(TAG, "connectException: " + connectException);
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "Could not close the client socket", closeException);
            }
            return;
        }
        //manageMyConnectedSocket(mmSocket);
    }

    //inchide conexiunea cu placuta
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }

    public BluetoothSocket getMmSocket(){
        return mmSocket;
    }
}