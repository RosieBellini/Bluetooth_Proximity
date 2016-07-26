package com.example.b2026015.bluetooth.rfb.layout;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.entities.BLEEntity;
import com.example.b2026015.bluetooth.rfb.entities.Beacon;
import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CustomAdapter extends BaseAdapter{

    ArrayList<? extends BLEEntity> entityList;
    Integer[] entityImages;
    String identifier;
    Context context;

    private static LayoutInflater inflater=null;

    public CustomAdapter(Activity activity, ArrayList<? extends BLEEntity> pEntityList, Integer[] pEntityImages) {

        context = activity;
        entityList = pEntityList;
        entityImages = pEntityImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    // Holder for layout
    public class Holder
    {
        ImageView graphic;
        TextView deviceName;
        TextView macAddress;
        TextView proximityValue;
    }

    public static boolean isBetween(double x, double lower, double upper) {
        return lower <= x && x <= upper;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.program_list, null);

        //Find views from table layout
        holder.graphic=(ImageView) rowView.findViewById(R.id.entityImageView);
        holder.deviceName=(TextView) rowView.findViewById(R.id.entityNameTextView);
        holder.macAddress=(TextView) rowView.findViewById(R.id.entityShortDescription);
        holder.proximityValue=(TextView) rowView.findViewById(R.id.entityProximityTextView);

        // For each entry in deviceInfo Map
        BLEEntity blee = (BLEEntity) entityList.get(position);
        holder.graphic.setImageResource(blee.getIcon());
        holder.deviceName.setText(blee.getName());
        holder.macAddress.setText(blee.getMACAddress());
        holder.proximityValue.setText("" + blee.getDistance());

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
//        rowView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//
//                Toast.makeText(context, "You Clicked "+ ((BLEEntity) entityList.get(position)).getName(), Toast.LENGTH_LONG).show();
//
//
//            }
//        });
        return rowView;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return entityList.size();
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
