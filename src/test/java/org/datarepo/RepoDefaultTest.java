package org.datarepo;

import org.junit.Test;
import org.junit.Before;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import static org.datarepo.Employee.employee;
import static org.datarepo.Employee.employees;

public class RepoDefaultTest {

    final String  getTestSSN = "777-222-2222";
    final String  getTestFirstName = "AAA";
    Repo <String, Employee> repo;

    List<Employee> employees =  Employee.employees(
            employee("AAA", "Foo", "777-222-2222", "05.29.70"),
            employee("Boa", "Smith", "666-222-2222", "05.29.70"),
            employee("Bobbie","Smith", "322-222-2222", "05.29.70"),
            employee("Bob", "Smith", "122-222-2222", "05.29.70"),
            employee("Bob", "Smith", "222-222-2222", "05.29.70"),
            employee("Bobbzie","Smith", "422-222-2222", "05.29.70"),
            employee("Boc", "Smith", "666-222-2222", "05.29.70"),
            employee("Darth", "Sith", "666-222-2222", "05.29.70"),
            employee("ZZZ", "Zmm", "777-222-2222", "05.29.70") );


    @Before
    public void setup() {
        RepoBuilder repoBuilder = RepoBuilder.getInstance();
        repoBuilder.primaryKey("ssn").searchIndex("firstName")
                .searchIndex("salary");
        repoBuilder.keyGetter("ssn", new KeyGetter<String, Employee>() {
            @Override
            public String getKey(Employee employee) {
                return employee.getSsn();
            }
        });

        repo  = repoBuilder.build(String.class, Employee.class);

        for (Employee employee : employees) {
            repo.add(employee);
        }
    }

    @Test
    public void testGet() throws Exception {
        Employee employee = repo.get(this.getTestSSN);
        assertNotNull("employee should not be null", employee);
        String firstName = employee.getFirstName();
        assertEquals("firstName should be this", this.getTestFirstName, firstName);

    }

    @Test
    public void testRemove() throws Exception {
        Employee emp = employee("Diana", "Hightower", "21785999", "08.15.82");
        repo.add(emp);
        assertNotNull(repo.get("21785999"));
        repo.remove(emp);
        assertNull("We were able to remove emp", repo.get("21785999"));

    }
}
