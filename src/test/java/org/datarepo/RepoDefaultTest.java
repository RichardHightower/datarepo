package org.datarepo;

import org.datarepo.reflection.Reflection;
import org.junit.Test;
import org.junit.Before;

import java.util.List;
import java.util.Map;

import static junit.framework.Assert.*;
import static junit.framework.Assert.assertEquals;
import static org.datarepo.Employee.employee;
import static org.datarepo.criteria.Criteria.*;
import static org.datarepo.criteria.ValueSetter.*;
import static org.datarepo.criteria.Selector.*;

import static org.datarepo.utils.Utils.*;
import static org.datarepo.reflection.Reflection.*;

public class RepoDefaultTest {

    Repo <String, Employee> repo;



    @Before
    public void setup() {
        repo = TestHelper.createFromBuilder();
    }

    @Test
    public void testGet() throws Exception {
        Employee employee = repo.get(TestHelper.getTestSSN);
        assertNotNull("employee should not be null", employee);
        String firstName = employee.getFirstName();
        assertEquals("firstName should be this", TestHelper.getTestFirstName, firstName);

    }

    @Test
    public void testAdd() throws Exception {
        Employee emp = employee("Diana", "Hightower", "21785999", "08.15.82", 100_000);
        repo.add(emp);
        assertNotNull(repo.get("21785999"));
        //assertNotSame(emp, repo.get("21785999"));
        repo.remove(emp);
        assertNull("We were able to remove emp", repo.get("21785999"));

    }

    @Test
    public void testRemove() throws Exception {
        Employee emp = employee("Diana", "Hightower", "21785999", "08.15.82", 100_000);
        repo.add(emp);
        assertNotNull(repo.get("21785999"));
        repo.remove(emp);
        assertNull("We were able to remove emp", repo.get("21785999"));

    }

    @Test
    public void testModify() throws Exception {
        Employee emp = employee("Diana", "Hightower", "21785999", "08.15.82", 100_000);
        repo.add(emp);
        assertNotNull(repo.get("21785999"));
        repo.modify(emp, "firstName", "Di");

        String firstName = repo.get("21785999").getFirstName();
        assertEquals("firstName equals", "Di", firstName);

        assertEquals("Test that the search index is rebuilt", "Di",
                repo.query(eq("firstName", "Di")).get(0).getFirstName());

    }

    @Test
    public void testUpdateByKey() throws Exception {
        Employee emp = employee("Diana", "Hightower", "21785999", "08.15.82", 100_000);
        repo.add(emp);
        assertNotNull(repo.get("21785999"));
        repo.update(emp.getSsn(), "firstName", "Di");

        String firstName = repo.get("21785999").getFirstName();
        assertEquals("firstName equals", "Di", firstName);

        assertEquals("Test that the search index is rebuilt", "Di",
                repo.query(eq("firstName", "Di")).get(0).getFirstName());

    }

    @Test
    public void testUpdateByKeyUsingValues() throws Exception {
        Employee emp = employee("Diana", "Hightower", "217859991", "08.15.82", 100_000);
        repo.add(emp);
        assertNotNull(repo.get("217859991"));
        repo.update(emp.getSsn(), value("firstName", "Di"));

        String firstName = repo.get("217859991").getFirstName();
        assertEquals("firstName equals", "Di", firstName);

        assertEquals("Test that the search index is rebuilt", "Di",
                repo.query(eq("firstName", "Di")).get(0).getFirstName());

    }

    @Test
    public void testUpdateByFilter() throws Exception {
        Employee emp = employee("Diana", "Hightower", "217859992", "08.15.82", 100_000);
        repo.add(emp);
        assertNotNull(repo.get("217859992"));


        repo.updateByFilter("firstName", "Di",
                and( eq("firstName", "Diana"),
                eq("lastName", "Hightower"),
                        eq("ssn", "217859992") ) );


        String firstName = repo.get("217859992").getFirstName();
        assertEquals("firstName equals", "Di", firstName);

        assertEquals("Test that the search index is rebuilt", "Di",
                repo.query(eq("firstName", "Di")).get(0).getFirstName());

    }

