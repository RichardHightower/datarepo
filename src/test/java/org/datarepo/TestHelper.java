package org.datarepo;

import java.util.List;

import static org.datarepo.Employee.employee;
import static org.datarepo.Employee.employees;

public class TestHelper {

    final static String  getTestSSN = "777-222-2222";
    final static String  getTestFirstName = "AAA";


    static List<Employee> employees =  employees(
            employee("AAA", "Foo", "777-222-2222", "05.29.70", 10_000),
            employee("Boa", "Smith", "666-222-2222", "05.29.70", 10_000),
            employee("Bobbie","Smith", "322-222-2222", "05.29.70", 100_000),
            employee("Bob", "Smith", "122-222-2222", "05.29.70", 99_000),
            employee("Bob", "Smith", "222-222-2222", "05.29.70", 199_000),
            employee("Bobbzie","Smith", "422-222-2222", "05.29.70", 666_000),
            employee("Boc", "Smith", "666-222-2222", "05.29.70", 10_000),
            employee("Darth", "Sith", "666-222-2222", "05.29.70", 10_000),
            employee("ZZZ", "Zmm", "777-222-2222", "05.29.70", 10_000) );


    static Repo <String, Employee>  createBuilderNoReflection() {
        Repo <String, Employee> repo;

        RepoBuilder repoBuilder = RepoBuilder.getInstance();
        repoBuilder.primaryKey("ssn")
                .searchIndex("firstName").searchIndex("lastName").searchIndex("salary") ;
        repoBuilder.keyGetter("ssn", new KeyGetter<String, Employee>() {
            @Override
            public String getKey(Employee employee) {
                return employee.getSsn();
            }
        });

        repoBuilder.keyGetter("firstName", new KeyGetter<String, Employee>() {
            @Override
            public String getKey(Employee employee) {
                return employee.getFirstName();
            }
        });

        repoBuilder.keyGetter("lastName", new KeyGetter<String, Employee>() {
            @Override
            public String getKey(Employee employee) {
                return employee.getLastName();
            }
        });

        repoBuilder.keyGetter("salary", new KeyGetter<Integer, Employee>() {
            @Override
            public Integer getKey(Employee employee) {
                return employee.getSalary();
            }
        });

        repo  = repoBuilder.build(String.class, Employee.class);

        for (Employee employee : employees) {
            repo.add(employee);
        }
        return repo;
    }
}
