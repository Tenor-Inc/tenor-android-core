package com.tenor.android.core.service;

import android.app.IntentService;
import android.content.Intent;

import com.tenor.android.core.util.AaidClient;


public class AaidService extends IntentService {

    public static final String ACTION_GET_AAID = "ACTION_GET_AAID";

    public AaidService() {
        super(AaidService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {

        if (workIntent == null) {
            return;
        }

        switch (workIntent.getAction()) {
            case ACTION_GET_AAID:
                AaidClient.init(getApplicationContext());
                break;
            default:
                // do nothing
                break;
        }
    }
}