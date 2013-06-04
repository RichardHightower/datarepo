package org.datarepo.tests;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.datarepo.Collections.$q;
import static org.datarepo.Collections.query;
import static org.datarepo.query.Criteria.between;
import static org.datarepo.utils.Utils.ls;
import static org.junit.Assert.assertEquals;

public class MoreTests {

    List<Employee> list;

    @Before
    public void setUp() throws Exception {
        list = ls(
                Employee.employee("a", "b", "123", "5.29.1970", 100),
                Employee.employee("b", "b", "124", "5.29.1960", 200)
        );

    }

    @Test
    public void test() throws Exception {
        List<Employee> queryableList = $q(list);
        List<Employee> results = query(queryableList, between("birthDate", "5/29/1950", "5/29/1990"));
        assertEquals("b", results.get(0).getFirstName());
        assertEquals("a", results.get(1).getFirstName());

    }
}
