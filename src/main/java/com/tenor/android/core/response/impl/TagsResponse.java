package com.tenor.android.core.response.impl;

import android.support.annotation.NonNull;

import com.tenor.android.core.model.impl.Tag;
import com.tenor.android.core.response.AbstractResponse;
import com.tenor.android.core.util.AbstractListUtils;

import java.util.Collections;
import java.util.List;

/**
 * The response of tags
 */
public class TagsResponse extends AbstractResponse {

    private static final long serialVersionUID = -8215681889738059793L;
    private List<Tag> tags;

    @NonNull
    public List<Tag> getTags() {
        return !AbstractListUtils.isEmpty(tags) ? tags : Collections.<Tag>emptyList();
    }
}
