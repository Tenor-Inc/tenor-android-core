package com.tenor.android.core.response.impl;

import android.support.annotation.NonNull;

import com.tenor.android.core.constant.StringConstant;

public class PackResponse extends GifsResponse {

    private static final long serialVersionUID = -6425188470381821462L;

    private String id;
    private String name;

    @NonNull
    public String getId() {
        return StringConstant.getOrEmpty(id);
    }

    @NonNull
    public String getName() {
        return StringConstant.getOrEmpty(name);
    }
}