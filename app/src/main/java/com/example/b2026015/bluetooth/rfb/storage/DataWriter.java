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

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Hugo Nicolau on 13/11/2014.
 *
 * Writes data to log file
 */

public class DataWriter extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "DataWriter";

    private List<String> mData;
    private String mFilePath;
    private String mFolderPath;
    private boolean mIsSyncWriting;


    public DataWriter(ArrayList<String> data, String folderPath, String filePath, boolean sync) {
        mData = data;
        mFilePath = filePath;
        mFolderPath = folderPath;
        mIsSyncWriting = sync;
        if (mIsSyncWriting) {
            if (mData != null && !mData.isEmpty()) {
                writeFile();
            }
        }
    }

    private void writeFile(){

        // creates folder
        File folder = new File(mFolderPath);
        if (!folder.exists())
            folder.mkdirs();

        try {
            FileWriter fw = new FileWriter(mFilePath, true);
            for(String line : mData){
                fw.write(line + "\n");
            }
            fw.flush();
            fw.close();
            Log.v(TAG,mData.size() + " Data write ok: " +mFilePath);
            mData = new ArrayList<String>();
        } catch (IOException e) {
            Log.v(TAG,"Data write BROKEN: " + mFilePath + " " + e.getMessage());
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (mIsSyncWriting)
            return null;
        if (mData != null && !mData.isEmpty()) {
            writeFile();
        }
        return null;
    }


}
