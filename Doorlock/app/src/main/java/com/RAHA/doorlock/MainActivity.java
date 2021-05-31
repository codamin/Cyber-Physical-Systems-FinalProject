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
import java.math.BigInteger;
import java.util.UUID;

import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog progress;
    String address = null;
    String myAddress = null;
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
                    Log.d("Main", ">>>>>>>>>>>>>ONON");
                    msg("ON");
                    openLock();
                }
                if (v == btnOff) {
                    Log.d("Main", ">>>>>>>>>>>>>>>>OFFOFF");
                    msg("OFF");
                    closeLock();
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
                    Log.d("Main", ">>>>>>>>>>>>>>one");
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    Log.d("Main", ">>>>>>>>>>>>>>two");
                    myAddress = myBluetooth.getAddress();
//                    String toSend = myAddress+"@"+Security.run("lock#"+myAddress);
//                    Log.d("Main", ">>>>>>>>>"+toSend);
                    Log.d("Main", ">>>>>>>>>>>>>>three");
                    Log.d("Main", ">>>>>>>>>>>my"+myAddress);
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    Log.d("Main", ">>>>>>>>>>>>>>four");
                    myBtSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
//                    myBtSocket = dispositivo.createRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection

                    Log.d("Main", ">>>>>>>>>>>>>>five");
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    Log.d("Main", ">>>>>>>>>>>>>>six");
                    myBtSocket.connect();//start connection
                    Log.d("Main", ">>>>>>>>>>>>>>seven");
                    Log.d("Main", ">>>>>>>>>>Device Connected");
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
                Log.e("Main", "I got an error", e);
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

    private void closeLock()
    {
        if (myBtSocket != null)
        {
            try
            {
                String toSend = myAddress+"#"+Security.run("lock#"+myAddress);
                Log.d("Main", ">>>>>>>>>>>>>>>>>Lock__"+toSend);
//                Log.d("Main", ">>>>>>>>>>>>>>>>>Lock_"+String.format("%x", new BigInteger(1, toSend.getBytes("UTF-8"))));
                byte[] message = Security.mergeByteString(myAddress+"#", Security.run("lock#"+myAddress));
//                myBtSocket.getOutputStream().write(toSend.getBytes());
                myBtSocket.getOutputStream().write(message);
                Log.d("Main", "Trying sending On");
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void openLock()
    {
        if (myBtSocket!=null)
        {
            try
            {
                String toSend = myAddress+"#"+Security.run("open#"+myAddress);
                Log.d("Main", ">>>>>>>>>>>>>>>>>Open__"+toSend);
//                Log.d("Main", ">>>>>>>>>>>>>>>>>Open_"+String.format("%x", new BigInteger(1, toSend.getBytes("UTF-8"))));
//                myBtSocket.getOutputStream().write(toSend.getBytes());
                byte[] message = Security.mergeByteString(myAddress+"#", Security.run("open#"+myAddress));
//                byte[] tmpf = Security.run("open#"+myAddress);
//                for (byte b: tmpf){
//                    Log.i("myactivity", String.format("0x%20x", b));
//                }
                myBtSocket.getOutputStream().write(message);
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
}