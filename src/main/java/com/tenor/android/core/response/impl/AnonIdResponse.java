package com.tenor.android.core.response.impl;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.tenor.android.core.constant.StringConstant;
import com.tenor.android.core.response.AbstractResponse;

/**
 * The response of anon id
 */
public class AnonIdResponse extends AbstractResponse {

    private static final long serialVersionUID = -1814022099833831972L;
    @SerializedName(value = "anonid", alternate = {"anon_id"})
    private String id;
    @SerializedName("localid")
    private int localId;

    @NonNull
    public String getId() {
        return StringConstant.getOrEmpty(id);
    }

    public int getLocalId() {
        return localId;
    }
}
