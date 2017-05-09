package com.tenor.android.core.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tenor.android.core.constant.SupportMessengers;
import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.model.IGif;
import com.tenor.android.core.model.impl.Media;
import com.tenor.android.core.model.impl.MediaCollection;
import com.tenor.android.core.model.impl.Result;
import com.tenor.android.core.model.impl.Tag;

public abstract class AbstractGifUtils {

    /**
     * Maximum video time limit
     */
    protected static final double VIDEO_LOOP_CAP = 5.0;
    public static final String MEDIA_TINY_GIF = "MEDIA_TINY_GIF";
    public static final String MEDIA_GIF = "MEDIA_GIF";
    public static final String MEDIA_MP4 = "MEDIA_MP4";

    /**
     * Get name of a given {@link IGif} object
     *
     * @param gif the given {@link IGif}
     * @return the name
     */
    public static String getGifName(@Nullable final IGif gif) {
        if (!(gif instanceof Tag)) {
            return StringConstant.EMPTY;
        }
        return gif.getName();
    }

    /**
     * Get tag search term of a given {@link IGif} object
     *
     * @param gif the given {@link IGif}
     * @return the tag search term
     */
    public static String getTagSearchterm(@Nullable final IGif gif) {
        if (!(gif instanceof Tag)) {
            return StringConstant.EMPTY;
        }
        return ((Tag) gif).getSearchTerm();
    }

    /**
     * Get mp4 url of a given {@link IGif} object
     *
     * @param gif the given {@link IGif}
     * @return the mp4 url
     */
    public static String getMp4Url(@NonNull IGif gif) {
        return getMediaUrl(gif, MEDIA_MP4);
    }

    /**
     * Get media looped mp4 url of a given {@link IGif} object
     *
     * @param gif the given {@link IGif}
     * @return the looped mp4 url
     */
    public static String getLoopedMp4Url(@Nullable final IGif gif) {
        if (!(gif instanceof Result)) {
            return StringConstant.EMPTY;
        }

        final Result result = (Result) gif;
        if (AbstractListUtils.isEmpty(result.getMedias())
                || result.getMedias().get(0) == null
                || result.getMedias().get(0).getMp4() == null) {
            return StringConstant.EMPTY;
        }

        final MediaCollection mediaCollection = result.getMedias().get(0);
        if (mediaCollection.getMp4() != null
                && mediaCollection.getMp4().getDuration() > VIDEO_LOOP_CAP) {
            return getMp4Url(result);
        } else if (mediaCollection.getLoopedMp4() == null) {
            return getMp4Url(result);
        } else if (!TextUtils.isEmpty(mediaCollection.getLoopedMp4().getUrl())) {
            return mediaCollection.getLoopedMp4().getUrl();
        } else {
            return StringConstant.EMPTY;
        }
    }

    /**
     * Get media gif url of a given {@link IGif} object
     *
     * @param gif the given {@link IGif}
     * @return the media gif url
     */
    public static String getGifUrl(@NonNull IGif gif) {
        return getMediaUrl(gif, MEDIA_GIF);
    }

    /**
     * Get media tiny gif url of a given {@link IGif} object
     *
     * @param gif the given {@link IGif}
     * @return the media tiny gif url
     */
    public static String getTinyGifUrl(@NonNull IGif gif) {
        return getMediaUrl(gif, MEDIA_TINY_GIF);
    }

    /**
     * Get url of a given {@link Media}
     */
    private static String getMediaUrl(@Nullable final Media media) {
        if (media == null || TextUtils.isEmpty(media.getUrl())) {
            return StringConstant.EMPTY;
        }
        return media.getUrl();
    }

    /**
     * Get url of a given {@link Result} on the type of interest
     *
     * @param result the given {@link Result}
     * @param type   the type of interest {MEDIA_TINY_GIF | MEDIA_GIF | MEDIA_MP4}
     * @return the {@link Media}
     */
    @Nullable
    public static Media getMedia(@Nullable final Result result, @Nullable final String type) {
        if (result == null || AbstractListUtils.isEmpty(result.getMedias())
                || result.getMedias().get(0) == null || TextUtils.isEmpty(type)) {
            return null;
        }

        final MediaCollection mediaCollection = result.getMedias().get(0);
        switch (type) {
            case MEDIA_TINY_GIF:
                return mediaCollection.getTinyGif();
            case MEDIA_GIF:
                return mediaCollection.getGif();
            case MEDIA_MP4:
                return mediaCollection.getMp4();
            default:
                return null;
        }
    }

    /**
     * Get url of a given {@link Result} on the type of interest
     *
     * @param result the given {@link Result}
     * @param type   the type of interest {MEDIA_TINY_GIF | MEDIA_GIF | MEDIA_MP4}
     */
    private static String getMediaUrl(@Nullable final Result result, @Nullable final String type) {
        return getMediaUrl(getMedia(result, type));
    }

    /**
     * Get url of a given {@link Result}
     *
     * @param result the given {@link Result}
     */
    private static String getUrl(@Nullable final Result result) {
        if (result == null || TextUtils.isEmpty(result.getUrl())) {
            return StringConstant.EMPTY;
        }
        return result.getUrl();
    }

    /**
     * Get url of a given {@link Tag}
     *
     * @param tag the given {@link Tag}
     * @return the image url of {@link Tag}
     */
    private static String getUrl(@Nullable final Tag tag) {
        //noinspection ConstantConditions
        if (tag == null || TextUtils.isEmpty(tag.getImage())) {
            return StringConstant.EMPTY;
        }
        return tag.getImage();
    }

