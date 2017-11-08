package com.tenor.android.core.service;

import android.support.annotation.NonNull;

import java.io.Serializable;


public interface IAaidListener extends Serializable {
    void success(@NonNull String aaid);

    void failure(@AaidInfo.State int state);
}
