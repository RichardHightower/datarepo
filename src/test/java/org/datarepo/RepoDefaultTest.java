package org.datarepo;

import org.junit.Test;
import org.junit.Before;

import java.util.List;

import static junit.framework.Assert.*;
import static org.datarepo.Employee.employee;
import static org.datarepo.Employee.employees;
import static org.datarepo.criteria.Criteria.*;

public class RepoDefaultTest {

    Repo <String, Employee> repo;



    @Before
    public void setup() {
        repo = TestHelper.createFromBuilderUsingPropertyAccess();
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
        assertNotSame(emp, repo.get("21785999"));
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
    public void testEasyFilter() throws Exception {
        Employee emp = employee("Diana", "Hightower", "21785999", "08.15.82", 100_000);
        repo.add(emp);
        List<Employee> employees = repo.query(eq("firstName", "Diana"));
        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals("Diana", employees.get(0).getFirstName());
    }



    @Test
    public void testHarderFilter() throws Exception {
        Employee emp = employee("Diana", "Hightower", "21785999", "08.15.82", 100_000);
        repo.add(emp);
        List<Employee> employees = repo.query(
                and(eq("firstName", "Diana"), eq("lastName", "Hightower"), eq("ssn", "21785999")));
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

}
