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

        //Generate iterators to iterate over keys and values
        Iterator it = deviceInfo.keySet().iterator();

        for (Map.Entry<String, List<String>> key : deviceInfo.keySet()) {
            String key = key.getKey();
            List<String> value = entry.getValue();

            // Set device name
            holder.deviceName.setText(key);

            // Set device address and proximity
            holder.macAddress.setText(value.get(0));
            holder.proximityValue.setText(value.get(1);

            for (int i = 0; i < 2; i++) {
                entityImages[i]
            }
            // ...
        }

        holder.deviceName.setText

        holder.tv.setText(result.get(position));
        holder.mac.setText(macaddress.get(position));
        holder.prox.setText("" + proximity.get(position));
        holder.img.setImageResource(imageId.get(position));
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked "+result.get(position), Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
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
