package com.tenor.android.core.response.impl;

import com.google.gson.annotations.SerializedName;
import com.tenor.android.core.response.AbstractResponse;

/**
 * The anon id response
 */
public class AnonIdResponse extends AbstractResponse {

    private static final long serialVersionUID = -1814022099833831972L;
    @SerializedName("anonid")
    private String id;
    @SerializedName("localid")
    private int localId;

    public String getId() {
        return id;
    }

    public int getLocalId() {
        return localId;
    }
}
