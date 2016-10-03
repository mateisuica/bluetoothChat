package ro.gmsoftware.bluetoothchat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static ro.gmsoftware.bluetoothchat.ConnectedThread.MESSAGE_READ;

public class MainActivity extends AppCompatActivity {

    private int REQUEST_ENABLE_BT = 93221;
    private DeviceArrayAdapter mArrayAdapter;
    private List<BluetoothDevice> deviceList = new ArrayList<>();
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView list = (ListView) findViewById(R.id.device_list);

        mArrayAdapter = new DeviceArrayAdapter(this, R.layout.device_item, deviceList);
        list.setAdapter(mArrayAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private ConnectThread connectThread;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(connectThread == null) {
                    connectThread = new ConnectThread(deviceList.get(position));
                    connectThread.start();
                }
                connectThread.sendMessage("Un mesaj ratacit");


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
// If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                deviceList.add(device);
            }
        }
        mArrayAdapter.notifyDataSetChanged();

    }

    public void serverClick(View view) {
        Intent intent = new Intent(this, ServerActivity.class);
        startActivity(intent);
    }




}
