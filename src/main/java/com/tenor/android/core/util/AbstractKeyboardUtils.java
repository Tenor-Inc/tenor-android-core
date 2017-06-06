package com.tenor.android.core.util;

import android.annotation.TargetApi;
import android.content.ClipDescription;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v13.view.inputmethod.EditorInfoCompat;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

import com.tenor.android.core.constant.ContentFormat;
import com.tenor.android.core.constant.ContentFormats;

import java.util.ArrayList;
import java.util.List;

/**
 * Determine keyboard related information
 * <p>
 * For use if implementing our SDK via a keyboard
 */
public abstract class AbstractKeyboardUtils {

    /**
     * Check if a given {@link EditorInfo} supports using rich content commit API to send gif
     * <p>
     * https://developer.android.com/guide/topics/text/image-keyboard.html
     *
     * @param editorInfo the {@link EditorInfo}
     * @return true if rich content commit API is supported
     */
    @RequiresApi(13)
    @TargetApi(13)
    public static boolean isRichContentSupported(@Nullable final EditorInfo editorInfo) {
        return isRichMimeTypeSupported(editorInfo, ContentFormats.IMAGE_GIF);
    }

    /**
     * Check if a given {@link EditorInfo} supports given mime type commit
     *
     * @param editorInfo the {@link EditorInfo}
     * @param mimeType   the given mime type in {@link ContentFormats}
     * @return true if rich content commit API is supported
     */
    @RequiresApi(13)
    @TargetApi(13)
    public static boolean isRichMimeTypeSupported(@Nullable final EditorInfo editorInfo,
                                                  @ContentFormat final String mimeType) {
        if (editorInfo == null || TextUtils.isEmpty(mimeType)) {
            return false;
        }

        String[] mimeTypes = EditorInfoCompat.getContentMimeTypes(editorInfo);
        for (String mt : mimeTypes) {
            if (ClipDescription.compareMimeTypes(mt, mimeType)) {
                return true;
            }
        }
        return false;
    }
}
