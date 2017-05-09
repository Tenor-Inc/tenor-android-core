package com.tenor.android.core.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tenor.android.core.constant.ContentFormat;
import com.tenor.android.core.constant.ContentFormats;
import com.tenor.android.core.constant.MediaFormats;
import com.tenor.android.core.constant.StringConstant;

/**
 * Manage and check content type
 */
public class ContentFormatUtils {

    /**
     * Checks the mime type of a resource
     *
     * @param url - url of resource
     * @return - resource String of the <b>mime type</b>.  gif as default
     */
    @NonNull
    @ContentFormat
    public static String getUrlContentType(@NonNull String url) {
        if (!TextUtils.isEmpty(url) && url.contains(MediaFormats.MP4)) {
            return ContentFormats.IMAGE_MP4;
        }
        return ContentFormats.IMAGE_GIF;
    }

    /**
     * Checks if asset is the mp4 type
     *
     * @param url String of the url of the asset
     * @return boolean of if url extension is 'mp4'
     */
    public static boolean isMP4(@NonNull String url) {
        return !TextUtils.isEmpty(url) && ContentFormats.IMAGE_MP4.equals(getUrlContentType(url));
    }

    /**
     * Checks if asset is the gif type
     *
     * @param url String of the url of the asset
     * @return boolean of if url extension is 'gif'
     */
    public static boolean isGif(@NonNull String url) {
        return !TextUtils.isEmpty(url) && ContentFormats.IMAGE_GIF.equals(getUrlContentType(url));
    }

    /**
     * Checks if asset is the gif type
     *
     * @param context the context
     * @param uri String of the url of the asset
     * @return boolean of if url extension is 'gif'
     */
    private boolean isGif(@NonNull final Context context, @NonNull final Uri uri) {
        String filePath = StringConstant.EMPTY;
        if ("file".equals(uri.getScheme())) {
            filePath = uri.getLastPathSegment();
        } else {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
        }

        int dotPosition = !TextUtils.isEmpty(filePath) ? filePath.lastIndexOf(StringConstant.DOT) : -1;
        String extension = dotPosition > -1 ? filePath.substring(dotPosition) : StringConstant.EMPTY;

        return ".gif".equals(extension);
    }

    /**
     * Get content extension
     *
     * @param hasAudio indicates if file of interest has audio
     * @return the extension of file
     */
    public static String getExtension(boolean hasAudio) {
        return hasAudio ? MediaFormats.MP4 : MediaFormats.GIF;
    }
}
