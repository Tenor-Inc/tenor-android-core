package com.tenor.android.core.util;

import android.annotation.TargetApi;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v13.view.inputmethod.EditorInfoCompat;
import android.support.v13.view.inputmethod.InputConnectionCompat;
import android.support.v13.view.inputmethod.InputContentInfoCompat;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

import com.tenor.android.core.constant.ContentFormat;
import com.tenor.android.core.constant.ContentFormats;
import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.network.ApiClient;

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

    /**
     * Send Gif using RCS
     *
     * @param inputMethodService the {@link InputMethodService} or its subclass
     * @param uri                the gif {@link Uri}
     */
    public static void commitGif(@NonNull final InputMethodService inputMethodService,
                                 @NonNull final Uri uri) {
        commitContent(inputMethodService, uri, ContentFormats.IMAGE_GIF);
    }

    /**
     * Send content using RCS
     *
     * @param inputMethodService the {@link InputMethodService} or its subclass
     * @param uri                the gif {@link Uri}
     * @param mimeTypes          the supported mime types in {@link ContentFormats}
     */
    public static void commitContent(@NonNull final InputMethodService inputMethodService,
                                     @NonNull final Uri uri,
                                     @Nullable @ContentFormat final String... mimeTypes) {

        if (mimeTypes == null || mimeTypes.length <= 0) {
            throw new IllegalArgumentException("MIME type cannot be empty");
        }

        final InputConnection inputConnection = inputMethodService.getCurrentInputConnection();
        final EditorInfo editorInfo = inputMethodService.getCurrentInputEditorInfo();

        /*
         * [ANDROID-1784] Make sure current input connection is still open
         */
        if (inputConnection == null || !inputConnection.beginBatchEdit()) {
            return;
        }

        int flags = 0;
        if (android.os.Build.VERSION.SDK_INT >= 25) {
            flags |= InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION;
        } else {
            try {
                // Use revokeUriPermission to revoke as needed.
                inputMethodService.grantUriPermission(
                        editorInfo.packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (Exception ignored) {
            }
        }

        final InputContentInfoCompat inputContentInfo = new InputContentInfoCompat(
                uri, new ClipDescription(uri.toString(), mimeTypes), null);
        InputConnectionCompat.commitContent(inputConnection, editorInfo, inputContentInfo, flags, null);
    }
}
