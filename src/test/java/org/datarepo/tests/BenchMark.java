package org.datarepo.tests;

import org.datarepo.Repo;
import org.datarepo.RepoBuilder;
import org.datarepo.tests.Employee;
import org.datarepo.tests.TestHelper;

import java.util.List;

import static org.datarepo.criteria.Criteria.eq;
import static org.datarepo.utils.Utils.*;

public class BenchMark {
    public static void main (String [] args) {

        List<Employee> employees1 = TestHelper.createMetricTonOfEmployees(200_000);
        List<Employee> employees2 = copy(employees1);


        Repo<String, Employee> repo = RepoBuilder.getInstance()
                .searchIndex("firstName")
                .lookupIndex("firstName")
                .primaryKey("ssn")
                .build(String.class, Employee.class);

        repo.addAll(employees1);

        boolean found = false;
        print("Data loaded");
        sleep(3000);
        print("Running tests");

        for (int index=0; index < 5; index++) {

        List<String> firstNames = ls("Rick", "Vipin", "Diana", "Alex");

        long startTime = System.nanoTime();

        for (String firstName : firstNames) {
             List<Employee> list = repo.query(eq("firstName", firstName));
             if (list!=null && index == 0) {
                 found = true;
                 Employee emp = list.get(0);
                 if (!emp.getFirstName().equals(firstName)){
                     throw new RuntimeException("bad mojo");
                 }
             }
        }
        long endTime = System.nanoTime() - startTime;
        print("index time",  endTime);

        print(found);

        startTime = System.nanoTime();


        for (String firstName : firstNames) {

            Object[] results = employees2.stream().filter(emp -> emp.getFirstName().equals(firstName)).toArray();


            if (results!=null && results.length>0) {
                List<Employee> list = ls(Employee.class, results);
                found = true;
                Employee emp = list.get(0);
                if (!emp.getFirstName().equals(firstName)){
                    throw new RuntimeException("bad mojo");
                }
            }
        }

        endTime = System.nanoTime() - startTime;
        print("stream time",  endTime );

        }




    }
}
