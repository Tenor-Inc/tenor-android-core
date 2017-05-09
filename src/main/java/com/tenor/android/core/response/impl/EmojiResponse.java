package com.tenor.android.core.response.impl;

import android.support.annotation.NonNull;

import com.tenor.android.core.model.impl.EmojiTag;
import com.tenor.android.core.response.AbstractResponse;
import com.tenor.android.core.util.AbstractListUtils;

import java.util.Collections;
import java.util.List;

/**
 * The emoji response
 */
public class EmojiResponse extends AbstractResponse {
    private static final long serialVersionUID = 5979803531976871364L;
    private List<EmojiTag> tags;

    @NonNull
    public List<EmojiTag> getTags() {
        return !AbstractListUtils.isEmpty(tags) ? tags : Collections.<EmojiTag>emptyList();
    }
}