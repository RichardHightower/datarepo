package org.datarepo.tests;

import org.datarepo.Repo;
import org.datarepo.RepoBuilder;
import org.datarepo.modification.ModificationEvent;
import org.datarepo.tests.model.Department;
import org.datarepo.tests.model.Employee;
import org.datarepo.tests.model.SalesEmployee;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.logging.Level;

import static org.datarepo.tests.model.Employee.employee;
import static org.datarepo.tests.model.Employee.employees;
import static org.datarepo.utils.Utils.list;
import static org.datarepo.utils.Utils.print;

public class TestHelper {

    final static String getTestSSN = "777-222-2222";
    final static String getTestFirstName = "AAA";


    static List<Employee> employees = employees(
            employee("AAA", "Foo", "777-222-2222", "05.29.70", 10_000, true),
            employee("Boa", "Smith", "666-222-2222", "05.29.70", 10_000),
            employee("Bobbie", "Smith", "322-222-2222", "05.29.70", 100_000),
            employee("Bob", "Smith", "122-222-2222", "05.29.70", 99_000, true),
            employee("Bob", "Smith", "222-222-2222", "05.29.70", 199_000),
            employee("Bobbzie", "Smith", "422-222-2222", "05.29.70", 666_000, true),
            employee("Boc", "Smith", "1010-222-2222", "05.29.70", 10_000),
            employee("Darth", "Sith", "1111-222-2222", "05.29.70", 10_000, true),
            employee("ZZZ", "Zmm", "777-333-3333", "05.29.70", 10_000));


