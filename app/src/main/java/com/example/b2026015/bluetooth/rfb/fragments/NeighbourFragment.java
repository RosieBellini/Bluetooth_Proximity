package com.example.b2026015.bluetooth.rfb.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.b2026015.bluetooth.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NeighbourFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NeighbourFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NeighbourFragment extends DialogFragment {
    Context mContext;

    public NeighbourFragment() {
        mContext = getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("Set 'Neighbour' Device");
        alertDialogBuilder.setMessage("Is this device close you constantly & would you like to remove it from further readings?");
        //null should be your on click listener
        alertDialogBuilder.setPositiveButton("Yes", null);
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        return alertDialogBuilder.create();
    }
}
