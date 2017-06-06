package com.tenor.android.core.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tenor.android.core.constant.MediaFormats;
import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.listener.OnWriteCompletedListener;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Uses helper functions to create local versions of web gifs and mp4s, to be sent as raw data
 */
public abstract class AbstractLocalStorageHelper {

    protected static final int DOWNLOAD_THREAD_POOL_SIZE = 1;
    protected static final String SHARED_OUTPUT_FILE_NAME = ".animated";
    private static String sApplicationId = StringConstant.EMPTY;

    private final ThinDownloadManager mDownloadManager;

    private String mMediaType = MediaFormats.GIF;

    @NonNull
    protected abstract String generateUniqueFileName();

    @Nullable
    protected abstract File getDestinationFile(String extension);

    protected AbstractLocalStorageHelper() {
        mDownloadManager = new ThinDownloadManager(DOWNLOAD_THREAD_POOL_SIZE);
    }

    protected AbstractLocalStorageHelper(int threadPoolSize) {
        mDownloadManager = new ThinDownloadManager(threadPoolSize);
    }

    protected void cancelDownloads() {
        mDownloadManager.cancelAll();
    }

    public static void setApplicationId(@NonNull final String applicationId) {
        sApplicationId = applicationId;
    }

    public static String getApplicationId() {
        return sApplicationId;
    }

    /**
     * Writes and creates a local copy of a gif or mp4, returning the uri via an OnWriteCompletedListener
     * Note: you MUST have a FileProvider specified in your manifest to use this function
     *
     * @param applicationContext       the application context
     * @param url                      original url
     * @param hasAudio                 if true, file should be written as an mp4; mp4 is also always load from network
     * @param applicationId            application id for accessing the FilerProvider
     * @param destinationFile          where the local copy of the gif or mp4 will write to
     * @param onWriteCompletedListener callbacks for if the file was successfully or unsuccessfully created
     */
    public void getLocalUriForUrl(@NonNull final Context applicationContext,
                                  String url,
                                  boolean hasAudio,
                                  String applicationId,
                                  File destinationFile,
                                  @NonNull final OnWriteCompletedListener onWriteCompletedListener) {
        if (url == null) {
            return;
        }
        mMediaType = ContentFormatUtils.getExtension(hasAudio);
        cancelDownloads();

        if (destinationFile == null) {
            return;
        }

        if (hasAudio) {
            getFromNetwork(applicationContext, destinationFile, onWriteCompletedListener, url, applicationId);
        } else {
            getFromCache(applicationContext, destinationFile, onWriteCompletedListener, url, applicationId);
        }
    }

    /**
     * Get file from network
     * <p>
     * Content Provider Compatible (API 24+)
     */
    protected void getFromNetwork(@Nullable final Context applicationContext,
                                  @NonNull final File output,
                                  @NonNull final OnWriteCompletedListener listener,
                                  @NonNull final String url,
                                  @Nullable final String applicationId) {

        final IllegalArgumentException throwable = checkIllegalArguments(
                applicationContext, output, listener, url, applicationId);
        if (throwable != null) {
            throw throwable;
        }

        final DownloadStatusListener l = new AbstractDownloadStatusListener(listener) {
            @Override
            public void onDownloadComplete(int id, @NonNull OnWriteCompletedListener listener) {
                if (!MediaFormats.MP4.equals(mMediaType)) {
                    return;
                }

                listener.onWriteSucceeded(output.getPath());
            }
        };

        final DownloadRequest downloadRequest = new DownloadRequest(Uri.parse(url))
                .setDestinationURI(Uri.parse(output.getPath()))
                .setDownloadListener(l);

        mDownloadManager.add(downloadRequest);
    }

