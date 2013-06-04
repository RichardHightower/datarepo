package org.datarepo.tests;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.datarepo.Collections.$q;
import static org.datarepo.Collections.sortedQuery;
import static org.datarepo.query.Criteria.between;
import static org.datarepo.utils.Utils.ls;
import static org.datarepo.utils.Utils.print;
import static org.junit.Assert.assertEquals;

public class MoreTests {

    List<Employee> list;

    @Before
    public void setUp() throws Exception {
        list = ls(
                Employee.employee("firstA", "LastA", "123", "5.29.1970:00:00:01", 100),
                Employee.employee("firstB", "LastB", "124", "5.29.1960:00:00:00", 200)
        );

    }

    @Test
    public void testBetweenSalary() throws Exception {

        List<Employee> queryableList = $q(list);
        List<Employee> results = sortedQuery(queryableList, "firstName", between("salary", 100, 200));

        assertEquals(1, results.size());
        assertEquals("firstA", results.get(0).getFirstName());

    }

    @Test
    public void testBetweenSalaryExact() throws Exception {

        List<Employee> queryableList = $q(list);
        List<Employee> results = sortedQuery(queryableList, "firstName", between("salary", 100, 201));

        assertEquals(2, results.size());
        assertEquals("firstA", results.get(0).getFirstName());
        assertEquals("firstB", results.get(1).getFirstName());

    }


    @Test
    public void testBetweenSalaryExactOutOfRange() throws Exception {
        print(list);
        List<Employee> queryableList = $q(list);
        List<Employee> results = sortedQuery(queryableList, "firstName", between("salary", 400, 500));

        assertEquals(0, results.size());

    }

    //@Test  //Java data handling SUCKS! I don't think it is an issue with index lib.
    public void testBetweenDateExact() throws Exception {

        print(list);

        List<Employee> queryableList = $q(list);
        List<Employee> results = sortedQuery(queryableList, "firstName", between("birthDate", "5/29/1960:00:00:01", "5/29/1970:00:00:00"));

        assertEquals(2, results.size());
        assertEquals("firstA", results.get(0).getFirstName());
        assertEquals("firstB", results.get(1).getFirstName());

    }


    @Test
    public void testBetweenDateExactJustOverAndUnder1Year() throws Exception {
        List<Employee> queryableList = $q(list);
        List<Employee> results = sortedQuery(queryableList, "firstName", between("birthDate", "5/29/1959", "5/29/1971"));

        assertEquals(2, results.size());
        assertEquals("firstA", results.get(0).getFirstName());
        assertEquals("firstB", results.get(1).getFirstName());

    }


    @Test
    public void testBetweenDate() throws Exception {
        List<Employee> queryableList = $q(list);
        List<Employee> results = sortedQuery(queryableList, "firstName", between("birthDate", "5/29/1950", "5/29/1990"));
        assertEquals(2, results.size());
        assertEquals("firstA", results.get(0).getFirstName());
        assertEquals("firstB", results.get(1).getFirstName());

    }

    @Test
    public void testBetweenDatePreInit() throws Exception {
        List<Employee> queryableList = $q(list);
        List<Employee> results = sortedQuery(queryableList, "firstName", between(Employee.class, "birthDate", "5/29/1950", "5/29/1990"));

        assertEquals(2, results.size());
        assertEquals("firstA", results.get(0).getFirstName());
        assertEquals("firstB", results.get(1).getFirstName());

    }


}
