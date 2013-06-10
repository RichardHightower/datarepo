package org.datarepo.tests;

import org.datarepo.tests.model.Employee;
import org.datarepo.tests.model.SalesEmployee;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.datarepo.Collections.$q;
import static org.datarepo.Collections.sortedQuery;
import static org.datarepo.query.Criteria.*;
import static org.datarepo.utils.Reflection.copy;
import static org.datarepo.utils.Types.toDate;
import static org.datarepo.utils.Utils.ls;
import static org.datarepo.utils.Utils.print;
import static org.junit.Assert.assertEquals;

public class MoreTests {

    List<Employee> list;
    List<Employee> bigList;

    List<Employee> h_list;


    @Before
    public void setUp() throws Exception {
        list = ls(
                Employee.employee("firstA", "LastA", "123", "5.29.1970:00:00:01", 100),
                Employee.employee("firstB", "LastB", "124", "5.29.1960:00:00:00", 200)
        );

        h_list = ls(
                Employee.employee("firstA", "LastA", "123", "5.29.1970:00:00:01", 100),
                Employee.employee("firstB", "LastB", "124", "5.29.1960:00:00:00", 200),
                Employee.employee("firstZ", "LastB", "125", "5.29.1960:00:00:00", 200, true)

        );

        bigList = copy(list);

        for (int index = 0; index < 2000; index++) {

            String dateString = "5.29.1970:00:00:01";
            if (index % 2 == 0) {
                dateString = "5.29.1950:00:00:01";

            } else {
                dateString = "5.29.1990:00:00:01";

            }
            bigList.add(Employee.employee("firstC" + index, "last" + index, "ssn" + index, dateString, 1000 + index));
        }

    }

    @Test
    public void typeOfTest() throws Exception {
        List<Employee> queryableList = $q(h_list);
        List<Employee> results = sortedQuery(queryableList, "firstName", typeOf("SalesEmployee"));
        assertEquals(1, results.size());
        assertEquals("SalesEmployee", results.get(0).getClass().getSimpleName());

    }

    @Test
    public void instanceOfTest() throws Exception {
        List<Employee> queryableList = $q(h_list);
        List<Employee> results = sortedQuery(queryableList, "firstName", instanceOf(SalesEmployee.class));
        assertEquals(1, results.size());
        assertEquals("SalesEmployee", results.get(0).getClass().getSimpleName());
    }

    @Test
    public void superClassTest() throws Exception {
        List<Employee> queryableList = $q(h_list);
        List<Employee> results = sortedQuery(queryableList, "firstName", typeOf("ZSalaryEmployee"));
        assertEquals(0, results.size());

    }

    @Test
    public void testBetweenSalary_AND_LOTS_OF_TERMS_BIG_LIST() throws Exception {
        List<Employee> queryableList = $q(bigList);
        List<Employee> results = sortedQuery(queryableList, "firstName",
                and(
                        between("salary", 1000, 2000),
                        eq("firstName", "firstC1"),
                        startsWith("lastName", "last"),
                        gt("birthDate", toDate("5.29.1940")),
                        startsWith("id", "ssn"),
                        gt("salary", 1000)
                ));

        assertEquals(1, results.size());

    }

    @Test
    public void testBetweenSalary_OR_PRECISE_NESTED_OR_AND() throws Exception {
        List<Employee> queryableList = $q(bigList);
        List<Employee> results = sortedQuery(queryableList, "firstName",
                or(
                        between("salary", 1001, 1002),
                        between("salary", 1002, 1003),
                        or(eq("firstName", "firstC12"), and(eq("firstName", "firstC10"), eq("firstName", "firstC11"))),
                        and(eq("firstName", "firstC20"), eq("firstName", "firstC21"), eq("firstName", "first22"),
                                or(eq("firstName", "firstC30"), eq("firstName", "firstC31"))),

                        or(
                                or(
                                        or(
                                                eq("firstName", "firstC52"),
                                                and(
                                                        eq("firstName", "firstC60"),
                                                        eq("firstName", "firstC61")
                                                )
                                        )
                                )
                        )


                ));

        print(results);
        assertEquals(4, results.size());
        assertEquals("firstC1", results.get(0).getFirstName());
        assertEquals("firstC12", results.get(1).getFirstName());
        assertEquals("firstC2", results.get(2).getFirstName());
        assertEquals("firstC52", results.get(3).getFirstName());


    }


    @Test
    public void testBetweenSalary_OR_PRECISE_NESTED_AND() throws Exception {
        List<Employee> queryableList = $q(bigList);
        List<Employee> results = sortedQuery(queryableList, "firstName",
                or(
                        between("salary", 1001, 1002),
                        between("salary", 1002, 1003),
                        and(eq("firstName", "firstC10"), eq("firstName", "firstC11"))

                ));

        assertEquals(2, results.size());

    }

