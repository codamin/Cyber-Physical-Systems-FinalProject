package com.RAHA.doorlock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog progress;
    String address = null;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket myBtSocket = null;
    private Boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//        Intent newint = getIntent();
//        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device
        Log.d("Main", "To Connect");
        Connect myConnect = new Connect();
        address = myConnect.pairedDevicesList();
        Log.d("Main", ">>>>>>>"+address);

        Button btnOn = (Button) findViewById(R.id.button_on);
        Button btnOff = (Button) findViewById(R.id.button_off);

        new ConnectBT().execute();

        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Main", ">>>>>>>>>Handler Ran");
                if (v == btnOn) {
//                    Intent intentGyroscope = new Intent(MainActivity.this,
//                            GyroscopeActivity.class);
//                    startActivity(intentGyroscope);
                    Log.d("Main", ">>>>>>>>>>>>>ONON");
                    msg("ON");
                    turnOnLed();
                }
                if (v == btnOff) {
//                    Intent intentGravity = new Intent(MainActivity.this,
//                            GravityActivity.class);
//                    startActivity(intentGravity);
                    Log.d("Main", ">>>>>>>>>>>>>>>>OFFOFF");
                    msg("OFF");
                    turnOffLed();
                }
            }
        };

        btnOn.setOnClickListener(handler);
        btnOff.setOnClickListener(handler);
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(MainActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (myBtSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    myBtSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    myBtSocket.connect();//start connection
                    Log.d("Main", ">>>>>>>>>>Device Connected");
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
                Log.d("Main", ">>>>>>>>>>>>>Device couldn't Connect");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
    }

    private void turnOffLed()
    {
        if (myBtSocket != null)
        {
            try
            {
                Log.d("Main", "Trying sending On");
                myBtSocket.getOutputStream().write("On".toString().getBytes());
            }
            catch (IOException e)
            {
                Log.d("Main", "Trying Sending Off");
                msg("Error");
            }
        }
    }

    private void turnOnLed()
    {
        if (myBtSocket!=null)
        {
            try
            {
                myBtSocket.getOutputStream().write("Of".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
}