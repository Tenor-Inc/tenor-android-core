package com.tenor.android.core.util;

import com.tenor.android.core.model.ICollection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AbstractListUtilsUT {

    @Test
    public void test_xor_join() throws Exception {
        List<ICollection> base = new ArrayList<>();
        List<ICollection> news = new ArrayList<>();

        base.add(new ICollection() {
            @Override
            public boolean isCollection() {
                return false;
            }

            @Override
            public boolean isCollectionTag() {
                return false;
            }

            @Override
            public String getName() {
                return "A";
            }
        });

        base.add(new ICollection() {
            @Override
            public boolean isCollection() {
                return false;
            }

            @Override
            public boolean isCollectionTag() {
                return false;
            }

            @Override
            public String getName() {
                return "B";
            }
        });

        news.add(new ICollection() {
            @Override
            public boolean isCollection() {
                return false;
            }

            @Override
            public boolean isCollectionTag() {
                return false;
            }

            @Override
            public String getName() {
                return "B";
            }
        });

        news.add(new ICollection() {
            @Override
            public boolean isCollection() {
                return false;
            }

            @Override
            public boolean isCollectionTag() {
                return false;
            }

            @Override
            public String getName() {
                return "C";
            }
        });


        AbstractListUtils.orJoinCollection(base, news);
        assertEquals(3, base.size());
        assertEquals("A", base.get(0).getName());
        assertEquals("B", base.get(1).getName());
        assertEquals("C", base.get(2).getName());
    }
}