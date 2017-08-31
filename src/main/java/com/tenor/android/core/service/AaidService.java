package com.tenor.android.core.service;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;


public class AaidService extends JobIntentService {

    private static final int JOB_ID_GET_AAID = 1611;
    private static final String ACTION_GET_AAID = "ACTION_GET_AAID";

    @Nullable
    private static IAaidListener sListener;

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        switch (intent.getAction()) {
            case ACTION_GET_AAID:
                AaidClient.init(getApplicationContext(), sListener);
                break;
            default:
                // do nothing
                break;
        }
    }

    public static void requestAaid(@NonNull Context context) {
        requestAaid(context, null);
    }

    public static void requestAaid(@NonNull Context context, @Nullable IAaidListener listener) {
        sListener = listener;
        enqueueWork(context, AaidService.class, JOB_ID_GET_AAID, new Intent(ACTION_GET_AAID));
    }
}