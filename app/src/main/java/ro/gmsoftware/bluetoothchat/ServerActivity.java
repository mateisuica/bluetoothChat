package ro.gmsoftware.bluetoothchat;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.UUID;

import static ro.gmsoftware.bluetoothchat.ConnectedThread.MESSAGE_READ;

/**
 * Created by mateisuica on 03/10/2016.
 */

public class ServerActivity extends Activity {

    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 8332;
    private AcceptThread mAcceptThread;
    private TextView view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_activity);

        view = (TextView)findViewById(R.id.textView);
    }

    @Override
    protected void onResume() {
        super.onResume();



        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }



    }


    private static final UUID MY_UUID = UUID.fromString("000011008-0000-1000-8000-00805F9B34FB");

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("BluetoothServer", MY_UUID);
            }
            catch (IOException e) { }
            mmServerSocket = tmp;
        }

        @Override
        public void run() {
            BluetoothSocket socket = null;
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                }
                catch (IOException e) {
                    break;
                }

                if (socket != null) {
                    manageConnectedSocket(socket);
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                    }
                    break;
                }
            }
        }

        public void cancel() {
            try {
                mmServerSocket.close();
            }
            catch (IOException e) { }
        }
    }


    public void manageConnectedSocket(BluetoothSocket socket) {
        ConnectedThread server = new ConnectedThread(mHandler, socket);
        server.start();
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_READ:
                    byte[] readBuf = (byte[])msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    view.append(readMessage);
            }
        }
    };

}
