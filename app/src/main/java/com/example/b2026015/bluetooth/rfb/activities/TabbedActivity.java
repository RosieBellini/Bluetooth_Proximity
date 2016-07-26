package com.example.b2026015.bluetooth.rfb.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.b2026015.bluetooth.R;
import com.example.b2026015.bluetooth.rfb.entities.BTDevice;
import com.example.b2026015.bluetooth.rfb.entities.Beacon;

import java.util.ArrayList;

public class TabbedActivity extends AppCompatActivity {

    private static ArrayList<Beacon> beaconList = new ArrayList<>();
    private static ArrayList<BTDevice> BTDeviceList = new ArrayList<>();

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Turn on bluetooth if not on already
        turnOnBluetooth();

    }

    private void turnOnBluetooth()
    {
        Intent intentBtEnabled = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        int REQUEST_ENABLE_BT = 1;
        startActivityForResult(intentBtEnabled, REQUEST_ENABLE_BT);
    }

    // Adds new entity to arrayLists
    public static boolean addNewEntity(String identifier, long pTimeStamp, String pName, String pMACAddress, long pRSSI, double pPower, double pDistance ) {

        if (identifier.equals("Beacon")) {
            Beacon nBeacon = new Beacon(pTimeStamp, pName, pMACAddress, pRSSI, pPower, pDistance);

            if (beaconList.isEmpty()) {
                beaconList.add(nBeacon);
                return true;

            } else {
                for (Beacon beacon : beaconList) {
                    //System.out.println("NEW:" + nBeacon.getMACAddress() + "EXISTING:" + beacon.getMACAddress());
                    if (nBeacon.getMACAddress().contentEquals(beacon.getMACAddress())) // beacon may already exist
                    {
                        beacon.addRSSIReading(pRSSI);
                        //beacon exists already so add rssi value to it
                        return false; //duplicate
                    }
                }
                beaconList.add(nBeacon); // new beacon found
                return true; // added successfully
            }

        } else if (identifier.equals("BTDevice")) {
            BTDevice nBTDevice = new BTDevice(pTimeStamp, pName, pMACAddress, pRSSI, pPower, pDistance);

            if (BTDeviceList.isEmpty()) {
                BTDeviceList.add(nBTDevice);
                return true;

            } else {
                for (BTDevice BTDevice : BTDeviceList) {
                    //System.out.println("NEW:" + nBTDevice.getMACAddress() + "EXISTING:" + BTDevice.getMACAddress());
                    if (nBTDevice.getMACAddress().contentEquals(BTDevice.getMACAddress())) {
                        BTDevice.addRSSIReading(pRSSI);
                        //beacon exists already so add rssi value to it
                        return false; //duplicate
                    }
                }
            }
            BTDeviceList.add(nBTDevice);
            return true;
        }
        return false; // entity was not added
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if((getArguments().getInt(ARG_SECTION_NUMBER) == 1)) {
                View rootView = inflater.inflate(R.layout.fragment_tabbed, container, false);
                TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                return rootView;
            }
            else {
                View rootView = inflater.inflate(R.layout.fragment_tabbed, container, false);
                TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                return rootView;
            }

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Page 1";
                case 1:
                    return "Page 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
