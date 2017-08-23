package com.tenor.android.core.loader;


import android.support.annotation.NonNull;

import java.io.Serializable;

public interface ITaskParams extends Serializable {

    /**
     * @return the unique id of the task
     */
    @NonNull
    String getId();
}