    @Test
    public void testBetweenSalary_OR_PRECISE() throws Exception {
        List<Employee> queryableList = $q(bigList);
        List<Employee> results = sortedQuery(queryableList, "firstName",
                or(
                        between("salary", 1001, 1002),
                        between("salary", 1002, 1003),
                        eq("firstName", "firstC10"),
                        eq("firstName", "firstC11")

                ));

        assertEquals(4, results.size());

    }

    @Test
    public void testBetweenSalary_OR_LOTS_OF_TERMS_BIG_LIST() throws Exception {
        List<Employee> queryableList = $q(bigList);
        List<Employee> results = sortedQuery(queryableList, "firstName",
                or(
                        between("salary", 1000, 2000),
                        eq("firstName", "firstC1"),
                        startsWith("lastName", "last"),
                        gt("birthDate", toDate("5.29.1940")),
                        startsWith("id", "ssn"),
                        gt("salary", 1000)
                ));

        assertEquals(2002, results.size());

    }

    @Test
    public void testBetweenSalary_OR_FirstNameEQ_SECOND_TERM_NOT_FOUND_BIG_LIST() throws Exception {

        List<Employee> queryableList = $q(bigList);
        List<Employee> results = sortedQuery(queryableList, "firstName",
                or(between("salary", 1000, 2000), eq("firstName", "RANDOMISHSTRING" + System.currentTimeMillis())));

        assertEquals(1000, results.size());

    }

    @Test
    public void testBetweenSalary_AND_FirstNameEQ_SECOND_TERM_NOT_FOUND_BIG_LIST() throws Exception {

        List<Employee> queryableList = $q(bigList);
        List<Employee> results = sortedQuery(queryableList, "firstName",
                and(between("salary", 1000, 2000), eq("firstName", "RANDOMISHSTRING" + System.currentTimeMillis())));

        assertEquals(0, results.size());

    }

    @Test
    public void testBetweenSalary_OR_FirstNameEQ_FIRST_TERM_NOT_FOUND_BIG_LIST() throws Exception {

        List<Employee> queryableList = $q(bigList);
        List<Employee> results = sortedQuery(queryableList, "firstName",
                or(between("salary", 1, 3), startsWith("firstName", "firstC")));

        assertEquals(2000, results.size());

    }

    @Test
    public void testBetweenSalary_AND_FirstNameEQ_FIRST_TERM_NOT_FOUND_BIG_LIST() throws Exception {

        List<Employee> queryableList = $q(bigList);
        List<Employee> results = sortedQuery(queryableList, "firstName",
                and(between("salary", 1, 3), startsWith("firstName", "firstC")));

        assertEquals(0, results.size());

    }

    @Test
    public void testBetweenSalary_OR_FirstNameEQ_SECOND_TERM_NOT_FOUND() throws Exception {

        List<Employee> queryableList = $q(list);
        List<Employee> results = sortedQuery(queryableList, "firstName",
                or(between("salary", 199, 201), eq("firstName", "RANDOMISHSTRING" + System.currentTimeMillis())));

        assertEquals(1, results.size());

    }


    @Test
    public void testBetweenSalary_OR_FirstNameEQ_FIRST_TERM_NOT_FOUND() throws Exception {

        List<Employee> queryableList = $q(list);
        List<Employee> results = sortedQuery(queryableList, "firstName",
                or(between("salary", -1, -1), eq("firstName", "firstA")));

        assertEquals(1, results.size());

    }

    @Test
    public void testBetweenSalaryAndFirstNameEQ_FIRST_TERM_NOT_FOUND() throws Exception {

        List<Employee> queryableList = $q(list);
        List<Employee> results = sortedQuery(queryableList, "firstName",
                and(between("salary", -1, -1), eq("firstName", "firstA")));

        assertEquals(0, results.size());

    }

    @Test
    public void testBetweenSalaryAndFirstNameEQ_SECOND_TERM_NOT_FOUND() throws Exception {

        List<Employee> queryableList = $q(list);
        List<Employee> results = sortedQuery(queryableList, "firstName",
                and(between("salary", 0, 1000), eq("firstName", "RANDOMISHSTRING" + System.currentTimeMillis())));

        assertEquals(0, results.size());

    }

    @Test
    public void testBetweenSalaryAndFirstNameEQ_SECOND_TERM_FOUND() throws Exception {

        List<Employee> queryableList = $q(list);
        List<Employee> results = sortedQuery(queryableList, "firstName",
                and(between("salary", 0, 1000), eq("firstName", "firstA")));

        assertEquals(1, results.size());

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
        //rint(list);
        List<Employee> queryableList = $q(list);
        List<Employee> results = sortedQuery(queryableList, "firstName", between("salary", 400, 500));

        assertEquals(0, results.size());

    }

    //@Test  //Java data handling SUCKS! I don't think it is an issue with index lib.
    public void testBetweenDateExact() throws Exception {

        //rint(list);

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
