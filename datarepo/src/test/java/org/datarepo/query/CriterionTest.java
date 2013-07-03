package org.datarepo.query;

import org.junit.Before;
import org.junit.Test;

import static org.datarepo.utils.Utils.assertTrue;

public class CriterionTest {

    @Before
    public void setUp() throws Exception {


    }

    @Test
    public void test() throws Exception {
        Query query = QueryFactory.and(QueryFactory.eq("foo", "bar"));
        query.toString();

        Query query1 = QueryFactory.and(QueryFactory.eq("foo", "bar"));

        assertTrue(query.equals(query1));


        Criterion c1 = QueryFactory.eq("foo", "bar");
        Criterion c2 = QueryFactory.eq("foo", "bar");

        assertTrue(c1.equals(c2));

    }
}
