package com.tenor.android.core.util;

import com.tenor.android.core.model.ICollection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AbstractListUtilsUT {

    @Test
    public void test_splits_size_equal_to_limit() throws Exception {

        int limit = 10;
        List<String> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add("test " + i);
        }

        List<List<String>> results = AbstractListUtils.splits(list, limit);

        // size check
        assertEquals(1, results.size());

        // first of the first column
        assertEquals("test 0", results.get(0).get(0));

        // last of the first column
        assertEquals("test 9", results.get(0).get(limit - 1));
    }

    @Test
    public void test_splits_size_larger_than_limit() throws Exception {

        int limit = 10;
        List<String> list = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            list.add("test " + i);
        }

        List<List<String>> results = AbstractListUtils.splits(list, limit);

        // size check
        assertEquals(5, results.size());

        // first of the first column
        assertEquals("test 0", results.get(0).get(0));

        // last of the first column
        assertEquals("test 9", results.get(0).get(limit - 1));

        // first of the second column
        assertEquals("test 10", results.get(1).get(0));

        // last of the second column
        assertEquals("test 19", results.get(1).get(limit - 1));

        // last of the fifth column
        assertEquals("test 49", results.get(4).get(limit - 1));
    }

    @Test
    public void test_splits_size_less_than_limit() throws Exception {

        int limit = 10;
        List<String> list = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            list.add("test " + i);
        }

        List<List<String>> results = AbstractListUtils.splits(list, limit);

        // size check
        assertEquals(1, results.size());

        // first of the first column
        assertEquals("test 0", results.get(0).get(0));

        // last of the first column
        assertEquals("test 7", results.get(0).get(results.get(0).size() - 1));
    }

    @Test
    public void test_splits_size_equal_to_0() throws Exception {

        int limit = 0;
        List<String> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add("test " + i);
        }

        List<List<String>> results = AbstractListUtils.splits(list, limit);

        // size check
        assertEquals(1, results.size());

        // first of the first column
        assertEquals("test 0", results.get(0).get(0));

        // last of the first column
        assertEquals("test 9", results.get(0).get(results.get(0).size() - 1));
    }

    @Test
    public void test_splits_size_equal_to_1() throws Exception {

        int limit = 1;
        List<String> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add("test " + i);
        }

        List<List<String>> results = AbstractListUtils.splits(list, limit);

        // size check
        assertEquals(1, results.size());

        // first of the first column
        assertEquals("test 0", results.get(0).get(0));

        // last of the first column
        assertEquals("test 9", results.get(0).get(results.get(0).size() - 1));
    }

    @Test
    public void test_splits_size_equal_to_negative_1() throws Exception {

        int limit = -1;
        List<String> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add("test " + i);
        }

        List<List<String>> results = AbstractListUtils.splits(list, limit);

        // size check
        assertEquals(1, results.size());

        // first of the first column
        assertEquals("test 0", results.get(0).get(0));

        // last of the first column
        assertEquals("test 9", results.get(0).get(results.get(0).size() - 1));
    }

    @Test
    public void test_splits_size_empty() throws Exception {

        // Variant 1
        int limit = 10;
        List<String> list = new ArrayList<>();

        List<List<String>> results = AbstractListUtils.splits(list, limit);

        // size check
        assertEquals(0, results.size());


        // Variant 2
        limit = -1;
        results = AbstractListUtils.splits(list, limit);

        // size check
        assertEquals(0, results.size());
    }

    @Test
    public void test_splits_size_null() throws Exception {

        // Variant 1
        int limit = 10;

        List<List<String>> results = AbstractListUtils.splits(null, limit);

        // size check
        assertEquals(0, results.size());


        // Variant 2
        limit = -1;
        results = AbstractListUtils.splits(null, limit);

        // size check
        assertEquals(0, results.size());
    }

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