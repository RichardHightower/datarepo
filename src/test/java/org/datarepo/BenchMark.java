package org.datarepo;

import java.util.List;
import java.util.stream.Stream;

import static org.datarepo.criteria.Criteria.eq;
import static org.datarepo.reflection.Reflection.*;
import static org.datarepo.utils.Utils.*;

public class BenchMark {
    public static void main (String [] args) {

        List<Employee> employees1 = TestHelper.createMetricTonOfEmployees(200_000);
        List<Employee> employees2 = copy(employees1);


        Repo<String, Employee> repo = RepoBuilder.getInstance()
                .searchIndex("firstName")
                .primaryKey("ssn")
                .build(String.class, Employee.class);

        repo.addAll(employees1);

        boolean found = false;
        List<String> firstNames = ls("Rick", "Vipin", "Diana", "Alex");

        long startTime = System.currentTimeMillis();
        print("start time", startTime);
        for (String firstName : firstNames) {
             List<Employee> list = repo.query(eq("firstName", firstName));
             if (list!=null) {
                 found = true;
                 Employee emp = list.get(0);
                 if (!emp.getFirstName().equals(firstName)){
                     throw new RuntimeException("bad mojo");
                 }
             }
        }
        print("time",  System.currentTimeMillis() - startTime);

        print(found);

        startTime = System.currentTimeMillis();
        print("start time", startTime);


        for (String firstName : firstNames) {
            List<Employee> list = ls(Employee.class, employees2.stream().filter(emp -> emp.getFirstName().equals(firstName)).toArray());

            if (list!=null && list.size()>0) {
                found = true;
                Employee emp = list.get(0);
                if (!emp.getFirstName().equals(firstName)){
                    throw new RuntimeException("bad mojo");
                }
            }
        }

        print("time",  System.currentTimeMillis() - startTime);


    }
}