    /**
     * Get media url of a given {@link IGif} on the type of interest
     *
     * @param gif  the given {@link IGif}
     * @param type the type of interest {MEDIA_TINY_GIF | MEDIA_GIF | MEDIA_MP4}
     * @return the url
     */
    private static String getMediaUrl(@Nullable final IGif gif, @Nullable final String type) {
        if (gif == null || TextUtils.isEmpty(type)) {
            return StringConstant.EMPTY;
        }

        if (gif instanceof Result) {
            return getMediaUrl((Result) gif, type);
        } else if (gif instanceof Tag) {
            return getUrl((Tag) gif);
        } else {
            return StringConstant.EMPTY;
        }
    }

    /**
     * Get url of a given {@link IGif} on the type of interest
     *
     * @param gif the given {@link IGif}
     * @return the url
     */
    public static String getShareGifUrl(@Nullable final IGif gif) {
        if (gif == null) {
            return StringConstant.EMPTY;
        }

        if (gif instanceof Result) {
            return getUrl((Result) gif);
        } else if (gif instanceof Tag) {
            return getUrl((Tag) gif);
        } else {
            return StringConstant.EMPTY;
        }
    }

    /**
     * Get media url of a given {@link IGif} object for sending/sharing
     *
     * @param packageName the package name
     * @param result      the given {@link Result}
     * @return the media url for sending/sharing
     */
    public static String getSharedMediaUrl(@Nullable final String packageName, @Nullable final Result result) {

        if (TextUtils.isEmpty(packageName) || result == null) {
            return StringConstant.EMPTY;
        }

        switch (packageName) {
            case SupportMessengers.WHATSAPP:
                return result.isHasAudio() ? getLoopedMp4Url(result) : getGifUrl(result);
            case SupportMessengers.EIGHT_SMS:
            case SupportMessengers.WE_CHAT:
            case SupportMessengers.HIKE:
            case SupportMessengers.KIK:
            case SupportMessengers.VIBER:
                return getMp4Url(result);
            default:
                if (!isMP4Supported(packageName)) {
                    return getTinyGifUrl(result);
                } else {
                    return result.isHasAudio() ? getMp4Url(result) : getTinyGifUrl(result);
                }
        }
    }

    /**
     * Get media url of a given {@link IGif} object for sending/sharing
     * {@param #useMp4} controls which preferred link should be returned for default messengers
     *
     * @param packageName   the package name of current messenger
     * @param result        the given {@link Result}
     * @param useMp4        whether an mp4 link should be returned instead of a gif
     * @return
     */
    public static String getSharedMediaUrl(@NonNull String packageName, @NonNull Result result, boolean useMp4) {

        if (TextUtils.isEmpty(packageName) || result == null) {
            return StringConstant.EMPTY;
        }

        switch (packageName) {
            case SupportMessengers.WHATSAPP:
                return result.isHasAudio() ? getLoopedMp4Url(result) : getGifUrl(result);
            case SupportMessengers.EIGHT_SMS:
            case SupportMessengers.WE_CHAT:
            case SupportMessengers.HIKE:
            case SupportMessengers.KIK:
            case SupportMessengers.VIBER:
            case SupportMessengers.VODAFONE:
            case SupportMessengers.TANGO:
                return getMp4Url(result);
            case SupportMessengers.LINE:
                return getGifUrl(result);
            default:
                if (!isMP4Supported(packageName) || !useMp4) {
                    return AbstractGifUtils.getTinyGifUrl(result);
                } else {
                    return AbstractGifUtils.getMp4Url(result);
                }
        }
    }

    /**
     * Checks if a given app supports raw mp4s
     *
     * @param packageName package name of the given app
     * @return true if raw mp4s can be sent in these apps, false otherwise
     */
    private static boolean isMP4Supported(@NonNull String packageName) {
        return !TextUtils.isEmpty(packageName)
                && !SupportMessengers.CHOMP.equals(packageName)
                && !SupportMessengers.HANGOUTS.equals(packageName)
                && !SupportMessengers.TWITTER.equals(packageName);
    }

    /**
     * Get tiny gif static preview image url of a given {@link IGif} object
     *
     * @param gif the given {@link IGif}
     * @return the tiny gif url
     */
    public static String getTinyGifPreviewUrl(@NonNull IGif gif) {
        //noinspection ConstantConditions
        if (gif == null) {
            return StringConstant.EMPTY;
        }

        if (gif instanceof Result) {
            final Result result = (Result) gif;
            if (AbstractListUtils.isEmpty(result.getMedias())
                    || result.getMedias().get(0) == null
                    || result.getMedias().get(0).getTinyGif() == null
                    || TextUtils.isEmpty(result.getMedias().get(0).getTinyGif().getPreviewUrl())) {
                return StringConstant.EMPTY;
            }
            return result.getMedias().get(0).getTinyGif().getPreviewUrl();
        } else if (gif instanceof Tag) {
            return getUrl((Tag) gif);
        } else {
            return StringConstant.EMPTY;
        }
    }

    /**
     * Get gif static preview image url of a given {@link IGif} object
     *
     * @param gif the given {@link IGif}
     * @return the tiny gif url
     */
    public static String getGifPreviewUrl(@NonNull IGif gif) {
        //noinspection ConstantConditions
        if (gif == null) {
            return StringConstant.EMPTY;
        }

        if (gif instanceof Result) {
            final Result result = (Result) gif;
            if (result.getMedias() == null
                    || result.getMedias().get(0) == null
                    || result.getMedias().get(0).getTinyGif() == null
                    || TextUtils.isEmpty(result.getMedias().get(0).getTinyGif().getPreviewUrl())) {
                return StringConstant.EMPTY;
            }
            return result.getMedias().get(0).getTinyGif().getPreviewUrl();
        } else if (gif instanceof Tag) {
            return getUrl((Tag) gif);
        } else {
            return StringConstant.EMPTY;
        }
    }
}
