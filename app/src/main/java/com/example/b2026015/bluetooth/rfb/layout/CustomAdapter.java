package com.example.b2026015.bluetooth.rfb.layout;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.model.BTDevice;
import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    // Beacons & BLE devices
    ArrayList<BTDevice> bleList;
    Integer[] entityImages;
    Context context;

    private static LayoutInflater inflater = null;

    public CustomAdapter (Activity activity, ArrayList<BTDevice> pEntityList, Integer[] pEntityImages) {

        context = activity;
        bleList = pEntityList;
        entityImages = pEntityImages;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // Holder for layout
    public class Holder {
        ImageView graphic;
        TextView deviceName;
        TextView macAddress;
        TextView proximityZone;
        TextView proximityValue;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.device_list, null);

        //Find views from table layout
        holder.graphic = (ImageView) rowView.findViewById(R.id.entityImageView);
        holder.deviceName = (TextView) rowView.findViewById(R.id.entityNameTextView);
        holder.macAddress = (TextView) rowView.findViewById(R.id.entityShortDescription);
        holder.proximityZone = (TextView) rowView.findViewById(R.id.proximityZoneTextView);
        holder.proximityValue = (TextView) rowView.findViewById(R.id.entityProximityTextView);

        // For each entry in deviceInfo Map
        BTDevice bt = (BTDevice) bleList.get(position);
        holder.graphic.setImageResource(bt.getIcon());
        holder.deviceName.setText(bt.getName());
        holder.macAddress.setText(bt.getMACAddress());
        holder.proximityZone.setText(BLEDevice.proximityFromAccuracy(bt.getDistance()));
        holder.proximityValue.setText(bt.getDistanceString());

//        double distance = blee.calculateDistance();
//        if (isBetween(distance, 0.0, 3.0)) {
//            rowView.setBackgroundResource(R.color.list_intimate_proximity);
//        }
//        else if (isBetween(distance, 3.0, 10.0)) {
//            rowView.setBackgroundResource(R.color.list_close_proximity);
//        }
//        else {
//            rowView.setBackgroundResource(R.color.list_far_proximity);
//        }
//
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked " + bleList.get(position).getName(), Toast.LENGTH_LONG).show();
            }
        });
        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                // Call NeighbourFragment

                Toast.makeText(context, "Make this device my 'neighbour'", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        return rowView;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return bleList.size();
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

