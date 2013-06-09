package org.datarepo.impl.indexes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.datarepo.utils.Utils.ls;
import static org.datarepo.utils.Utils.print;
import static org.junit.Assert.assertEquals;

public class MultiValueTest {

    private MultiValue mv;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testAdd() throws Exception {

        MultiValue mv = MultiValue.add(null, "Rick");
        assertEquals("Rick", mv.getValue());

    }

    @Test
    public void testMany() throws Exception {

        ls("Rick", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13")
                .forEach((item) -> {
                    mv = MultiValue.add(mv, item)
                    ;
                });

        assertEquals("Rick", mv.getValue());
        assertEquals(13, mv.size());

        mv.getValues().forEach((item) -> {
            assertNotNull(item);
            print(item);
        });


    }

    @Test
    public void testManyThenAddTo() throws Exception {

        ls("Rick", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13")
                .forEach((item) -> {
                    mv = MultiValue.add(mv, item)
                    ;
                });

        List<String> results = ls("Rick", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13");

        mv.addTo(results);

        results.forEach((item) -> {
            assertNotNull(item);
            //print(item);
        });


    }

    @Test
    public void testManyUseAddTo() throws Exception {

        MultiValue mv = MultiValue.add(null, "Rick");

        assertEquals("Rick", mv.getValue());

        List<String> results = ls("Rick", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13");

        mv.addTo(results);

        results.forEach((item) -> {
            assertNotNull(item);
            //print(item);
        });

        mv.getValues().forEach((item) -> {
            assertNotNull(item);
            //print(item);
        });

        results.forEach((item) -> {
            assertNotNull(item);
            //print(item);
        });

    }

}
