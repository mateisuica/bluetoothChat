package ro.gmsoftware.bluetoothchat;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static ro.gmsoftware.bluetoothchat.ConnectedThread.MESSAGE_READ;

public class SendMessageActivity extends AppCompatActivity {

    private ConnectThread connectThread;
    private TextView messaged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        messaged = (TextView) findViewById(R.id.clientMessages);
        final BluetoothDevice device = getIntent().getParcelableExtra("device");
        if(device != null) {
            if(connectThread == null) {
                connectThread = new ConnectThread(mHandler, device);
                connectThread.start();
            }
        }

        final EditText text = (EditText) findViewById(R.id.message);
        Button send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mesaj = device.getName() + " " + Long.toString(System.currentTimeMillis()) + " " + text.getText().toString();
                if( connectThread.sendMessage(mesaj) ) {
                    messaged.append("To: " + mesaj + "\n");
                }
            }
        });
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_READ:
                    byte[] readBuf = (byte[])msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    messaged.append(readMessage + "\n");
            }
        }
    };
}
