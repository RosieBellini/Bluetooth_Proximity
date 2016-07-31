package com.example.b2026015.bluetooth.rfb.entities;

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

    public InteractionTimer(long commence) {
        dateStart = new Date().toString();
        startedAt = commence;
    }

    public void endTimer(long finish) {
        dateFinish = new Date().toString();
        finishedAt = finish - startedAt;
    }

    public String getDate() {
        Date date = new Date();
        return dateFormat.format(date);
    }

    public long getInteractionLength() {
        return finishedAt - startedAt;
    }
}