    @Test
    public void testUpdateByFilterUsingValues() throws Exception {
        Employee emp = employee("Diana", "Hightower", "2178599917788", "08.15.82", 100_000);
        repo.add(emp);
        assertNotNull(repo.get("2178599917788"));


        repo.updateByFilter(values(value("firstName", "Di")),
                and( eq("firstName", "Diana"),
                        eq("lastName", "Hightower"),
                        eq("ssn", "2178599917788") ) );


        String firstName = repo.get("2178599917788").getFirstName();
        assertEquals("firstName equals", "Di", firstName);

        assertEquals("Test that the search index is rebuilt", "Di",
                repo.query(eq("firstName", "Di")).get(0).getFirstName());

    }

    @Test
    public void testEasyFilter2() throws Exception {
        Employee emp = employee("Diana", "Hightower", "7178599912", "08.15.82", 100_000);
        repo.add(emp);
        emp = employee("Diana", "Hightower", "8178599912", "08.15.82", 100_000);
        repo.add(emp);

        List<Employee> employees = repo.query(eq("firstName", "Diana"));
        assertNotNull(employees);
        assertEquals(2, employees.size());
        assertEquals("Diana", employees.get(0).getFirstName());
    }
    @Test
    public void testEasyFilter() throws Exception {
        Employee emp = employee("Diana", "Hightower", "2178599912", "08.15.82", 100_000);
        repo.add(emp);
        List<Employee> employees = repo.query(eq("firstName", "Diana"));
        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals("Diana", employees.get(0).getFirstName());
    }

    @Test
    public void testEasyFilterByMap() throws Exception {
        Employee emp = employee("Diana", "Hightower", "3178599912", "08.15.82", 100_000);
        repo.add(emp);
        List<Map<String, Object>> employees = repo.queryAsMaps(eq("firstName", "Diana"));
        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals("Diana", employees.get(0).get("firstName"));

        System.out.println(employees.get(0).get("department"));
    }

    @Test
    public void testEasySelect() throws Exception {
        Employee emp = employee("Diana", "Hightower", "2178599966", "08.15.82", 100_000);
        Employee emp2 = employee("Bob", "Hightower", "21785990", "08.15.82", 100_000);

        repo.add(emp);
        repo.add(emp2);

        List <Map<String, Object>> list = repo.query(selects(select("firstName")), eq("lastName", "Hightower"));

        assertEquals("Diana", list.get(0).get("firstName"));
        assertEquals("Bob", list.get(1).get("firstName"));

    }

    @Test
    public void testQueryOnUniqueIndex() throws Exception {
        List <Map<String, Object>> list = repo.query(selects(select("firstName")), gt("empNum", 5l));
        assertNotNull(list);
        assertTrue(list.size()>1);

    }

    @Test
    public void testFieldPathSelect() throws Exception {
        Employee emp = employee("Diana", "Hightower", "2178599966", "08.15.82", 100_000);
        Employee emp2 = employee("Bob", "Hightower", "21785990", "08.15.82", 100_000);

        repo.add(emp);
        repo.add(emp2);

        List <Map<String, Object>> list = repo.query(
                selects(select("department", "name")),
                eq("lastName", "Hightower"));

        assertEquals("engineering", list.get(0).get("department.name"));
        assertEquals("engineering", list.get(1).get("department.name"));

    }

    @Test
    public void testFieldPathSelectToCollection() throws Exception {
        Employee emp = employee("Diana", "Hightower", "2178599966", "08.15.82", 100_000);
        Employee emp2 = employee("Bob", "Hightower", "21785990", "08.15.82", 100_000);

        repo.add(emp);
        repo.add(emp2);

        List <Map<String, Object>> list = repo.query(
                selects(select("tags", "name")),
                eq("lastName", "Hightower"));

        print("list", list);
        assertEquals("tag1", idx(list.get(0).get("tags.name"), 0));

    }

