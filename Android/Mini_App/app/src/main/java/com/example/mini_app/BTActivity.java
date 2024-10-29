package com.example.mini_app;

import static com.example.mini_app.image_utilities.BackgroundRemover.removeWhiteBackground;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.mini_app.bt_utilities.ConnectThread;
import com.example.mini_app.bt_utilities.ConnectedThread;
import com.example.mini_app.bt_utilities.PriorStates;
import com.example.mini_app.image_utilities.BackgroundRemover;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BTActivity extends AppCompatActivity {

    private static final String TAG = "BluetoothLogs";
    private static final int REQUEST_ENABLE_BT = 1;
    public static final int MESSAGE_READ = 1; // Verific daca am citit mesaj
    //Handler pt a verifica statusul conexiunii bt
    public static Handler handler;
    private final static int ERROR_READ = 0;
    BluetoothDevice arduinoBTModule = null;
    UUID arduinoUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //default UUID
    private PriorStates priorState = PriorStates.NONE;

    MediaPlayer playerFar = null;
    MediaPlayer playerMedium = null;
    MediaPlayer playerNear = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btactivity);

        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

        TextView btReadingsDist = findViewById(R.id.btReadingsDist);
        TextView btReadingsCoords = findViewById(R.id.btReadingsCoords);
        TextView btReadingsSpeed = findViewById(R.id.btReadingsSpeed);
        //TextView btDevices = findViewById(R.id.btDevices);
        Button connectToDevice = (Button) findViewById(R.id.connectToDevice);
        Button searchDevices = (Button) findViewById(R.id.seachDevices);

        ImageView imageViewNone = findViewById(R.id.imageNone);
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.proximity_inactive2);
        Bitmap transparentBitmap = BackgroundRemover.removeWhiteBackground(originalBitmap);
        imageViewNone.setImageBitmap(transparentBitmap);

        ImageView imageViewFar = findViewById(R.id.imageFar);
        originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.proximity_far2);
        transparentBitmap = BackgroundRemover.removeWhiteBackground(originalBitmap);
        imageViewFar.setImageBitmap(transparentBitmap);
        imageViewFar.setVisibility(View.INVISIBLE);

        ImageView imageViewMedium = findViewById(R.id.imageMedium);
        originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.proximity_medium2);
        transparentBitmap = BackgroundRemover.removeWhiteBackground(originalBitmap);
        imageViewMedium.setImageBitmap(transparentBitmap);
        imageViewMedium.setVisibility(View.INVISIBLE);

        ImageView imageViewNear = findViewById(R.id.imageNear);
        originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.proximity_close2);
        transparentBitmap = BackgroundRemover.removeWhiteBackground(originalBitmap);
        imageViewNear.setImageBitmap(transparentBitmap);
        imageViewNear.setVisibility(View.INVISIBLE);

        /////////
        if(MapsActivity.isCloseToParkingSpot()){

            Random random = new Random();
            int randomiser = random.nextInt(2047);

            if(randomiser%2==0)
            {
                ImageView car0 = findViewById(R.id.imageViewCar0);
                car0.setVisibility(View.INVISIBLE);
            }
            if((randomiser/2)%2==0)
            {
                ImageView car1 = findViewById(R.id.imageViewCar1);
                car1.setVisibility(View.INVISIBLE);
            }
            if((randomiser/4)%2==0) {
                ImageView car2 = findViewById(R.id.imageViewCar2);
                car2.setVisibility(View.INVISIBLE);
            }
            if((randomiser/8)%2==0) {
                ImageView car3 = findViewById(R.id.imageViewCar3);
                car3.setVisibility(View.INVISIBLE);
            }
            if((randomiser/16)%2==0) {
                ImageView car4 = findViewById(R.id.imageViewCar4);
                car4.setVisibility(View.INVISIBLE);
            }
            if((randomiser/32)%2==0) {
                ImageView car5 = findViewById(R.id.imageViewCar5);
                car5.setVisibility(View.INVISIBLE);
            }
            if((randomiser/64)%2==0) {
                ImageView car6 = findViewById(R.id.imageViewCar6);
                car6.setVisibility(View.INVISIBLE);
            }
            if((randomiser/128)%2==0) {
                ImageView car7 = findViewById(R.id.imageViewCar7);
                car7.setVisibility(View.INVISIBLE);
            }if((randomiser/256)%2==0) {
                ImageView car8 = findViewById(R.id.imageViewCar8);
                car8.setVisibility(View.INVISIBLE);
            }if((randomiser/512)%2==0) {
                ImageView car9 = findViewById(R.id.imageViewCar9);
                car9.setVisibility(View.INVISIBLE);
            }


        }
        else {
            ConstraintLayout parkingPlace = findViewById(R.id.parking_layout);
            parkingPlace.setVisibility(View.INVISIBLE);
        }

        /////////

        playerFar = MediaPlayer.create(BTActivity.this,R.raw.parking_far);
        playerMedium = MediaPlayer.create(BTActivity.this,R.raw.parking_medium);
        playerNear = MediaPlayer.create(BTActivity.this,R.raw.parking_near);

        Log.d(TAG, "Begin Execution");

        //Handler pt diferitele cazuri intampinate
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case ERROR_READ:
                        String arduinoMsg = msg.obj.toString(); // Read message from Arduino
                        btReadingsDist.setText(arduinoMsg);
                        btReadingsCoords.setText("");
                        btReadingsSpeed.setText("");
                        break;
                    case MESSAGE_READ:
                        String message = (String) msg.obj;
                        String[] strings = message.split("/");
                        if(strings.length>=3) {
                            btReadingsCoords.setText("");
                            btReadingsCoords.append(strings[0] + "\n");
                            btReadingsSpeed.setText("");
                            btReadingsSpeed.append(strings[1] + "\n");
                            btReadingsDist.setText("");
                            btReadingsDist.append(strings[2] + "\n");

                            Float distance = Float.valueOf(strings[2]);
                            if(distance > 40){
                                imageViewNone.setVisibility(View.VISIBLE);
                                imageViewFar.setVisibility(View.INVISIBLE);
                                imageViewMedium.setVisibility(View.INVISIBLE);
                                imageViewNear.setVisibility(View.INVISIBLE);
                                if(playerFar!= null && playerFar.isPlaying())
                                {
                                    playerFar.pause();
                                }
                                else {
                                    if (playerMedium!= null &&  playerMedium.isPlaying()) {
                                        playerMedium.pause();
                                    }
                                    else{
                                        if(playerNear!= null && playerNear.isPlaying()){
                                            playerNear.pause();
                                        }
                                    }
                                }

                                priorState=PriorStates.NONE;
                            }
                            else {
                                if(distance > 25){
                                    imageViewNone.setVisibility(View.INVISIBLE);
                                    imageViewFar.setVisibility(View.VISIBLE);
                                    imageViewMedium.setVisibility(View.INVISIBLE);
                                    imageViewNear.setVisibility(View.INVISIBLE);
                                    if(playerFar!= null && !playerFar.isPlaying())
                                    {
                                        playerFar.start();
                                        playerFar.setLooping(true);
                                    }

                                    if (playerMedium!= null && playerMedium.isPlaying()) {
                                        playerMedium.pause();
                                    }
                                    else{
                                         if(playerNear!= null && playerNear.isPlaying()){
                                            playerNear.pause();
                                        }
                                    }


                                    priorState=PriorStates.FAR;
                                }
                                else{
                                    if(distance > 10){
                                        imageViewNone.setVisibility(View.INVISIBLE);
                                        imageViewFar.setVisibility(View.INVISIBLE);
                                        imageViewMedium.setVisibility(View.VISIBLE);
                                        imageViewNear.setVisibility(View.INVISIBLE);
                                        if(playerMedium!= null && !playerMedium.isPlaying())
                                        {
                                            playerMedium.start();
                                            playerMedium.setLooping(true);
                                        }

                                        if (playerFar!= null && playerFar.isPlaying()) {
                                            playerFar.pause();
                                        }
                                        else{
                                            if(playerNear!= null && playerNear.isPlaying()){
                                                playerNear.pause();
                                            }
                                        }
                                        priorState=PriorStates.MEDIUM;
                                    }
                                    else {
                                        imageViewNone.setVisibility(View.INVISIBLE);
                                        imageViewFar.setVisibility(View.INVISIBLE);
                                        imageViewMedium.setVisibility(View.INVISIBLE);
                                        imageViewNear.setVisibility(View.VISIBLE);
                                        /*if(player==null)
                                        {
                                            player = MediaPlayer.create(BTActivity.this,R.raw.parking_near);
                                            player.start();
                                            player.setLooping(true);
                                        }
                                        else {
                                            if(priorState != PriorStates.NEAR){
                                                player.release();
                                                player = MediaPlayer.create(BTActivity.this,R.raw.parking_near);
                                                player.start();
                                                player.setLooping(true);
                                            }
                                        }*/
                                        if(playerNear!= null && !playerNear.isPlaying())
                                        {
                                            playerNear.start();
                                            playerNear.setLooping(true);
                                        }

                                        if (playerFar!= null && playerFar.isPlaying()) {
                                            playerFar.pause();
                                        }
                                        else{
                                            if(playerMedium!= null && playerMedium.isPlaying()){
                                                playerMedium.pause();
                                            }
                                        }
                                        priorState=PriorStates.NEAR;
                                    }

                                }
                            }

                        }
                        else {
                            System.out.println("not enough data provided");
                        }
                        break;
                }
            }
        };


        final Observable<String> connectToBTObservable = Observable.create(emitter -> {
            Log.d(TAG, "Calling connectThread class");

            ConnectThread connectThread = new ConnectThread(arduinoBTModule, arduinoUUID, handler);
            connectThread.run();
            //Check if Socket connected
            if (connectThread.getMmSocket().isConnected()) {
                Log.d(TAG, "Calling ConnectedThread class");
                //The pass the Open socket as arguments to call the constructor of ConnectedThread
                ConnectedThread connectedThread = new ConnectedThread(connectThread.getMmSocket());
                connectedThread.run();
                // Close the socket connection on completion
                connectedThread.cancel();
            }
            connectThread.cancel();
            emitter.onComplete();
        });

        connectToDevice.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View view) {
                btReadingsCoords.setText("");
                btReadingsSpeed.setText("");
                btReadingsDist.setText("");
                if (arduinoBTModule != null) {
                    connectToBTObservable.
                            observeOn(AndroidSchedulers.mainThread()).
                            subscribeOn(Schedulers.io()).
                            subscribe(valueRead -> {
                                //valueRead returned by the onNext() from the Observable
                                String[] strings = valueRead.split("/");
                                if(strings.length>=3) {
                                    btReadingsCoords.setText("");
                                    btReadingsCoords.append(strings[0] + "\n");
                                    btReadingsSpeed.setText("");
                                    btReadingsSpeed.append(strings[1] + "\n");
                                    btReadingsDist.setText("");
                                    btReadingsDist.append(strings[2] + "\n");
                                }
                                else {
                                    System.out.println("not enough data provided");
                                }

                            });
                }
            }
        });

        searchDevices.setOnClickListener(new View.OnClickListener() {
            //Display all the linked BT Devices
            @Override
            public void onClick(View view) {
                //Check if the phone supports BT
                if (bluetoothAdapter == null) {
                    // Device doesn't support Bluetooth
                    Log.d(TAG, "Device doesn't support Bluetooth");
                } else {
                    Log.d(TAG, "Device support Bluetooth");
                    //Check BT enabled. If disabled, we ask the user to enable BT
                    if (!bluetoothAdapter.isEnabled()) {
                        Log.d(TAG, "Bluetooth is disabled");
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            Log.d(TAG, "We don't have BT Permissions");
                            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                            Log.d(TAG, "Bluetooth is enabled now");
                        } else {
                            Log.d(TAG, "We have BT Permissions");
                            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                            Log.d(TAG, "Bluetooth is enabled now");
                        }
                    } else {
                        Log.d(TAG, "Bluetooth is enabled");
                    }
                    //String btDevicesString = "";
                    Set < BluetoothDevice > pairedDevices = bluetoothAdapter.getBondedDevices();

                    if (pairedDevices.size() > 0) {

                        // There are paired devices. Get the name and address of each paired device.
                        for (BluetoothDevice device: pairedDevices) {
                            String deviceName = device.getName();
                            String deviceAddress = device.getAddress(); // MAC address
                            //btDevicesString += deviceName + " " + deviceAddress + "\n";
                            // In case you want to connect to a specific device
                            if (deviceName.equals("ESP32test")) {
                                connectToDevice.setEnabled(true);
                                arduinoBTModule = bluetoothAdapter.getRemoteDevice(deviceAddress);
                                Log.d(TAG, "Arduino Module address: " + deviceAddress);
                            }
                        }
                        //btDevices.setText(btDevicesString);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(playerFar!=null)
        {
            playerFar.release();
            playerFar = null;
        }
        if(playerMedium!=null)
        {
            playerMedium.release();
            playerMedium = null;
        }
        if(playerNear!=null)
        {
            playerNear.release();
            playerNear = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(playerFar!=null)
        {
            playerFar.release();
            playerFar = null;
        }
        if(playerMedium!=null)
        {
            playerMedium.release();
            playerMedium = null;
        }
        if(playerNear!=null)
        {
            playerNear.release();
            playerNear = null;
        }
    }
}