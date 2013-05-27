package org.datarepo.tests;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.datarepo.query.Criteria.eq;
import static org.datarepo.utils.Utils.*;

import static org.datarepo.Collections.*;

public class BenchMark {
    public static void main(String[] args) {

        Set<Employee> employees1 = new HashSet<>(TestHelper.createMetricTonOfEmployees(200_000));
        Set<Employee> employees2 = copy(employees1);

        employees1 = $q(employees1);

        boolean found = false;
        print("Data loaded");
        sleep(3000);
        print("Running tests");

        for (int index = 0; index < 500; index++) {

            List<String> firstNames = ls("Rick", "Vipin", "Diana", "Alex");

            long startTime = System.nanoTime();

            for (String firstName : firstNames) {
                List<Employee> list = query(employees1, eq("firstName", firstName));
                if (list != null && index == 0) {
                    found = true;
                    Employee emp = list.get(0);
                    if (!emp.getFirstName().equals(firstName)) {
                        throw new RuntimeException("bad mojo");
                    }
                }
            }
            long endTime = System.nanoTime() - startTime;
            print("index time", endTime);

            print(found);

            startTime = System.nanoTime();


            for (String firstName : firstNames) {

                Object[] results = employees2.stream().filter(emp -> emp.getFirstName().equals(firstName)).toArray();


                if (results != null && results.length > 0) {
                    List<Employee> list = ls(Employee.class, results);
                    found = true;
                    Employee emp = list.get(0);
                    if (!emp.getFirstName().equals(firstName)) {
                        throw new RuntimeException("bad mojo");
                    }
                }
            }

            endTime = System.nanoTime() - startTime;
            print("stream time", endTime);

        }


    }
}
