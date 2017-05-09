package com.tenor.android.core.util;

import com.squareup.otto.Bus;

public class BusManager {

    private static BusManager sInstance;

    private Bus mBus;

    protected BusManager() {
        mBus = new Bus();
    }

    public static BusManager getInstance() {
        if (sInstance == null) {
            sInstance = new BusManager();
        }
        return sInstance;
    }

    public static Bus getBus() {
        return getInstance().mBus;
    }
}
