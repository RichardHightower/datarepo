package org.datarepo.tests;

import org.datarepo.Repo;
import org.datarepo.Repos;
import org.datarepo.tests.model.ClassForTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TypeTest {
    Repo<String, ClassForTest> repo;

    @Before
    public void setUp() throws Exception {
        repo = Repos.builder().primaryKey("strings")
                .build(String.class, ClassForTest.class);


    }

    @Test
    public void testName() throws Exception {
        ClassForTest forTest = new ClassForTest();
        String key = "AAAA";
        forTest.setStrings(key);
        repo.add(forTest);

        repo.modify(forTest, "ints", 1);
        int i = repo.getInt(forTest, "ints");
        assertEquals(1, i);

        repo.modify(forTest, "shorts", (short) 1);
        short s = repo.getShort(forTest, "shorts");
        assertEquals(1, s);

        repo.modify(forTest, "bytes", (byte) 1);
        byte b = repo.getByte(forTest, "bytes");
        assertEquals(1, b);

        repo.modify(forTest, "longs", 1l);
        long l = repo.getLong(forTest, "longs");
        assertEquals(1, l);


        repo.modify(forTest, "doubles", 1.0);
        double d = repo.getDouble(forTest, "doubles");
        assertEquals(1, d, 0.1d);

        repo.modify(forTest, "floats", 1.0f);
        float f = repo.getFloat(forTest, "floats");
        assertEquals(1, f, 0.1f);

        repo.modify(forTest, "chars", 'a');
        char c1 = repo.getChar(forTest, "chars");
        assertEquals('a', c1);


        repo.update(key, "ints", 2);
        i = repo.readInt(key, "ints");
        assertEquals(2, i);

        repo.update(key, "shorts", (short) 2);
        s = repo.readShort(key, "shorts");
        assertEquals(2, s);

        repo.update(key, "bytes", (byte) 2);
        b = repo.readByte(key, "bytes");
        assertEquals(2, b);

        repo.update(key, "longs", 2l);
        l = repo.readLong(key, "longs");
        assertEquals(2, l);


        repo.update(key, "doubles", 2.0);
        d = repo.readDouble(key, "doubles");
        assertEquals(2, d, 0.1d);

        repo.update(key, "floats", 2.0f);
        f = repo.readFloat(key, "floats");
        assertEquals(2, f, 0.1f);

        repo.update(key, "chars", 'b');
        c1 = repo.readChar(key, "chars");
        assertEquals('b', c1);

        //--------------

        repo.compareAndUpdate(key, "ints", 2, 3);
        i = repo.readInt(key, "ints");
        assertEquals(3, i);

        repo.compareAndUpdate(key, "shorts", (short) 2, (short) 3);
        s = repo.readShort(key, "shorts");
        assertEquals(3, s);

        repo.compareAndUpdate(key, "bytes", (byte) 2, (byte) 3);
        b = repo.readByte(key, "bytes");
        assertEquals(3, b);

        repo.compareAndUpdate(key, "longs", 2l, 3l);
        l = repo.readLong(key, "longs");
        assertEquals(3, l);


        repo.compareAndUpdate(key, "doubles", 2.0, 3.0);
        d = repo.readDouble(key, "doubles");
        assertEquals(3, d, 0.1d);

        repo.compareAndUpdate(key, "floats", 2.0f, 3.0f);
        f = repo.readFloat(key, "floats");
        assertEquals(3, f, 0.1f);

        repo.compareAndUpdate(key, "chars", 'b', 'c');
        c1 = repo.readChar(key, "chars");
        assertEquals('c', c1);

        //------------------

        repo.compareAndIncrement(key, "ints", 3);
        i = repo.readInt(key, "ints");
        assertEquals(4, i);

        repo.compareAndIncrement(key, "shorts", (short) 3);
        s = repo.readShort(key, "shorts");
        assertEquals(4, s);

        repo.compareAndIncrement(key, "bytes", (byte) 3);
        b = repo.readByte(key, "bytes");
        assertEquals(4, b);

        repo.compareAndIncrement(key, "longs", 3l);
        l = repo.readLong(key, "longs");
        assertEquals(4, l);


    }
}
