package com.tenor.android.core.model;

import java.io.Serializable;

/**
 * The Interface of Gif
 */
public interface IGif extends Serializable {
    /**
     * Get the id of this gif
     */
    String getId();

    /**
     * Get the name of this gif
     */
    String getName();
}
