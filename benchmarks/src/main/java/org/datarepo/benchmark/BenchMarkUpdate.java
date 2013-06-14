package org.datarepo.benchmark;

import org.datarepo.Repo;
import org.datarepo.Repos;
import org.datarepo.benchmark.model.Employee;
import org.datarepo.benchmark.utils.BenchmarkHelper;
import org.datarepo.query.ValueSetter;
import org.datarepo.utils.Utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.datarepo.query.Criteria.eqNested;
import static org.datarepo.utils.Utils.*;

public class BenchMarkUpdate {


    public static void main(String[] args) {


        final List<Employee> employees = BenchmarkHelper.createMetricTonOfEmployees(100_000);
        print("employees created " + employees.size());

        Map<String, List<MeasuredRun>> testResults = new ConcurrentHashMap<>();

        MeasuredRun run1 = test(employees, testResults);
        MeasuredRun run2 = test1(employees, testResults);

        List<MeasuredRun> runs = ls(run1, run2);


        for (int index = 0; index < 1; index++) {

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

        for (int index = 0; index < 1; index++) {

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
        return new MeasuredRun("test indexed", 1, 100, results) {
            Repo repo;

            @Override
            protected void init() {
                                                    /* Create a repo, and decide what to index. */
                repo = Repos.builder().primaryKey("id").searchIndex("firstName")
                        .lookupIndex("firstName").nestedIndex("department", "name")

                        .build(String.class, Employee.class);

                repo.addAll(copy(employees));


            }

            @Override
            protected void test() {
                repo.updateByFilter(
                        ValueSetter.values(ValueSetter.incPercent("salary", 10)),
                        eqNested("engineering", "department", "name"));
            }
        };
    }

    private static MeasuredRun test1(final List<Employee> employees, final Map<String, List<MeasuredRun>> results) {
        return new MeasuredRun("test linear", 1, 100, results) {
            List<Employee> employeeList;

            @Override
            protected void init() {

                employeeList = copy(employees);


            }

            @Override
            protected void test() {
                for (Employee employee : employeeList) {
                    if (employee.getDepartment().getName().equals("engineering")) {
                        int increase = 10;
                        double dincrease = increase / 100.0;

                        int value = employee.getSalary();

                        double dvalue = value;

                        dvalue = dvalue + (dvalue * dincrease);
                        employee.setSalary((int) dvalue);
                    }
                }
            }
        };
    }

}
