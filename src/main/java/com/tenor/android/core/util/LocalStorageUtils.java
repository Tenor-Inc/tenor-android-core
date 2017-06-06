package com.tenor.android.core.util;


import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

/**
 * Interacts with local storage
 */
public class LocalStorageUtils {

    public interface IModifyLocalStorageListener {
        void onCreateStorageDirectoryFailed();
    }

    protected static final String LOCAL_URI_RAW_RESOURCE_FORMATTER = "android.resource://%1$s/%2$s";

    /**
     * the default gif storage folder name
     */
    private static final String DEFAULT_GIF_FOLDER_NAME = "Sent Gif";
    private static final String DEFAULT_TENOR_PROFILE_PHOTOS_FOLDER_NAME = "Tenor Profile Pics";
    private static final String DEFAULT_SCREEN_RECORDINGS_FOLDER_NAME = "Screen Recordings";

    /**
     * Get the gif storage folder directory
     *
     * @return storage directory with the default gif storage folder name
     */
    public static File getGifStorageDir() {
        return getGifStorageDir(DEFAULT_GIF_FOLDER_NAME, null);
    }

    /**
     * Get the gif storage folder directory
     *
     * @param folderName the given gif storage folder name
     * @return storage directory with the given gif storage folder name
     */
    public static File getGifStorageDir(@NonNull String folderName, @Nullable IModifyLocalStorageListener IModifyLocalStorageListener) {
        folderName = !TextUtils.isEmpty(folderName) ? folderName : DEFAULT_GIF_FOLDER_NAME;
        return getExternalPublicStorageDir(folderName, Environment.DIRECTORY_PICTURES, IModifyLocalStorageListener);
    }

    /**
     * Get the profile photos storage folder directory
     *
     * @param folderName the given gif storage folder name
     * @return storage directory with the given gif storage folder name
     */
    public static File getProfilePhotosStorageDir(@NonNull String folderName, @Nullable IModifyLocalStorageListener IModifyLocalStorageListener) {
        folderName = !TextUtils.isEmpty(folderName) ? folderName : DEFAULT_TENOR_PROFILE_PHOTOS_FOLDER_NAME;
        return getExternalPublicStorageDir(folderName, Environment.DIRECTORY_PICTURES, IModifyLocalStorageListener);
    }

    /**
     * Get the screen recordings storage folder directory
     *
     * @param folderName the given screen recordings storage folder name
     * @return storage directory with the given screen recording storage folder name
     */
    public static File getScreenRecordingsStorageDir(@NonNull String folderName, @Nullable IModifyLocalStorageListener IModifyLocalStorageListener) {
        folderName = !TextUtils.isEmpty(folderName) ? folderName : DEFAULT_SCREEN_RECORDINGS_FOLDER_NAME;
        return getExternalPublicStorageDir(folderName, Environment.DIRECTORY_MOVIES, IModifyLocalStorageListener);
    }

    private static File getExternalPublicStorageDir(@NonNull String folderName,
                                                    @NonNull String environmentDirectory,
                                                    @Nullable IModifyLocalStorageListener IModifyLocalStorageListener) {
        File file = new File(
                Environment.getExternalStoragePublicDirectory(environmentDirectory),
                folderName);
        if (!file.exists() && !file.mkdirs() && IModifyLocalStorageListener != null) {
            IModifyLocalStorageListener.onCreateStorageDirectoryFailed();
        }
        return file;
    }

    public static boolean deleteGif(Uri uri) {
        if (uri == null || TextUtils.isEmpty(uri.getPath())) {
            return false;
        }

        File file = new File(uri.getPath());
        //noinspection ResultOfMethodCallIgnored
        return file.delete();
    }

    public static Uri getLocalUriForRawResource(@NonNull String applicationId, int resId) {
        return Uri.parse(String.format(LOCAL_URI_RAW_RESOURCE_FORMATTER, applicationId, resId));
    }

    public static byte[] readFromAssets(Context context, String path) {
        byte[] data = null;

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream is = context.getAssets().open(path);

            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[65536];
            while (is.read(buffer) != -1) {
                outputStream.write(buffer);
            }

            data = outputStream.toByteArray();
            outputStream.close();
            is.close();
        } catch (IOException ignored) {
        }

        return data;
    }

    public static boolean copyFile(String source, String destination) {
        FileChannel fileInputChannel = null;
        FileChannel fileOutputChannel = null;

        try {
            File file = new File(destination);

            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                return false;
            }

            fileInputChannel = new FileInputStream(source).getChannel();
            fileOutputChannel = new FileOutputStream(file).getChannel();
            fileInputChannel.transferTo(0, fileInputChannel.size(), fileOutputChannel);
        } catch (Exception ignored) {
            return false;
        }

        try {
            if (fileInputChannel.isOpen()) {
                fileInputChannel.close();
            }

            if (fileOutputChannel.isOpen()) {
                fileOutputChannel.close();
            }
        } catch (IOException ignored) {
            return false;
        }
        return true;
    }

    /**
     * API 24+ Compatible method for getting {@link Uri} from {@link File}
     */
    public static Uri getUriForFileCompat(@NonNull final Context applicationContext,
                                          @NonNull final String applicationId,
                                          @NonNull final File output) {
        if (Build.VERSION.SDK_INT < 24) {
            return Uri.fromFile(output);
        } else {
            return FileProvider.getUriForFile(applicationContext, applicationId + ".provider", output);
        }
    }
}