    static Repo<String, Employee> createBuilderNoReflection() {
        Repo<String, Employee> repo;

        RepoBuilder repoBuilder = RepoBuilder.getInstance();
        repoBuilder.primaryKey("id")
                .searchIndex("firstName").searchIndex("lastName")
                .searchIndex("salary").uniqueSearchIndex("empNum");

        repoBuilder.keyGetter("id", new Function<Employee, String>() {
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

        repo = repoBuilder.build(String.class, Employee.class);

        for (Employee employee : employees) {
            repo.add(employee);
        }
        return repo;
    }


    //"tags", "metas", "metas2", "name2"

    static Repo<String, Employee> createFromBuilderNestedIndex() {

        /* Create a repo, and decide what to index. */
        RepoBuilder repoBuilder = RepoBuilder.getInstance();

        /* Decide what to index, ssn is primaryKey, firstName, lastName, and salary are indexes. */
        repoBuilder.primaryKey("id")
                .searchIndex("firstName").searchIndex("lastName")
                .searchIndex("salary").uniqueSearchIndex("empNum").nestedIndex("tags", "metas", "metas2", "name2");

        /* Create the repo with the builder. */
        Repo<String, Employee> repo
                = repoBuilder.build(String.class, Employee.class, SalesEmployee.class);

        for (Employee employee : employees) {
            repo.add(employee);
        }
        return repo;
    }

    static Repo<String, Employee> createFromBuilder() {

        /* Create a repo, and decide what to index. */
        RepoBuilder repoBuilder = RepoBuilder.getInstance();

        /* Decide what to index, ssn is primaryKey, firstName, lastName, and salary are indexes. */
        repoBuilder.primaryKey("id")
                .searchIndex("firstName").searchIndex("lastName")
                .searchIndex("salary").uniqueSearchIndex("empNum");

        /* Create the repo with the builder. */
        Repo<String, Employee> repo
                = repoBuilder.build(String.class, Employee.class, SalesEmployee.class);

        for (Employee employee : employees) {
            repo.add(employee);
        }
        return repo;
    }


    static Repo<String, Employee> createFromBuilderWithTransformAndCollation() {

        /* Create a repo, and decide what to index. */
        RepoBuilder repoBuilder = RepoBuilder.getInstance();

        /* Decide what to index, ssn is primaryKey, firstName, lastName, and salary are indexes. */
        repoBuilder.primaryKey("id")
                .searchIndex("firstName").searchIndex("lastName")
                .upperCaseIndex("firstName").collateIndex("lastName")
                .searchIndex("salary").uniqueSearchIndex("empNum");

        /* Create the repo with the builder. */
        Repo<String, Employee> repo
                = repoBuilder.build(String.class, Employee.class, SalesEmployee.class);

        for (Employee employee : employees) {
            repo.add(employee);
        }
        return repo;
    }


    public static Repo<String, Employee> createFromBuilderLogAndClone() {
        /* Create a repo, and decide what to index. */
        RepoBuilder repoBuilder = RepoBuilder.getInstance();

        /* Decide what to index, ssn is primaryKey, firstName, lastName, and salary are indexes. */
        repoBuilder.primaryKey("id")
                .searchIndex("firstName").searchIndex("lastName")
                .searchIndex("salary").uniqueSearchIndex("empNum")
                .debug().level(Level.INFO).cloneEdits(true).events((ModificationEvent event) -> {
            print(event);
        });

        /* Create the repo with the builder. */
        Repo<String, Employee> repo
                = repoBuilder.build(String.class, Employee.class, SalesEmployee.class);

        for (Employee employee : employees) {
            repo.add(employee);
        }
        return repo;

    }


    public static Repo<String, Employee> createWithNoIndexes() {
        /* Create a repo, and decide what to index. */
        RepoBuilder repoBuilder = RepoBuilder.getInstance();

        /* Decide what to index, ssn is primaryKey, firstName, lastName, and salary are indexes. */
        repoBuilder.primaryKey("id");


        /* Create the repo with the builder. */
        Repo<String, Employee> repo
                = repoBuilder.build(String.class, Employee.class, SalesEmployee.class);

        for (Employee employee : employees) {
            repo.add(employee);
        }
        return repo;

    }


    static Repo<String, Employee> createFromBuilderUsingPropertyAccess() {

        /* Create a repo, and decide what to index. */
        RepoBuilder repoBuilder = RepoBuilder.getInstance();

        /* Decide what to index, ssn is primaryKey, firstName, lastName, and salary are indexes. */
        repoBuilder.primaryKey("id")
                .searchIndex("firstName").searchIndex("lastName")
                .searchIndex("salary").uniqueSearchIndex("empNum")
                .usePropertyForAccess(true);

        /* Create the repo with the builder. */
        Repo<String, Employee> repo
                = repoBuilder.build(String.class, Employee.class, SalesEmployee.class);

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


        for (int index = 0; index < numEmps; index++) {
            Employee employee = new Employee();
            employees.add(employee);
            int firstNameIdx = Math.abs(random.nextInt() % numEmps);
            int lastNameIdx = Math.abs(random.nextInt() % numEmps);
            employee.setFirstName(firstNames.get(firstNameIdx));
            employee.setLastName(lastNames.get(lastNameIdx));
            employee.setEmpNum(index);
            employee.setSsn("" + index * 33 + "1234567-" + index);
            employee.setDepartment(new Department());
            int dept = index % 7;
            switch (dept) {
                case 0:
                    employee.getDepartment().setName("engineering");
                    employee.setSalary(100_000);
                    break;
                case 1:
                    employee.getDepartment().setName("finance");
                    employee.setSalary(300_000);
                    break;
                case 2:
                    employee.getDepartment().setName("accounting");
                    employee.setSalary(90_000);
                    break;
                case 3:
                    employee.getDepartment().setName("sales");
                    employee.setSalary(1_300_000);
                    break;
                case 4:
                    employee.getDepartment().setName("manufacturing");
                    employee.setSalary(30_000);
                    break;
                case 5:
                    employee.getDepartment().setName("marketing");
                    employee.setSalary(200_000);
                    break;
                case 6:
                    employee.getDepartment().setName("IT");
                    employee.setSalary(150_000);
                    break;
                default:
                    employee.getDepartment().setName("project mgmt");
                    employee.setSalary(100_000);
                    break;

            }

        }
        return employees;
    }

}
