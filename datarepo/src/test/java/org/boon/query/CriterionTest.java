package org.boon.query;

import org.junit.Before;
import org.junit.Test;

import static org.boon.Exceptions.die;


public class CriterionTest {

    @Before
    public void setUp() throws Exception {


    }

    @Test
    public void test() throws Exception {
        Query query = QueryFactory.and(QueryFactory.eq("foo", "bar"));
        query.toString();

        Query query1 = QueryFactory.and(QueryFactory.eq("foo", "bar"));

        boolean ok = true;
        ok &= (query.equals(query1)) || die("Die") ;


        Criterion c1 = QueryFactory.eq("foo", "bar");
        Criterion c2 = QueryFactory.eq("foo", "bar");

        ok &= (c1.equals(c2)) || die("Die") ;

    }
}
