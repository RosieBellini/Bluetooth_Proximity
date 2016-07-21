/*
 * The MIT License (MIT)
 * Copyright (c) 2016 Kyle Montague
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.example.b2026015.bluetooth.rfb.storage;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by Kyle Montague on 10/11/2014.
 */
public class Logger {



    private static final String SUBTAG = "Logger: ";

    protected String mFolderName = Environment.getExternalStorageDirectory().getPath()+"/Traces";
    protected String mFilename = "";
    protected String mName;
    protected String mDeviceName ="MOBILE";
    protected ArrayList<String> mData = null;


    private BroadcastReceiver mStorageReceiver = null;

    protected int mFlushThreshold = 1000;
    protected final int MIN_FLUSH_THRESHOLD = 50;


    public enum FileFormat{
        csv,
        txt,
        xml
    };

    public Logger(String name, int flushThreshold, long timestamp, FileFormat format) {
        // initialize variables

        mName = name;
        mFlushThreshold = Math.max(MIN_FLUSH_THRESHOLD, flushThreshold);
        mData = new ArrayList<String>();
        setFileInfo(timestamp,format);
        mDeviceName = BluetoothAdapter.getDefaultAdapter().getAddress();
    }


    public String getFilename(){
        return mFilename;
    }


    public void setFileInfo(long timestamp, FileFormat format){
        mFolderName = mFolderName+"/"+timestamp+"/";
        mFilename = mFolderName+mDeviceName+"_"+mName+"."+format;
    }

    public void flush(){
        //Log.v(BaseLogger.TAG, SUBTAG + "Flush - "+mData.size()+" file: "+mFilename);
        DataWriter w = new DataWriter(mData, mFolderName, mFilename, false);
        w.execute();
        mData = new ArrayList<String>();
    }

    public void writeAsync(String data){
        mData.add(data);
        if(mData.size() >= mFlushThreshold)
            flush();
    }

    public void writeSync(ArrayList<String> data){
        mData.addAll(data);
        flush();
    }

    public void writeFile(File file){
        String name = file.getName();
        file.renameTo(new File(mFolderName + "/" + name));
    }


    public String parentDirectory(){
        return mFolderName;
    }
}
