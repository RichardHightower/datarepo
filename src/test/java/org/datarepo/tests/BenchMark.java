package org.datarepo.tests;


import org.datarepo.query.Criterion;
import org.datarepo.query.Expression;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.datarepo.Collections.$q;
import static org.datarepo.Collections.query;
import static org.datarepo.query.Criteria.eq;
import static org.datarepo.utils.Utils.*;

public class BenchMark {
    public static void main(String[] args) {


        Set<Employee> employees1 = new HashSet<>(TestHelper.createMetricTonOfEmployees(100_000));

        Set<Employee> employees2 = copy(employees1);

        employees1 = $q(employees1);

        boolean found = false;
        print("Data loaded - Start Profiler now");
        sleep(3000);
        print("Running tests");

        List<String> firstNames = ls("Rick", "Vipin", "Diana", "Alex");

        for (int index = 0; index < 5; index++) {
            print(index);
            for (String firstName : firstNames) {
                List<Employee> list = query(employees1, eq("firstName", firstName));
                if (list != null && index == 0) {
                    found = true;
                }
            }
        }

        List<Criterion> firstNamesExp = ls(eq("firstName", "Rick"),
                eq("firstName", "Vipin"), eq("firstName", "Diana"), eq("firstName", "Alex"));

        long startTime = System.nanoTime();
        for (int index = 0; index < 100_000; index++) {

            for (Expression firstNameExp : firstNamesExp) {
                List<Employee> list = query(employees1, firstNameExp);
                if (list != null && index == 0) {
                    found = true;
                }
            }
        }

        long endTime = (System.nanoTime() - startTime) / 100_000;
        print("index time", endTime);

        for (int index = 0; index < 10; index++) {
            for (String firstName : firstNames) {
                Object[] results = employees2.stream().filter(emp -> emp.getFirstName().equals(firstName)).toArray();
                if (results != null && results.length > 0) {
                    found = true;
                }
            }
        }

        startTime = System.nanoTime();
        for (int index = 0; index < 50; index++) {
            for (String firstName : firstNames) {
                Object[] results = employees2.stream().filter(emp -> emp.getFirstName().equals(firstName)).toArray();
                if (results != null && results.length > 0) {
                    found = true;
                }
            }
        }
        endTime = (System.nanoTime() - startTime) / 50;
        print("stream time", endTime);


    }
}
