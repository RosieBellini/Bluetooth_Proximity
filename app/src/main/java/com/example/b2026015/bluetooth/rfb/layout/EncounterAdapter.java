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
import android.widget.Toast;

import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.entities.Response;
import com.example.b2026015.bluetooth.rfb.model.BTDevice;
import com.example.b2026015.bluetooth.rfb.sensors.BLEDevice;
import com.example.b2026015.bluetooth.rfb.storage.SQLHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class EncounterAdapter extends BaseAdapter {

    // Classic devices
    private static Integer[] questionImage = {R.drawable.qm};
    private LayoutInflater inflater;
    private ArrayList<Response> responseList;
    private Context context;
    private OnPairButtonClickListener mListener;
    private SQLHelper sql;

    public EncounterAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public EncounterAdapter(Activity activity) {

        context = activity;
        sql = new SQLHelper(context);
        responseList = sql.getAllResponses();
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ResponseHolder holder = new ResponseHolder();
        View rowView;
        rowView = inflater.inflate(R.layout.device_list, null);

        //Find views from table layout
        holder.graphic = (ImageView) rowView.findViewById(R.id.entityImageView);
        holder.personName = (TextView) rowView.findViewById(R.id.entityNameTextView);
        holder.date = (TextView) rowView.findViewById(R.id.entityShortDescription);
        holder.length = (TextView) rowView.findViewById(R.id.entityProximityTextView);

        // For each entry in deviceInfo Map
        Response resp = (Response) responseList.get(position);
        holder.graphic.setImageResource(questionImage[0]);
        holder.personName.setText(resp.gettName());
        holder.date.setText(resp.getDate());
        holder.time.setText(resp.getDate());
        holder.length.setText(humanReadableMilli(resp.getLength()));

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked " + responseList.get(position).gettName(), Toast.LENGTH_LONG).show();
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

    public String humanReadableMilli(long milli) {

        // 1 Minutes = 60000 Milliseconds
        // 1 Second = 1000 Milliseconds

        double minutes = Math.floor((milli % 3600000) / 60000);
        double seconds = Math.floor(((milli % 360000) % 60000) / 1000);
        return "" + minutes + "m" + " " + seconds + "s";
    }

    public String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd MMM yyyy", Locale.ENGLISH);
        return dateFormat.format(date);
    }

    public String formatTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH mm ss", Locale.ENGLISH);
        return dateFormat.format(date);
    }

    public void setData(ArrayList<Response> data) {
        responseList = data;
    }

    public void setListener(OnPairButtonClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getCount() {
        return responseList.size();
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

    static class ResponseHolder {

        ImageView graphic;
        TextView personName;
        TextView date;
        TextView time;
        TextView length;

    }


}