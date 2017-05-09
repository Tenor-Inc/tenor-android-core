package com.tenor.android.core.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.tenor.android.core.BuildConfig;
import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.network.CustomUserAgent;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Contains methods to access the network status
 */
public abstract class AbstractNetworkUtils {

    private static CustomUserAgent sUserAgent;

    private static int sBatchSize = -1;

    /**
     * Get the batch size
     */
    public static int getBatchSize(@Nullable final Context context) {
        // Use the cached sBatchSize
        return sBatchSize > 0 ? sBatchSize : updateBatchSize(context);
    }

    /**
     * Update batch size based on network type and status
     *
     * @param context the context, usually the application context
     */
    public static int updateBatchSize(@Nullable final Context context) {
        if (context == null) {
            sBatchSize = 6;
            return sBatchSize;
        }

        // No network, use small batch size
        NetworkInfo info = getNetworkInfo(context);
        if (info == null || !info.isConnected()) {
            sBatchSize = 6;
            return sBatchSize;
        }

        // In WiFi, use large batch size
        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            sBatchSize = 24;
            return sBatchSize;
        }

        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            switch (info.getSubtype()) {
                case TelephonyManager.NETWORK_TYPE_HSDPA: // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSUPA: // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_EHRPD: // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_LTE: // ~ 10+ Mbps
                    sBatchSize = 24;
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_0: // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A: // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_HSPA: // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_UMTS: // ~ 400-7000 kbps
                    sBatchSize = 12;
                    break;
                case TelephonyManager.NETWORK_TYPE_IDEN: // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA: // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_1xRTT: // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE: // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS: // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    sBatchSize = 6;
                    break;
            }
        } else {
            // In other undetermined network, use small batch size
            sBatchSize = 6;
        }
        return sBatchSize;
    }

    /**
     * Update batch size based on network speed
     *
     * @param speed network speed measured in KB/s
     */
    public static void updateBatchSize(float speed) {
        final int kbps = (int) (speed * 8);
        final int batchSize;
        if (kbps > 1000) {
            batchSize = 24;
        } else if (kbps > 400) {
            batchSize = 12;
        } else {
            batchSize = 6;
        }
        sBatchSize = batchSize;
    }

    /**
     * Gets the network info of the device
     *
     * @param context the context
     * @return true if the device is connected to internet
     */
    public static boolean isOnline(@Nullable final Context context) {
        if (context == null) {
            return false;
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Gets the network info of the device
     *
     * @param context the context
     * @return current network info
     */
    @Nullable
    public static NetworkInfo getNetworkInfo(@Nullable final Context context) {
        if (context == null) {
            return null;
        }

        try {
            /*
             * [ANDROID-1380]
             *
             * Some OEM devices have custom permission implementation that ask
             * `ACCESS_NETWORK_STATE` even though it has been granted to the app
             */
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm != null ? cm.getActiveNetworkInfo() : null;
        } catch (SecurityException ignored) {
            return null;
        }
    }

    /**
     * Gets the network info of the device
     *
     * @param context the context
     * @return the current network type name
     */
    @NonNull
    public static String getNetworkTypeName(@Nullable final Context context) {
        if (context == null) {
            return StringConstant.EMPTY;
        }
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected()) ? info.getTypeName() : StringConstant.UNKNOWN;
    }

    /**
     * Gets the network info of the device
     *
     * @param context the context
     * @return the current network type name
     */
    @NonNull
    public static String getNetworkTypeNameCompat(@Nullable final Context context) {
        if (context == null) {
            return StringConstant.EMPTY;
        }
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected()) ? getNetworkTypeNameCompat(info.getType(), info.getSubtype()) : StringConstant.EMPTY;
    }

    /**
     * Gets the network info of the device
     *
     * @param context the context
     * @return the current network subtype name
     */
    @NonNull
    public static String getNetworkSubtypeName(@Nullable final Context context) {
        if (context == null) {
            return StringConstant.EMPTY;
        }
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected()) ? info.getSubtypeName() : StringConstant.UNKNOWN;
    }

    /**
     * Checks if device is connected to any network
     *
     * @param context the context
     * @return true if connected
     */
    public static boolean isNetworkConnected(@NonNull Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    /**
     * Checks if device is connected to a wifi network
     *
     * @param context the context
     * @return true if device is connected to Wifi
     */
    public static boolean isWifiConnected(@NonNull Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Checks if device is connected to a cellular network
     *
     * @param context the context
     * @return true if connected
     */
    public static boolean isCellularConnected(@NonNull Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Checks if device is connected to a strong and fast network
     *
     * @param context the context
     * @return true if device is connected to a fast network connection
     */
    public static boolean isFastNetworkConnected(@NonNull Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && isNetworkConnected(info.getType(), info.getSubtype()));
    }

    /**
     * Checks network type and subtype, to determine if network is fast (in kbps)
     *
     * @param type    ConnectivityManager type
     * @param subType TelephonyManager type for mobile
     * @return true if device is connected to a fast network connection
     */
    private static boolean isNetworkConnected(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) return true;

        if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps

                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps

                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps

                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps

                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps

                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps

                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps

                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps

                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps

                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps

                case TelephonyManager.NETWORK_TYPE_EHRPD:
                    return true; // ~ 1-2 Mbps

                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    return true; // ~ 5 Mbps

                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    return true; // ~ 10-20 Mbps

                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return false; // ~25 kbps

                case TelephonyManager.NETWORK_TYPE_LTE:
                    return true; // ~ 10+ Mbps

                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        }
        return false;
    }

    /**
     * Checks network type and subtype, to determine if network is fast (in kbps)
     *
     * @param type    ConnectivityManager type
     * @param subType TelephonyManager type for mobile
     * @return true if device is connected to a fast network connection
     */
    private static String getNetworkTypeNameCompat(int type, int subType) {

        final String t;
        switch (type) {
            case ConnectivityManager.TYPE_BLUETOOTH:
                t = "BLUETOOTH";
                break;
            case ConnectivityManager.TYPE_DUMMY:
                t = "DUMMY";
                break;
            case ConnectivityManager.TYPE_ETHERNET:
                t = "ETHERNET";
                break;
            case ConnectivityManager.TYPE_MOBILE:
                t = "MOBILE";
                break;
            case ConnectivityManager.TYPE_MOBILE_DUN:
                t = "MOBILE_DUN";
                break;
            case ConnectivityManager.TYPE_MOBILE_HIPRI:
                t = "MOBILE_HIPRI";
                break;
            case ConnectivityManager.TYPE_MOBILE_MMS:
                t = "MOBILE_MMS";
                break;
            case ConnectivityManager.TYPE_MOBILE_SUPL:
                t = "MOBILE_SUPL";
                break;
            case ConnectivityManager.TYPE_VPN:
                t = "VPN";
                break;
            case ConnectivityManager.TYPE_WIFI:
                t = "WIFI";
                break;
            case ConnectivityManager.TYPE_WIMAX:
                t = "WIMAX";
                break;
            default:
                t = StringConstant.UNKNOWN;
                break;
        }

        final String st;
        switch (subType) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                st = "1xRTT"; // ~ 50-100 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                st = "CDMA"; // ~ 14-64 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                st = "EDGE"; // ~ 50-100 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                st = "EVDO_0"; // ~ 400-1000 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                st = "EVDO_A"; // ~ 600-1400 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                st = "GPRS"; // ~ 100 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                st = "HSDPA"; // ~ 2-14 Mbps
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                st = "HSPA"; // ~ 700-1700 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                st = "HSUPA"; // ~ 1-23 Mbps
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                st = "UMTS"; // ~ 400-7000 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                st = "EHRPD"; // ~ 1-2 Mbps
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                st = "EVDO_B"; // ~ 5 Mbps
                break;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                st = "HSPAP"; // ~ 10-20 Mbps
                break;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                st = "IDEN"; // ~25 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                st = "LTE"; // ~ 10+ Mbps
                break;
            case TelephonyManager.NETWORK_TYPE_GSM:
                st = "GSM"; // ~ 50-60 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_IWLAN:
                st = "IWLAN"; // ~ 11-54 Mbps
                break;
            case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                st = "TD_SCDMA"; // ~ 400-7000 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            default:
                st = StringConstant.UNKNOWN;
                break;
        }
        return t + StringConstant.DASH + st;
    }

    public static void initUserAgent(@NonNull final CustomUserAgent userAgent) {
        sUserAgent = userAgent;
    }

    public static CustomUserAgent getUserAgent(@NonNull Context context) {
        if (sUserAgent == null) {
            sUserAgent = new CustomUserAgent.Builder(BuildConfig.APPLICATION_ID,
                    BuildConfig.VERSION_NAME,
                    BuildConfig.VERSION_CODE)
                    .locale(AbstractLocaleUtils.getCurrentLocaleName(context))
                    .build();
        }
        return sUserAgent;
    }

    public static String parseIpAddress(@Nullable final String ip) {
        if (TextUtils.isEmpty(ip)) {
            throw new IllegalArgumentException("input: " + ip + " is neither IPv4, nor IPv6");
        }

        if (ip.indexOf(':') < 0) {
            // it is already a IPv4
            return ip;
        }

        int mark = ip.indexOf('%'); // drop ip6 zone suffix
        return mark < 0 ? ip.toUpperCase() : ip.substring(0, mark).toUpperCase();
    }

    public static String getIpAddress() {
        final Enumeration<NetworkInterface> nis;
        try {
            nis = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            return StringConstant.EMPTY;
        }

        NetworkInterface ni;
        Enumeration<InetAddress> eia;
        InetAddress ia;
        String ip;

        while (nis.hasMoreElements()) {
            ni = nis.nextElement();
            eia = ni.getInetAddresses();

            while (eia.hasMoreElements()) {
                ia = eia.nextElement();

                if (!ia.isLoopbackAddress()) {
                    ip = ia.getHostAddress();

                    if (!TextUtils.isEmpty(ip)) {
                        return parseIpAddress(ip);
                    }
                }
            }
        }
        return StringConstant.EMPTY;
    }
}