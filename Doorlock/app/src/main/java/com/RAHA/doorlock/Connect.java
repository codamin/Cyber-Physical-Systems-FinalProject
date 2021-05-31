package com.RAHA.doorlock;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;

public class Connect
{
    //widgets
    Button btnPaired;
    ListView devicelist;
    //Bluetooth
    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";

    public Connect() {
        Log.d("Connect", "Connect class Started");
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if(myBluetooth == null)
        {
            System.out.println("Bluetooth Device Not Available");
        }
        else if(!myBluetooth.isEnabled())
        {
            System.out.println("Turn On Your Bluetooth!!!");
        }

        pairedDevicesList();


    }

    String pairedDevicesList()
    {
        Log.d("Connect", "PairedDeviceList() ran");
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size()>0)
        {
            for(BluetoothDevice bt : pairedDevices)
            {
                Log.d("Connect", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                Log.d("Connect", bt.getName());
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                System.out.println(bt.getName());
                if (bt.getName().equals("DESKTOP-254608Q")) {
                    return bt.getAddress();
                }
//                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
        }
        else
        {
            System.out.println("No Paired Bluetooth Devices Found.");
            return "None";
        }

//        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
//        devicelist.setAdapter(adapter);
//        devicelist.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked

        return null;
    }

//    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
//    {
//        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
//        {
//            // Get the device MAC address, the last 17 chars in the View
//            String info = ((TextView) v).getText().toString();
//            String address = info.substring(info.length() - 17);
//
//            // Make an intent to start next activity.
//            Intent i = new Intent(DeviceList.this, MainActivity.class);
//
//            //Change the activity.
//            i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
//            startActivity(i);
//        }
//    };

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_device_list, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}