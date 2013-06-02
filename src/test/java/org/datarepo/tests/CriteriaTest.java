package org.datarepo.tests;

import org.datarepo.reflection.FieldAccess;
import org.datarepo.reflection.Reflection;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.datarepo.query.Criteria.*;
import static org.datarepo.utils.LinearSearchWithFields.findWithFiltersAnd;
import static org.datarepo.utils.Utils.ls;
import static org.junit.Assert.assertEquals;

public class CriteriaTest {

    private List<TestClass> items;
    Map<String, FieldAccess> fields;

    class TestClass {
        int i;
        float f;
        String s;
        short st;

        TestClass(int i, float f, String s, short st) {
            this.i = i;
            this.f = f;
            this.s = s;
            this.st = st;
        }

    }

    @Before
    public void setUp() throws Exception {
        fields = Reflection.getPropertyFieldAccessMap(TestClass.class, true, true);

        items = ls(
                new TestClass(0, 0.1f, "a", (short) 1),
                new TestClass(1, 1.1f, "a", (short) 2),
                new TestClass(2, 2.1f, "a", (short) 3),
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
    public void testIn() throws Exception {
        List<TestClass> results = findWithFiltersAnd(items, fields, inInts("i", 5, 6, 7));
        assertEquals(3, results.size());
    }

    @Test
    public void testNotIn() throws Exception {
        List<TestClass> results = findWithFiltersAnd(items, fields, notInInts("i", 5, 6, 7));
        assertEquals(7, results.size());
    }

    @Test
    public void testBetween() throws Exception {
        List<TestClass> results = findWithFiltersAnd(items, fields, between("i", 5, 10));
        assertEquals(5, results.size());
    }

    @Test
    public void testNotEqual() throws Exception {
        List<TestClass> results = findWithFiltersAnd(items, fields, notEq("i", 5));
        assertEquals(9, results.size());
    }

    @Test
    public void testEqual() throws Exception {
        List<TestClass> results = findWithFiltersAnd(items, fields, eq("i", 5));
        assertEquals(1, results.size());
        assertEquals(5, results.get(0).i);
    }

    @Test
    public void testGTE() throws Exception {
        List<TestClass> results = findWithFiltersAnd(items, fields, gte("i", 5));
        assertEquals(5, results.size());
        assertEquals(5, results.get(0).i);
        assertEquals(6, results.get(1).i);
        assertEquals(7, results.get(2).i);
        assertEquals(8, results.get(3).i);
        assertEquals(9, results.get(4).i);
    }

    @Test
    public void testGT() throws Exception {
        List<TestClass> results = findWithFiltersAnd(items, fields, gt("i", 5));
        assertEquals(4, results.size());
        assertEquals(6, results.get(0).i);
        assertEquals(7, results.get(1).i);
        assertEquals(8, results.get(2).i);
        assertEquals(9, results.get(3).i);
    }

    @Test
    public void testLT() throws Exception {
        List<TestClass> results = findWithFiltersAnd(items, fields, lt("i", 5));
        assertEquals(5, results.size());
        assertEquals(0, results.get(0).i);
        assertEquals(4, results.get(4).i);
    }

    @Test
    public void testLTE() throws Exception {
        List<TestClass> results = findWithFiltersAnd(items, fields, lte("i", 5));
        assertEquals(6, results.size());
        assertEquals(0, results.get(0).i);
        assertEquals(5, results.get(5).i);
    }

}
