package com.example.viji.miniproject;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    BluetoothAdapter mBluetoothAdapter;

    BluetoothConnectionService mBluetoothConnection;


    long normalCounter=0,emergencyCounter=0;

    TextView emergency,normal;

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    boolean foundPaired = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothConnection = new BluetoothConnectionService(this);

        emergency = (TextView)findViewById(R.id.counterTextEmergency);
        normal = (TextView)findViewById(R.id.counterTextNormal);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                if(deviceName.equals("HC-05")){
                    foundPaired=true;
                    startBTConnection(device,MY_UUID_INSECURE);
                }
            }
        }
        if(!foundPaired)
            Toast.makeText(this,"Turn on bluetooth and pair with HC-05",Toast.LENGTH_LONG).show();
    }


    public void startBTConnection(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");

        mBluetoothConnection.startClient(device,uuid);
    }

    public void increment(boolean isEmergency){
        if(isEmergency){
            emergencyCounter++;
            emergency.setText(String.valueOf(emergencyCounter));
        }else{
            normalCounter++;
            normal.setText(String.valueOf(normalCounter));
        }
    }

    public void shutDown(View view) {
        if (foundPaired)
            mBluetoothConnection.write("s".getBytes());
        else
            Toast.makeText(this,"No bluetooth found first pair then shutdown then remove power",Toast.LENGTH_LONG).show();
    }
}
