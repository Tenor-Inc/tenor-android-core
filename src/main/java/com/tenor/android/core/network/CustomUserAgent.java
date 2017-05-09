package com.tenor.android.core.network;

import android.os.Build;
import android.support.annotation.NonNull;

import com.tenor.android.core.constant.StringConstant;

/**
 * Configuration of custom user agent for network request
 */
public class CustomUserAgent {

    private final String mApplicationId;
    private final String mVersionName;
    private final String mVersionCode;
    private String mLocale;
    private String mAndroidVersion;
    private String mSdkVersion;
    private String mSupportLibraryVersion;
    private String mPhoneModel;

    private CustomUserAgent(@NonNull final CustomUserAgent.Builder builder) {
        mApplicationId = builder.mApplicationId;
        mVersionName = builder.mVersionName;
        mVersionCode = String.valueOf(builder.mVersionCode);
        mLocale = builder.mLocale;
        mAndroidVersion = builder.mAndroidVersion;
        mSdkVersion = String.valueOf(builder.mSdkVersion);
        mSupportLibraryVersion = builder.mSupportLibraryVersion;
        mPhoneModel = builder.mPhoneModel;
    }

    public String getApplicationId() {
        return mApplicationId;
    }

    public String getVersionName() {
        return mVersionName;
    }

    public String getVersionCode() {
        return mVersionCode;
    }

    public String getLocale() {
        return mLocale;
    }

    public String getAndroidVersion() {
        return mAndroidVersion;
    }

    public String getSdkVersion() {
        return mSdkVersion;
    }

    public String getSupportLibraryVersion() {
        return mSupportLibraryVersion;
    }

    public String getPhoneModel() {
        return mPhoneModel;
    }

    @NonNull
    public String toString() {
        return StringConstant.encode(mApplicationId) +
                StringConstant.SLASH +
                StringConstant.encode(mVersionName) +
                StringConstant.SLASH +
                StringConstant.encode(mVersionCode) +
                StringConstant.SLASH +
                StringConstant.encode(mLocale) +
                " (Android " +
                StringConstant.encode(mAndroidVersion) +
                StringConstant.SLASH +
                StringConstant.encode(mSdkVersion) +
                "; SptLib/" +
                StringConstant.encode(mSupportLibraryVersion) +
                "; " +
                StringConstant.encode(mPhoneModel) +
                ")";
    }

    public static class Builder {

        private final String mApplicationId;
        private final String mVersionName;
        private final int mVersionCode;
        private String mLocale;
        private String mAndroidVersion = Build.VERSION.RELEASE;
        private int mSdkVersion = Build.VERSION.SDK_INT;
        private String mSupportLibraryVersion;
        private String mPhoneModel = Build.MODEL;

        public Builder(@NonNull final String applicationId,
                       @NonNull final String versionName,
                       final int versionCode) {
            mApplicationId = applicationId;
            mVersionName = versionName;
            mVersionCode = versionCode;
        }

        public Builder locale(@NonNull final String locale) {
            mLocale = locale;
            return this;
        }

        public Builder androidVersion(@NonNull final String androidVersion) {
            mAndroidVersion = androidVersion;
            return this;
        }

        public Builder sdkVersion(final int sdkVersion) {
            mSdkVersion = sdkVersion;
            return this;
        }

        public Builder supportLibraryVersion(@NonNull final String supportLibraryVersion) {
            mSupportLibraryVersion = supportLibraryVersion;
            return this;
        }

        public Builder phoneModel(@NonNull final String phoneModel) {
            mPhoneModel = phoneModel;
            return this;
        }

        public CustomUserAgent build() {
            return new CustomUserAgent(this);
        }
    }
}
