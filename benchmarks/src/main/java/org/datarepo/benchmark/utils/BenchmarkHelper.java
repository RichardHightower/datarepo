package org.datarepo.benchmark.utils;

import org.datarepo.benchmark.model.Department;
import org.datarepo.benchmark.model.Employee;
import org.datarepo.benchmark.model.SalesEmployee;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.boon.utils.Utils.list;
import static org.boon.utils.Utils.print;

public class BenchmarkHelper {

    final static String getTestSSN = "777-222-2222";
    final static String getTestFirstName = "AAA";


    public static List<Employee> createMetricTonOfEmployees(int numEmps) {
        List<Employee> employees = new ArrayList<>();
        List<String> firstNames = list(RandomWordUtil.generateWords(numEmps));
        List<String> lastNames = list(RandomWordUtil.generateWords(numEmps));


        print("Creating this many employees " + numEmps);
        Random random = new Random(System.nanoTime());


        for (int index = 0; index < numEmps; index++) {
            Employee employee = null;

            if (index % 100 == 0) {
                employee = new Employee();
            } else {
                employee = new SalesEmployee();
            }

            if (index % 1000 == 0) {
                print("employee count " + index);
            }
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


        //A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z
        employees.add(Employee.employee("Mike", "Middleoflist"));
        employees.add(Employee.employee("Asimov", "Asistopoflist"));
        employees.add(Employee.employee("Zed", "Zedisbottomoflist"));
        employees.add(Employee.employee("Xan", "Xiddleofnowhere"));
        employees.add(Employee.employee("Jan", "Janmiddlelowertopoflist"));

        print("Created this many employees " + employees.size());


        return employees;
    }

}