    private static IllegalArgumentException checkIllegalArguments(@Nullable final Context applicationContext,
                                                                  @NonNull final File output,
                                                                  @NonNull final OnWriteCompletedListener listener,
                                                                  @NonNull final String url,
                                                                  @Nullable final String applicationId) {
        if (TextUtils.isEmpty(url) || !URLUtil.isValidUrl(url)) {
            return new IllegalArgumentException("invalid url: " + url);
        }

        if (output == null) {
            return new IllegalArgumentException("output cannot be null");
        }

        if (listener == null) {
            return new IllegalArgumentException("listener cannot be null");
        }

        if (Build.VERSION.SDK_INT >= 24) {
            if (applicationContext == null) {
                return new IllegalArgumentException("context cannot be null");
            }

            if (TextUtils.isEmpty(applicationId)) {
                return new IllegalArgumentException("invalid application id: " + applicationId);
            }
        }
        return null;
    }

    protected void getFromCache(@Nullable final Context applicationContext,
                                @NonNull final File output,
                                @NonNull final OnWriteCompletedListener listener,
                                @NonNull final String url,
                                @Nullable final String applicationId) {

        final IllegalArgumentException throwable = checkIllegalArguments(
                applicationContext, output, listener, url, applicationId);
        if (throwable != null) {
            throw throwable;
        }

        final SimpleTarget<byte[]> target = new AbstractSimpleTarget(listener, url) {
            @Override
            public void onResourceReady(byte[] resource, GlideAnimation<? super byte[]> glideAnimation, @NonNull OnWriteCompletedListener listener) {
                try {
                    FileOutputStream outputStream = new FileOutputStream(output);
                    outputStream.write(resource);
                    outputStream.close();
                } catch (IOException e) {
                    onLoadFailed(e, null);
                    return;
                }

                if (MediaFormats.GIF.equals(mMediaType)) {
                    listener.onWriteSucceeded(output.getPath());
                }
            }
        };

        DrawableTypeRequest<String> request = Glide.with(applicationContext).load(url);
        request.diskCacheStrategy(DiskCacheStrategy.SOURCE);
        request.asGif()
                .toBytes()
                .into(target);
    }

    private abstract class AbstractDownloadStatusListener implements DownloadStatusListener {

        private final OnWriteCompletedListener mOnWriteCompletedListener;

        public AbstractDownloadStatusListener(@NonNull final OnWriteCompletedListener listener) {
            if (listener == null) {
                throw new IllegalArgumentException("OnWriteCompletedListener cannot be null");
            }
            mOnWriteCompletedListener = listener;
        }

        public abstract void onDownloadComplete(int id, @NonNull OnWriteCompletedListener listener);

        @Override
        public final void onDownloadComplete(int id) {
            onDownloadComplete(id, mOnWriteCompletedListener);
        }

        @Override
        public void onDownloadFailed(int id, int errorCode, String errorMessage) {
            final Throwable throwable;
            if (!TextUtils.isEmpty(errorMessage)) {
                throwable = new Throwable(errorMessage);
            } else {
                throwable = new Throwable("onDownloadFailed() with errorCode: " + errorCode);
            }
            mOnWriteCompletedListener.onWriteFailed(throwable);
        }

        @Override
        public void onProgress(int id, long totalBytes, long downloadedBytes, int progress) {
        }
    }

    private abstract class AbstractSimpleTarget extends SimpleTarget<byte[]> {
        private final OnWriteCompletedListener mOnWriteCompletedListener;
        private final String mUrl;

        public AbstractSimpleTarget(@NonNull final OnWriteCompletedListener listener,
                                    @NonNull final String url) {
            if (listener == null) {
                throw new IllegalArgumentException("OnWriteCompletedListener cannot be null");
            }
            mOnWriteCompletedListener = listener;
            mUrl = url;
        }

        public abstract void onResourceReady(byte[] resource, GlideAnimation<? super byte[]> glideAnimation,
                                             @NonNull OnWriteCompletedListener listener);

        @Override
        public final void onResourceReady(byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
            onResourceReady(resource, glideAnimation, mOnWriteCompletedListener);
        }

        @Override
        public void onLoadFailed(Exception e, Drawable errorDrawable) {
            mOnWriteCompletedListener.onWriteFailed(e != null ?
                    e : new Throwable(AbstractSimpleTarget.class.getName()
                    + " failed, unable to load url:" + mUrl));
        }
    }
}