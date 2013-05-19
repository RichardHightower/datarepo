package org.datarepo;

import org.junit.Test;
import org.junit.Before;

import java.util.List;
import static org.datarepo.Employee.employee;
import static org.datarepo.Employee.employees;

public class RepoDefaultTest {

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
        repoBuilder.keyGetter("firstName", new KeyGetter<Object, Object>() {
            @Override
            public Object getKey(Object o) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        Repo repo = repoBuilder.build(String.class, Employee.class);

        for (Employee employee : employees) {
            repo.add(employee);
        }
    }

    @Test
    public void testGet() throws Exception {

    }

    @Test
    public void testAdd() throws Exception {

    }

    @Test
    public void testRemove() throws Exception {

    }
}
