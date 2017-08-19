package com.tenor.android.core.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.util.AbstractSessionUtils;

import java.util.concurrent.LinkedBlockingQueue;

public class AaidClient {

    public interface OnInitAaidListener {
        void onReceiveAaidSucceeded(@NonNull String aaid);

        void onReceiveAaidFaileded();
    }

    @WorkerThread
    public static void init(@NonNull Context context) {
        init(context, null);
    }

    @WorkerThread
    public static void init(@NonNull Context context, @Nullable final OnInitAaidListener listener) {
        if (context == null) {
            throw new IllegalStateException("context cannot be null");
        }
        final String aaid = getAdvertisingId(context);
        AbstractSessionUtils.setAndroidAdvertiseId(context, aaid);

        if (listener != null) {
            if (!TextUtils.isEmpty(aaid)) {
                listener.onReceiveAaidSucceeded(aaid);
            } else {
                listener.onReceiveAaidFaileded();
            }
        }
    }

    @WorkerThread
    @NonNull
    private static String getAdvertisingId(Context context) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("Cannot be called from the main thread");
        }

        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo("com.android.vending", 0);
        } catch (Throwable throwable) {
            return StringConstant.EMPTY;
        }

        AdvertisingConnection connection = new AdvertisingConnection();
        Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
        intent.setPackage("com.google.android.gms");

        final boolean googleServiceBinded;
        try {
            googleServiceBinded = context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        } catch (Throwable ignored) {
            // Google Play connection failed, or it is not installed
            return StringConstant.EMPTY;
        }

        try {
            if (googleServiceBinded) {
                AdvertisingInterface adInterface = new AdvertisingInterface(connection.getBinder());
                AdInfo adInfo = new AdInfo(adInterface.getId(), adInterface.isLimitAdTrackingEnabled(true));
                return !adInfo.isLimitAdTrackingEnabled() ? adInfo.getId() : StringConstant.EMPTY;
            }
        } catch (Throwable ignored) {
            return StringConstant.EMPTY;
        } finally {
            context.unbindService(connection);
        }
        return StringConstant.EMPTY;
    }

    private static final class AdInfo {

        @NonNull
        private final String mAdvertisingId;
        private final boolean mLimitAdTrackingEnabled;

        public AdInfo(@Nullable final String advertisingId, final boolean limitAdTrackingEnabled) {
            mAdvertisingId = !TextUtils.isEmpty(advertisingId) ? advertisingId : StringConstant.EMPTY;
            mLimitAdTrackingEnabled = limitAdTrackingEnabled;
        }

        @NonNull
        public String getId() {
            return mAdvertisingId;
        }

        public boolean isLimitAdTrackingEnabled() {
            return mLimitAdTrackingEnabled;
        }
    }

    private static final class AdvertisingConnection implements ServiceConnection {
        private boolean mRetrieved = false;
        private final LinkedBlockingQueue<IBinder> mQueue = new LinkedBlockingQueue<>(1);

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                mQueue.put(service);
            } catch (InterruptedException ignored) {
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        public IBinder getBinder() throws InterruptedException {
            if (mRetrieved) {
                throw new IllegalStateException();
            }
            mRetrieved = true;
            return mQueue.take();
        }
    }

    private static final class AdvertisingInterface implements IInterface {
        @NonNull
        private final IBinder mBinder;

        public AdvertisingInterface(@NonNull final IBinder binder) {
            mBinder = binder;
        }

        @NonNull
        public IBinder asBinder() {
            return mBinder;
        }

        public String getId() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            String id;
            try {
                data.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                mBinder.transact(1, data, reply, 0);
                reply.readException();
                id = reply.readString();
            } finally {
                reply.recycle();
                data.recycle();
            }
            return id;
        }

        public boolean isLimitAdTrackingEnabled(boolean paramBoolean) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            boolean limitAdTracking;
            try {
                data.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                data.writeInt(paramBoolean ? 1 : 0);
                mBinder.transact(2, data, reply, 0);
                reply.readException();
                limitAdTracking = 0 != reply.readInt();
            } finally {
                reply.recycle();
                data.recycle();
            }
            return limitAdTracking;
        }
    }
}
