package com.tenor.android.core.service;

import android.app.Application;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tenor.android.core.constant.StringConstant;


/**
 * Base class for {@code AaidService} to be use with v3 APIs.
 */
public final class AaidServiceCompat extends IntentService {

    @Nullable
    private static IAaidListener sListener;

    public AaidServiceCompat() {
        super(AaidServiceCompat.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

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
        Context ctx = context;
        if (!(ctx instanceof Application)) {
            ctx = context.getApplicationContext();
        }
        Intent intent = new Intent(ctx, AaidServiceCompat.class).setAction(AaidService.ACTION_GET_AAID);
        context.startService(intent);
    }
}