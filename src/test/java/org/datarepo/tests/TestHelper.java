package org.datarepo.tests;

import org.datarepo.Repo;
import org.datarepo.RepoBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static org.datarepo.tests.Employee.employee;
import static org.datarepo.tests.Employee.employees;

import static org.datarepo.utils.Utils.*;

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
            employee("Boc", "Smith", "1010-222-2222", "05.29.70", 10_000),
            employee("Darth", "Sith", "1111-222-2222", "05.29.70", 10_000),
            employee("ZZZ", "Zmm", "777-333-3333", "05.29.70", 10_000) );


    static Repo<String, Employee> createBuilderNoReflection() {
        Repo <String, Employee> repo;

        RepoBuilder repoBuilder = RepoBuilder.getInstance();
        repoBuilder.primaryKey("ssn")
                .searchIndex("firstName").searchIndex("lastName")
                .searchIndex("salary").searchIndex("empNum", true);

        repoBuilder.keyGetter("ssn", new Function<Employee, String>() {
            @Override
            public String apply(Employee employee) {
                return employee.getSsn();
            }
        });

        repoBuilder.keyGetter("firstName", new Function<Employee, String>() {
            @Override
            public String apply(Employee employee) {
                return employee.getFirstName();
            }
        });

        repoBuilder.keyGetter("lastName", new Function<Employee, String>() {
            @Override
            public String apply(Employee employee) {
                return employee.getLastName();
            }
        });

        repoBuilder.keyGetter("salary", new Function<Employee, Integer>() {
            @Override
            public Integer apply(Employee employee) {
                return employee.getSalary();
            }
        });

        repo  = repoBuilder.build(String.class, Employee.class);

        for (Employee employee : employees) {
            repo.add(employee);
        }
        return repo;
    }

    static Repo <String, Employee>  createFromBuilder() {

        /* Create a repo, and decide what to index. */
        RepoBuilder repoBuilder = RepoBuilder.getInstance();

        /* Decide what to index, ssn is primaryKey, firstName, lastName, and salary are indexes. */
        repoBuilder.primaryKey("ssn")
                .searchIndex("firstName").searchIndex("lastName")
                .searchIndex("salary").searchIndex("empNum", true);

        /* Create the repo with the builder. */
        Repo <String, Employee> repo
             = repoBuilder.build(String.class, Employee.class);

        for (Employee employee : employees) {
            repo.add(employee);
        }
        return repo;
    }

    static Repo <String, Employee>  createFromBuilderUsingPropertyAccess() {

        /* Create a repo, and decide what to index. */
        RepoBuilder repoBuilder = RepoBuilder.getInstance();

        /* Decide what to index, ssn is primaryKey, firstName, lastName, and salary are indexes. */
        repoBuilder.primaryKey("ssn")
                .searchIndex("firstName").searchIndex("lastName")
                .searchIndex("salary").searchIndex("empNum", true)
                .usePropertyForAccess(true);

        /* Create the repo with the builder. */
        Repo <String, Employee> repo
                = repoBuilder.build(String.class, Employee.class);

        for (Employee employee : employees) {
            repo.add(employee);
        }
        return repo;
    }

    public static List<Employee> createMetricTonOfEmployees(int numEmps) {
        List<Employee> employees = new ArrayList<>();
        List<String> firstNames = list(RandomWordUtil.generateWords(numEmps));
        List<String> lastNames = list(RandomWordUtil.generateWords(numEmps));
        Random random = new Random(System.nanoTime());


        for (int index =0 ; index < numEmps; index++) {
            Employee employee = new Employee();
            employees.add(employee);
            int firstNameIdx = Math.abs(random.nextInt() % numEmps);
            int lastNameIdx = Math.abs(random.nextInt() % numEmps);
            employee.setFirstName(firstNames.get(firstNameIdx));
            employee.setLastName(lastNames.get(lastNameIdx));
            employee.empNum = index;
            employee.setSsn("" + index * 33 + "1234567-" + index);
            employee.department = new Department();
            int dept = index % 7;
            switch (dept) {
                case 0:
                    employee.department.setName("engineering");
                    employee.setSalary(100_000);
                    break;
                case 1:
                    employee.department.setName("finance");
                    employee.setSalary(300_000);
                    break;
                case 2:
                    employee.department.setName("accounting");
                    employee.setSalary(90_000);
                    break;
                case 3:
                    employee.department.setName("sales");
                    employee.setSalary(1_300_000);
                    break;
                case 4:
                    employee.department.setName("manufacturing");
                    employee.setSalary(30_000);
                    break;
                case 5:
                    employee.department.setName("marketing");
                    employee.setSalary(200_000);
                    break;
                case 6:
                    employee.department.setName("IT");
                    employee.setSalary(150_000);
                    break;
                default:
                    employee.department.setName("project mgmt");
                    employee.setSalary(100_000);
                    break;

            }

        }
        return employees;
    }

}
