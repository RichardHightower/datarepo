package org.datarepo.tests;

import junit.framework.Assert;
import org.datarepo.fields.FieldAccess;
import org.datarepo.utils.Reflection;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.datarepo.query.QueryFactory.*;
import static org.datarepo.utils.Utils.ls;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CriteriaTest {

    private List<TestClass> items;
    Map<String, FieldAccess> fields;

    class TestClass {
        int i;
        float f;
        String s;
        short st;
        List items;

        TestClass(int i, float f, String s, short st, String... items) {
            this.i = i;
            this.f = f;
            this.s = s;
            this.st = st;
            this.items = ls(items);
        }

    }

    @Before
    public void setUp() throws Exception {
        fields = Reflection.getPropertyFieldAccessMap(TestClass.class, true, true);

        items = ls(
                new TestClass(0, 0.1f, "a", (short) 1, "dog", "cat", "girl"),
                new TestClass(1, 1.1f, "a dog chased a cat today", (short) 2),
                new TestClass(2, 2.1f, null, (short) 3),
                new TestClass(3, 3.1f, "a", (short) 4),
                new TestClass(4, 4.1f, "a", (short) 5),
                new TestClass(5, 5.1f, "a", (short) 6),
                new TestClass(6, 6.1f, "a", (short) 7),
                new TestClass(7, 7.1f, "a", (short) 8),
                new TestClass(8, 8.1f, "a", (short) 9),
                new TestClass(9, 9.1f, "a", (short) 10)
        );


    }

    @Test
    public void testIsNull() throws Exception {
        TestClass tc = items.get(2);
        assertTrue(test(tc, isNull("s")));
        Assert.assertFalse(test(tc, notNull("s")));

    }


    @Test
    public void tesNotNull() throws Exception {
        TestClass tc = items.get(3);
        assertTrue(test(tc, notNull("s")));
        Assert.assertFalse(test(tc, isNull("s")));

    }


    @Test
    public void testContains() throws Exception {
        TestClass tc = items.get(0);
        assertTrue(test(tc, contains("items", "cat")));
        Assert.assertFalse(test(tc, contains("items", "Mountain Lion")));

    }

    @Test
    public void testContainsStr() throws Exception {
        TestClass tc = items.get(1);
        assertTrue(test(tc, contains("s", "cat")));
        Assert.assertFalse(test(tc, contains("s", "Mountain Lion")));
    }

    @Test
    public void testNotContains() throws Exception {
        TestClass tc = items.get(0);
        Assert.assertFalse(test(tc, notContains("items", "cat")));
        assertTrue(test(tc, notContains("items", "Mountain Lion")));

    }


    @Test
    public void testNotEmpty() throws Exception {
        TestClass tc = items.get(0);
        assertTrue(test(tc, notEmpty("items")));

    }


    @Test
    public void testEmpty() throws Exception {
        TestClass tc = items.get(9);
        assertTrue(test(tc, empty("items")));

    }

    @Test
    public void testNotContainsStr() throws Exception {
        TestClass tc = items.get(1);
        Assert.assertFalse(test(tc, notContains("s", "cat")));
        assertTrue(test(tc, notContains("s", "Mountain Lion")));
    }

    @Test
    public void testIn() throws Exception {
        List<TestClass> results = filter(items, in("i", 5, 6, 7));
        assertEquals(3, results.size());
    }

    @Test
    public void testNotIn() throws Exception {
        List<TestClass> results = filter(items, notIn("i", 5, 6, 7));
        assertEquals(7, results.size());
    }

    @Test
    public void testNotInUsingNot() throws Exception {
        List<TestClass> results = filter(items, not(in("i", 5, 6, 7)));
        assertEquals(7, results.size());
    }

    @Test
    public void testBetween() throws Exception {
        List<TestClass> results = filter(items, between("i", 5, 10));
        assertEquals(5, results.size());
    }

    @Test
    public void testNotEqual() throws Exception {
        List<TestClass> results = filter(items, notEq("i", 5));
        assertEquals(9, results.size());
    }

    @Test
    public void testEqual() throws Exception {
        List<TestClass> results = filter(items, eq("i", 5));
        assertEquals(1, results.size());
        assertEquals(5, results.get(0).i);
    }

    @Test
    public void testGTE() throws Exception {
        List<TestClass> results = filter(items, gte("i", 5));
        assertEquals(5, results.size());
        assertEquals(5, results.get(0).i);
        assertEquals(6, results.get(1).i);
        assertEquals(7, results.get(2).i);
        assertEquals(8, results.get(3).i);
        assertEquals(9, results.get(4).i);
    }

    @Test
    public void testGT() throws Exception {
        List<TestClass> results = filter(items, gt("i", 5));
        assertEquals(4, results.size());
        assertEquals(6, results.get(0).i);
        assertEquals(7, results.get(1).i);
        assertEquals(8, results.get(2).i);
        assertEquals(9, results.get(3).i);
    }

    @Test
    public void testLT() throws Exception {
        List<TestClass> results = filter(items, lt("i", 5));
        assertEquals(5, results.size());
        assertEquals(0, results.get(0).i);
        assertEquals(4, results.get(4).i);
    }

    @Test
    public void testLTE() throws Exception {
        List<TestClass> results = filter(items, lte("i", 5));
        assertEquals(6, results.size());
        assertEquals(0, results.get(0).i);
        assertEquals(5, results.get(5).i);
    }

}
