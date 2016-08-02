package com.example.b2026015.bluetooth.rfb.layout;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.b2026015.bluetooth.R;

import java.util.ArrayList;
import java.util.Random;

public class ClassicAdapter extends BaseAdapter {

    // Classic devices
    private static Integer[] classicImages = {R.drawable.deviceb, R.drawable.deviceg, R.drawable.devicep};
    ArrayList<BluetoothDevice> classicList;
    Integer[] deviceImages;
    Context context;

    private static LayoutInflater inflater = null;

    public ClassicAdapter(Activity activity, ArrayList<BluetoothDevice> pEntityList, Integer[] pEntityImages) {

        context = activity;
        classicList = pEntityList;
        deviceImages = pEntityImages;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // Holder for layout
    public class ClassicHolder {
        ImageView graphic;
        TextView deviceName;
        TextView macAddress;
        Button pairButton;
        TextView proximityZone;
        TextView proximityValue;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ClassicHolder holder = new ClassicHolder();
        View rowView;
        Random random = new Random();
        rowView = inflater.inflate(R.layout.list_item_device, null);

        //Find views from table layout
        holder.graphic = (ImageView) rowView.findViewById(R.id.entityImageView);
        holder.deviceName = (TextView) rowView.findViewById(R.id.entityNameTextView);
        holder.macAddress = (TextView) rowView.findViewById(R.id.entityShortDescription);
        holder.proximityZone = (TextView) rowView.findViewById(R.id.proximityZoneTextView);
        holder.proximityValue = (TextView) rowView.findViewById(R.id.entityProximityTextView);

        // For each entry in deviceInfo Map
        BluetoothDevice bt = (BluetoothDevice) classicList.get(position);
        holder.graphic.setImageResource(classicImages[random.nextInt(3)]);
        holder.deviceName.setText(bt.getName());
        holder.macAddress.setText(bt.getAddress());
        holder.proximityZone.setText("???");
        holder.proximityValue.setText("???");

        return rowView;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return classicList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}