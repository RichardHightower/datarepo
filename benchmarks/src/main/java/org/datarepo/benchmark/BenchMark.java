package org.datarepo.benchmark;

import org.datarepo.Repo;
import org.datarepo.Repos;
import org.datarepo.benchmark.model.Employee;
import org.datarepo.benchmark.utils.BenchmarkHelper;
import org.datarepo.query.Query;
import org.datarepo.utils.Utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.datarepo.query.QueryFactory.eq;
import static org.datarepo.utils.Utils.*;

public class BenchMark {


    public static void main(String[] args) {


        final List<Employee> employees = BenchmarkHelper.createMetricTonOfEmployees(100_000);
        print("employees created " + employees.size());

        Map<String, List<MeasuredRun>> testResults = new ConcurrentHashMap<>();

        MeasuredRun run1 = test(employees, testResults);

        List<MeasuredRun> runs = ls(run1);


        for (int index = 0; index < 2; index++) {

            for (MeasuredRun run : runs) {
                System.gc();
                Utils.sleep(10);
                run.run();
            }
        }

        sleep(3_000);
        print("Waiting...");
        sleep(1_000);
        print("Start Now...");
        sleep(1_000);

        for (int index = 0; index < 5; index++) {

            for (MeasuredRun run : runs) {
                System.gc();
                Utils.sleep(10);
                run.run();
                printf("%s\t%s", run.name(), run.time());
            }
        }

        print("done");


    }


    private static MeasuredRun test(final List<Employee> employees, final Map<String, List<MeasuredRun>> results) {
        return new MeasuredRun("test", 1000, 1_000_000, results) {
            Repo repo;
            Query exp = eq("firstName", "Mike");

            @Override
            protected void init() {
                                                    /* Create a repo, and decide what to index. */
                repo = Repos.builder().primaryKey("id").searchIndex("firstName").lookupIndex("firstName")
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
                Utils.assertTrue(found);

            }
        };
    }

}
