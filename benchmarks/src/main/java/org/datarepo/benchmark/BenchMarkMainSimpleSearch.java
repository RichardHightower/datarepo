package org.datarepo.benchmark;

import org.datarepo.Repo;
import org.datarepo.Repos;
import org.datarepo.benchmark.model.Employee;
import org.datarepo.benchmark.utils.BenchmarkHelper;
import org.datarepo.query.Query;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.datarepo.query.QueryFactory.eq;


import static org.boon.utils.Utils.ls;
import static org.boon.utils.Utils.assertTrue;
import static org.boon.utils.Utils.sleep;

public class BenchMarkMainSimpleSearch {


    public static void main(String[] args) {


        final List<Employee> employees = BenchmarkHelper.createMetricTonOfEmployees(100_000);
        System.out.println("employees created " + employees.size());

        Map<String, List<MeasuredRun>> testResults = new ConcurrentHashMap<>();

        MeasuredRun run1 = firstNameSearchIndexTest_DR(employees, testResults);
        MeasuredRun run2 = firstNameSearchTest(employees, testResults);
        MeasuredRun run3 = firstNameSearchNoIndexTest_DR(employees, testResults);
        MeasuredRun run4 = firstNameSearchNoIndexTestUnsafe_DR(employees, testResults);
        MeasuredRun run5 = linearSearchWithCache_DR(employees, testResults);

        List<MeasuredRun> runs = ls(run1, run2, run3, run4, run5);


        for (int index = 0; index < 2; index++) {

            for (MeasuredRun run : runs) {
                System.gc();
                sleep(10);
                run.run();
            }
        }


        for (int index = 0; index < 5; index++) {

            for (MeasuredRun run : runs) {
                System.gc();
                sleep(10);
                run.run();
                System.out.printf("%s\t%s", run.name(), run.time());
            }
        }

        System.out.println("done");


    }

    private static MeasuredRun firstNameSearchTest(final List<Employee> employees, final Map<String, List<MeasuredRun>> results) {
        return new MeasuredRun("A_linear_search", 1000, 10_000, results) {

            List<Employee> employeesList;

            @Override
            protected void init() {

                employeesList = employees;
            }

            @Override
            protected void test() {
                List<Employee> results = employeesList;

                boolean found = false;
                for (Employee employee : results) {
                    if (employee.getFirstName().equals("Mike")
                            && employee.getLastName().equals("Middleoflist")) {
                        found = true;
                        break;
                    }
                }
                assertTrue(found);
            }
        };
    }


    private static MeasuredRun firstNameSearchNoIndexTest_DR(final List<Employee> employees, final Map<String, List<MeasuredRun>> results) {
        return new MeasuredRun("A_no_index_DR", 1000, 10_000, results) {
            Repo repo;
            Query exp = eq("firstName", "Mike");

            @Override
            protected void init() {
                                                    /* Create a repo, and decide what to index. */
                repo = Repos.builder().primaryKey("id")
                        .build(String.class, Employee.class);

                repo.addAll(employees);


            }

            @Override
            protected void test() {
                List<Employee> results = repo.query(exp);

                boolean found = false;
                for (Employee employee : results) {
                    if (employee.getFirstName().equals("Mike")
                            && employee.getLastName().equals("Middleoflist")) {
                        found = true;
                        break;
                    }
                }
                assertTrue(found);

            }
        };
    }

    private static MeasuredRun firstNameSearchNoIndexTestUnsafe_DR(final List<Employee> employees, final Map<String, List<MeasuredRun>> results) {
        return new MeasuredRun("A_no_index_unsafe_DR", 1000, 10_000, results) {
            Repo repo;
            Query exp = eq("firstName", "Mike");

            @Override
            protected void init() {
                                                    /* Create a repo, and decide what to index. */
                repo = Repos.builder().primaryKey("id").useUnsafe(true)
                        .build(String.class, Employee.class);

                repo.addAll(employees);


            }

            @Override
            protected void test() {
                List<Employee> results = repo.query(exp);

                boolean found = false;
                for (Employee employee : results) {
                    if (employee.getFirstName().equals("Mike")
                            && employee.getLastName().equals("Middleoflist")) {
                        found = true;
                        break;
                    }
                }
                assertTrue(found);

            }
        };
    }

    private static MeasuredRun linearSearchWithCache_DR(final List<Employee> employees, final Map<String, List<MeasuredRun>> results) {
        return new MeasuredRun("A_no_index_with_cache_DR", 1000, 10_000, results) {
            Repo repo;
            Query exp = eq("firstName", "Mike");

            @Override
            protected void init() {
                                                    /* Create a repo, and decide what to index. */
                repo = Repos.builder().primaryKey("id").useCache()
                        .build(String.class, Employee.class);

                repo.addAll(employees);


            }

            @Override
            protected void test() {
                List<Employee> results = repo.query(exp);

                boolean found = false;
                for (Employee employee : results) {
                    if (employee.getFirstName().equals("Mike")
                            && employee.getLastName().equals("Middleoflist")) {
                        found = true;
                        break;
                    }
                }
                assertTrue(found);

            }
        };
    }

    private static MeasuredRun firstNameSearchIndexTest_DR(final List<Employee> employees, final Map<String, List<MeasuredRun>> results) {
        return new MeasuredRun("A_with_search_index_DR", 1000, 10_000, results) {
            Repo repo;
            Query exp = eq("firstName", "Mike");

            @Override
            protected void init() {
                                                    /* Create a repo, and decide what to index. */
                repo = Repos.builder().primaryKey("id")
                        .searchIndex("firstName").build(String.class, Employee.class);

                repo.addAll(employees);


            }

            @Override
            protected void test() {
                List<Employee> results = repo.query(exp);

                boolean found = false;
                for (Employee employee : results) {
                    if (employee.getFirstName().equals("Mike")
                            && employee.getLastName().equals("Middleoflist")) {
                        found = true;
                        break;
                    }
                }
                assertTrue(found);

            }
        };
    }

}
