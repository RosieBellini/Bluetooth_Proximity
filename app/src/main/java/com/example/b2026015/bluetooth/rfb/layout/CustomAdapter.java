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

public class CustomAdapter extends BaseAdapter{
    ArrayList<String> result;
    ArrayList<String> macaddress;
    ArrayList<Double> proximity;
    ArrayList<Integer> imageId;
    Context context;
    private static LayoutInflater inflater=null;

    public CustomAdapter(Activity beaconActivity, ArrayList<String> beaconNameList, ArrayList<String> macAddress, ArrayList<Double> beaconProximity, ArrayList<Integer> beaconImages) {
        // TODO Auto-generated constructor stub
        result=beaconNameList;
        macaddress=macAddress;
        context=beaconActivity;
        imageId=beaconImages;
        proximity=beaconProximity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    public class Holder
    {
        TextView tv;
        TextView mac;
        ImageView img;
        TextView prox;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.program_list, null);
        holder.tv=(TextView) rowView.findViewById(R.id.beaconNameTextView);
        holder.mac=(TextView) rowView.findViewById(R.id.beaconShortDescription);
        holder.img=(ImageView) rowView.findViewById(R.id.beaconImageView);
        holder.prox=(TextView) rowView.findViewById(R.id.proximityTextView);
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

}