    @Test
    public void testFieldPathSelectToCollection2() throws Exception {
        Employee emp = employee("Diana", "Hightower", "2178599966", "08.15.82", 100_000);
        Employee emp2 = employee("Bob", "Hightower", "21785990", "08.15.82", 100_000);

        repo.add(emp);
        repo.add(emp2);

        List <Map<String, Object>> list = repo.query(
                selects(select("tags", "metas", "name0")),
                eq("lastName", "Hightower"));

        print("list", list);
        assertEquals("mtag1", idx(list.get(0).get("tags.metas.name0"), 0));


    }
    @Test
    public void testFieldPathSelectToCollection3() throws Exception {
        Employee emp = employee("Diana", "Hightower", "2178599966", "08.15.82", 100_000);
        Employee emp2 = employee("Bob", "Hightower", "21785990", "08.15.82", 100_000);

        repo.add(emp);
        repo.add(emp2);

        List <Map<String, Object>> list = repo.query(
                selects(select("tags", "metas", "metas2", "name2")),
                eq("lastName", "Hightower"));

        print("list", list);

        assertEquals("2tag1", idx(list.get(0).get("tags.metas.metas2.name2"), 0));


    }

    @Test
    public void testPropertyPathSelect() throws Exception {
        Employee emp = employee("Diana", "Hightower", "2178599966", "08.15.82", 100_000);
        Employee emp2 = employee("Bob", "Hightower", "21785990", "08.15.82", 100_000);

        repo.add(emp);
        repo.add(emp2);

        List <Map<String, Object>> list = repo.query(
                selects(selectPropPath("department", "name")),
                eq("lastName", "Hightower"));

        assertEquals("engineering", list.get(0).get("department.name"));
        assertEquals("engineering", list.get(1).get("department.name"));

    }

    @Test
    public void testEasySelectWithSort() throws Exception {
        Employee emp = employee("Diana", "Hightower", "2178599990", "08.15.82", 100_000);
        Employee emp2 = employee("Bob", "Hightower", "2178599088", "08.15.82", 100_000);

        repo.add(emp);
        repo.add(emp2);

        List <Map<String, Object>> list = repo.sortedQuery("firstName",selects(select("firstName")), eq("lastName", "Hightower"));

        assertEquals("Bob", list.get(0).get("firstName"));
        assertEquals("Diana", list.get(1).get("firstName"));

    }


    @Test
    public void testHarderFilter() throws Exception {
        Employee emp = employee("Diana", "Hightower", "217859997", "08.15.82", 100_000);
        repo.add(emp);
        List<Employee> employees = repo.query(
                and(eq("firstName", "Diana"), eq("lastName", "Hightower"), eq("ssn", "217859997")));
        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals("Diana", employees.get(0).getFirstName());
    }

    @Test
    public void testFilterLogicalOperators() throws Exception {
        List<Employee> employees = repo.query(
                and(startsWith("firstName", "Bob"), eq("lastName", "Smith"), lte("salary", 200_000),
                        gte("salary", 190_000)));
        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals("Bob", employees.get(0).getFirstName());
        assertEquals("222-222-2222", employees.get(0).getSsn());

    }

    @Test
    public void testFilterLogicalOperators2() throws Exception {
        List<Employee> employees = repo.query(
                and(startsWith("firstName", "Bob"), eq("lastName", "Smith"), lt("salary", 200_000),
                        gt("salary", 190_000)));
        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals("Bob", employees.get(0).getFirstName());
        assertEquals("222-222-2222", employees.get(0).getSsn());

    }

    @Test
    public void testFilterLogicalOperators3() throws Exception {
        List<Employee> employees = repo.query(
                and(startsWith("firstName", "Bob"), eq("lastName", "Smith"), between("salary", 190_000, 200_000)));
        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals("Bob", employees.get(0).getFirstName());
        assertEquals("222-222-2222", employees.get(0).getSsn());

    }

    @Test
    public void testFieldPathSelectToCollection4() throws Exception {
        Employee emp = employee("Diana", "Hightower", "asdf", "08.15.82", 100_000);
        Employee emp2 = employee("Bob", "Hightower", "217asdfasdfasdf85990", "08.15.82", 100_000);

        repo.add(emp);
        repo.add(emp2);

        List <Map<String, Object>> list = repo.query(
                selects(select("tags", "metas", "metas2", "metas3", "name3")),
                eq("lastName", "Hightower"));

        print("list", list);

        assertEquals("3tag1", idx(list.get(0).get("tags.metas.metas2.metas3.name3"), 0));


    }

}
