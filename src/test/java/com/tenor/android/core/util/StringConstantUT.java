package com.tenor.android.core.util;

import com.tenor.android.core.constant.StringConstant;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StringConstantUT {

    @Test
    public void test_constants() throws Exception {
        assertEquals(StringConstant.EMPTY, "");
        assertEquals(StringConstant.SPACE, " ");
        assertEquals(StringConstant.HASH, "#");
        assertEquals(StringConstant.COMMA, ",");
        assertEquals(StringConstant.DOT, ".");
    }

    @Test
    public void test_join() throws Exception {
        List<String> list = new ArrayList<>();
        String str;

        str = StringConstant.join(null, ",");
        assertEquals(str, "");

        str = StringConstant.join(list, null);
        assertEquals(str, "");

        str = StringConstant.join(list, ",");
        assertEquals(str, "");

        list.clear();
        for (int i = 0; i < 1; i++) {
            list.add(String.valueOf(i));
        }
        str = StringConstant.join(list, ",");
        assertEquals(str, "0");

        str = StringConstant.join(list, "");
        assertEquals(str, "0");

        str = StringConstant.join(list, "\n");
        assertEquals(str, "0");


        list.clear();
        for (int i = 0; i < 3; i++) {
            list.add(String.valueOf(i));
        }
        str = StringConstant.join(list, ",");
        assertEquals(str, "0,1,2");

        str = StringConstant.join(list, "");
        assertEquals(str, "012");

        str = StringConstant.join(list, "\n");
        assertEquals(str, "0\n1\n2");
    }
}