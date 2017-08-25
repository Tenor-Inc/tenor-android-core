package com.tenor.android.core.service;

import android.support.annotation.NonNull;


public interface IAaidListener {
    void success(@NonNull String aaid);

    void failure();
}
