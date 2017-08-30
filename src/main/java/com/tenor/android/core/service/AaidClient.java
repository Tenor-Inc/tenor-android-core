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

final class AaidClient {

    @WorkerThread
    public static void init(@NonNull Context app, @Nullable IAaidListener listener) {
        final String aaid = getAdvertisingId(app);
        AbstractSessionUtils.setAndroidAdvertiseId(app, aaid);

        if (listener != null) {
            if (!TextUtils.isEmpty(aaid)) {
                listener.success(aaid);
            } else {
                listener.failure();
            }
        }
    }

    @WorkerThread
    @NonNull
    private static String getAdvertisingId(Context context) {
        if (isOnMainThread()) {
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
                Info info = new Info(adInterface.getId(), adInterface.isLimitAdTrackingEnabled(true));
                return !info.isLimitAdTrackingEnabled() ? info.getId() : StringConstant.EMPTY;
            }
        } catch (Throwable ignored) {
            return StringConstant.EMPTY;
        } finally {
            context.unbindService(connection);
        }
        return StringConstant.EMPTY;
    }

    /**
     * @return {@code true} if called on the main thread, {@code false} otherwise.
     */
    private static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * Mirror of AdvertisingIdClient#Info
     */
    private static final class Info {

        @NonNull
        private final String mAdvertisingId;
        private final boolean mLimitAdTrackingEnabled;

        public Info(@Nullable String advertisingId, boolean limitAdTrackingEnabled) {
            mAdvertisingId = StringConstant.getOrEmpty(advertisingId);
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
