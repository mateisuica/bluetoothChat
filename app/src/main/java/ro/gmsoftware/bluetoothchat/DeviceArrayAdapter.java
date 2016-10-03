package ro.gmsoftware.bluetoothchat;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mateisuica on 03/10/2016.
 */

public class DeviceArrayAdapter extends BaseAdapter {

    private final List<BluetoothDevice> mList;
    private final Context mContext;
    private final int mLayout;

    public DeviceArrayAdapter(Context context, int resource, @NonNull List<BluetoothDevice> objects) {
        mList = objects;
        mContext = context;
        mLayout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(mLayout, parent, false);

        TextView name = (TextView) view.findViewById(R.id.deviceName);
        TextView address = (TextView) view.findViewById(R.id.deviceAddress);

        name.setText(mList.get(position).getName());
        address.setText(mList.get(position).getAddress());

        return view;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public BluetoothDevice getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
