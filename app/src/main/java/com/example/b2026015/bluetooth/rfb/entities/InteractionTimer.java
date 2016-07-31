package com.example.b2026015.bluetooth.rfb.entities;

import java.lang.reflect.Array;
import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InteractionTimer {

    private String dateStart;
    private String dateFinish;
    private long startedAt;
    private long finishedAt;
    private long interactionLength;
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); //29/07/2016 15:59:48

    // When created records current time in milliseconds + makes new Date for database
    public InteractionTimer(long commence) {
        dateStart = new Date().toString();
        startedAt = commence;
    }

    // Finishes timer, creates date with time finished
    public void endTimer(long finish) {
        dateFinish = new Date().toString();
        finishedAt = finish;
    }

    public String[] getDates() {
        String[] startEnd = new String[2];
        startEnd[0] = dateStart;
        startEnd[1] = dateFinish;
        return startEnd;
    }

    public long getInteractionLength() {
        return finishedAt - startedAt;
    }
}
