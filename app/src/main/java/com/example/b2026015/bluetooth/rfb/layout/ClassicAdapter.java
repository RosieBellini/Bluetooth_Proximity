package com.example.b2026015.bluetooth.rfb.layout;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.b2026015.bluetooth.R;

import java.util.ArrayList;
import java.util.Random;

public class ClassicAdapter extends BaseAdapter {

    // Classic devices
    private static Integer[] classicImages = {R.drawable.deviceb, R.drawable.deviceg, R.drawable.devicep};
    private LayoutInflater inflater;
    private ArrayList<BluetoothDevice> classicList;
    private Integer[] deviceImages;
    private Context context;
    private OnPairButtonClickListener mListener;

    public ClassicAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public ClassicAdapter(Activity activity, ArrayList<BluetoothDevice> pEntityList, Integer[] pEntityImages) {

        context = activity;
        classicList = pEntityList;
        deviceImages = pEntityImages;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ClassicHolder holder = new ClassicHolder();
        View rowView;
        Random random = new Random();
        rowView = inflater.inflate(R.layout.list_item_device, null);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_device, null);
            holder.nameTv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.addressTv = (TextView) convertView.findViewById(R.id.tv_address);
            holder.pairBtn = (Button) convertView.findViewById(R.id.btn_pair);

            convertView.setTag(holder);
        } else {
            holder = (ClassicHolder) convertView.getTag();
        }

        BluetoothDevice device = classicList.get(position);

        holder.nameTv.setText(device.getName());
        holder.addressTv.setText(device.getAddress());
        holder.pairBtn.setText((device.getBondState() == BluetoothDevice.BOND_BONDED) ? "Unpair" : "Pair");
        holder.pairBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onPairButtonClick(position);
                }
            }
        });

        return convertView;
    }

    public void setData(ArrayList<BluetoothDevice> data) {
        classicList = data;
    }

    public void setListener(OnPairButtonClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getCount() {
        return classicList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface OnPairButtonClickListener {
        public abstract void onPairButtonClick(int position);
    }

    static class ClassicHolder {
        TextView nameTv;
        TextView addressTv;
        TextView pairBtn;
    }


}