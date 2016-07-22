package com.example.b2026015.bluetooth.rfb.layout;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b2026015.bluetooth.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CustomAdapter extends BaseAdapter{

    Map<String, List<String>> deviceInfo;
    Integer[] entityImages;
    Context context;

//    ArrayList<String> result;
//    ArrayList<String> macaddress;
//    ArrayList<Double> proximity;
//    ArrayList<Integer> imageId;

    private static LayoutInflater inflater=null;

    public CustomAdapter(Activity activity, Map<String, List<String>> pDeviceInfo, Integer[] pEntityImages) {
        context = activity;
        deviceInfo = pDeviceInfo;
        entityImages = pEntityImages;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // Holder for layout
    public class Holder
    {
        TextView deviceName;
        TextView macAddress;
        ImageView graphic;
        TextView proximityValue;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.program_list, null);

        //Find views from table layout
        holder.deviceName=(TextView) rowView.findViewById(R.id.beaconNameTextView);
        holder.macAddress=(TextView) rowView.findViewById(R.id.beaconShortDescription);
        holder.graphic=(ImageView) rowView.findViewById(R.id.beaconImageView);
        holder.proximityValue=(TextView) rowView.findViewById(R.id.proximityTextView);

        // For each entry in deviceInfo Map
        for (Map.Entry<String, List<String>> entry : deviceInfo.entrySet()) {

            String key = entry.getKey();
            List<String> value = entry.getValue();

            // Set device name
            holder.deviceName.setText(key);

            // Set device address and proximity
            holder.macAddress.setText(value.get(0)); // Get MAC Address
            holder.proximityValue.setText(value.get(1)); // Get Proximity Value

            // Set device graphics
            int i = 0;

            if (i < 3) { // Avoids out of bounds exceptions
                holder.graphic.setImageResource(entityImages[i]);
                i++;
                break;
            }
            else { // i is out of bounds so reset to 0.
                i = 0;
                holder.graphic.setImageResource(entityImages[i]);
            }
            i++;

        }

        // Sets click listener if device is selected
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                for (Map.Entry<String, List<String>> entry : deviceInfo.entrySet()) {
                    String key = entry.getKey();
                    int i = 0;
                    i++;

                    // If i matches position on listview selected
                    if (i == position) {
                        Toast.makeText(context, "You Clicked "+ key, Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        return rowView;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return deviceInfo.size();
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
