package com.tenor.android.core.model.impl;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.response.AbstractResponse;

import java.io.Serializable;

/**
 * The response model of gif user or uploader
 */
public class GifPoster implements Serializable {

    private static final long serialVersionUID = 6888455363007568130L;
    private static final String TYPE_USER = "user";

    private String username;
    private String usertype;

    // TODO: communicate with backend to add this field in the future
    private String usericonurl;

    @NonNull
    public String getUsername() {
        return !TextUtils.isEmpty(username) ? username : StringConstant.EMPTY;
    }

    public boolean isUser() {
        return TYPE_USER.equals(usertype);
    }

    public String getUserIconUrl() {
        return !TextUtils.isEmpty(usericonurl) ? usericonurl : StringConstant.EMPTY;
    }

    public boolean hasUserIcon() {
        return !TextUtils.isEmpty(usericonurl);
    }
}
