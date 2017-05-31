package com.tenor.android.core.network.impl;

import android.content.Context;

import org.junit.Test;

import static org.junit.Assert.*;

public class NetworkStatusTest {

    @Test
    public void test_network_status_changed() throws Exception {
        assertFalse(new NetworkStatus().isStatusChanged(new NetworkStatus((Context) null)));
        assertFalse(new NetworkStatus().isStatusChanged(new NetworkStatus(false)));
        assertTrue(new NetworkStatus().isStatusChanged(new NetworkStatus(true)));
    }

}