package com.tenor.android.core.service;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;

import com.tenor.android.core.constant.StringConstant;

/**
 * Base class for {@code AaidService} to be use with v26 APIs.
 */
public final class AaidServiceApi26 extends JobIntentService {

    @Nullable
    private static IAaidListener sListener;

    @Override
    protected void onHandleWork(@Nullable Intent intent) {

        if (intent == null || intent.getAction() == null) {
            if (sListener != null) {
                sListener.failure(AaidInfo.AAID_FAILURE);
            }
            return;
        }

        String action = StringConstant.getOrEmpty(intent.getAction());
        switch (action) {
            case AaidService.ACTION_GET_AAID:
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
        enqueueWork(context, AaidServiceApi26.class, AaidService.JOB_ID_GET_AAID,
                new Intent(AaidService.ACTION_GET_AAID));
    }
}