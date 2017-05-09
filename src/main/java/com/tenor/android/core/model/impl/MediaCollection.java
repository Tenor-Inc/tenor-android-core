package com.tenor.android.core.model.impl;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * The response model of the media collection object, which contains different supported formats of a gif
 */
public class MediaCollection implements Serializable {
    private static final long serialVersionUID = -8824214919408809561L;
    private Media gif;
    private Media mp4;
    @SerializedName("loopedmp4")
    private Media loopedMp4;
    @SerializedName("tinygif")
    private Media tinyGif;

    private Media webm;
    @SerializedName("tinymp4")
    private Media tinyMp4;
    @SerializedName("thumbnail")
    private Media thumbNail;


    /**
     * @return Media of the standard GIF format
     */
    public Media getGif() {
        return gif;
    }

    /**
     * @return Media of the standard mp4 format
     */
    public Media getMp4() {
        return mp4;
    }

    /**
     * @return Media of an mp4 that contains 3 loops built in
     */
    public Media getLoopedMp4() {
        return loopedMp4;
    }

    /**
     * @return Media of a more compressed gif.  Good for quick loading and sending
     */
    public Media getTinyGif() {
        return tinyGif;
    }

    /**
     * @return Media of web player url
     */
    public Media getWebm() {
        return webm;
    }

    /**
     * @deprecated
     */
    public Media getThumbNail() {
        return thumbNail;
    }

    /**
     * @return Media of a more compressed mp4.  Good for quick loading and sending
     */
    public Media getTinyMp4() {
        return tinyMp4;
    }
}
