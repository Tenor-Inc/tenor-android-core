package com.tenor.android.core.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.listener.IThrowableListener;
import com.tenor.android.core.util.AbstractNetworkUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class UserAgentInterceptor implements Interceptor, IThrowableListener<Request> {

    private static final String KEY_USER_AGENT = "User-Agent";
    private final String mUserAgent;

    public UserAgentInterceptor(@NonNull final Context context, @Nullable final CustomUserAgent customUserAgent) {
        this(context, customUserAgent != null ? customUserAgent.toString() : StringConstant.EMPTY);
    }

    public UserAgentInterceptor(@NonNull final Context context, @Nullable final String userAgent) {
        if (!TextUtils.isEmpty(userAgent)) {
            mUserAgent = userAgent;
        } else {
            mUserAgent = AbstractNetworkUtils.getUserAgent(context).toString();
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            final Request original = chain.request();
            final Request request = original.newBuilder()
                    .header(KEY_USER_AGENT, mUserAgent)
                    .build();
            return chain.proceed(request);
        } catch (Throwable throwable) {
            onReceiveThrowable(throwable, chain.request());
            throw new IOException(throwable);
        }
    }

    @Override
    public final void onReceiveThrowable(@Nullable Throwable throwable) {
        // do nothing and block subclass
    }

    @Override
    public void onReceiveThrowable(@Nullable Throwable throwable, @Nullable Request event) {
        // do nothing and allow subclass
    }

    @Override
    public final void onReceiveThrowable(@Nullable Throwable throwable, @Nullable Request event, @Nullable List<Request> events) {
        // do nothing and block subclass
    }
}
