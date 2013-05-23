package org.datarepo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Employee {
    String firstName;
    String lastName;
    String ssn;
    Date birthDate;
    int salary;
    Department department = new Department();
    long empNum;
    List <Tag> tags = new ArrayList<>();
    {
        tags.add(new Tag("tag1"));
        tags.add(new Tag("tag2"));
        tags.add(new Tag("tag3"));

    }
    public static long num = System.currentTimeMillis();

    {
        empNum = num;
        num++;
    }

    static SimpleDateFormat dateParse = new SimpleDateFormat("dd.MM.yy");

    public static Employee employee(String f, String l, String s, String d, int salary) {
        Employee e = new Employee();
        try {
            e.birthDate = dateParse.parse(d);
        } catch (ParseException e1) {
            throw new RuntimeException(e1);
        }
        e.lastName = l;
        e.firstName = f;
        e.ssn = s;
        e.salary = salary;
        return e;
    }
    public static List<Employee> employees(Employee... _employees) {
        List <Employee> employees = new ArrayList<Employee>(_employees.length);
        for (Employee emp : _employees) {
            employees.add(emp);
        }
        return employees;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        if (birthDate != null ? !birthDate.equals(employee.birthDate) : employee.birthDate != null) return false;
        if (firstName != null ? !firstName.equals(employee.firstName) : employee.firstName != null) return false;
        if (lastName != null ? !lastName.equals(employee.lastName) : employee.lastName != null) return false;
        if (ssn != null ? !ssn.equals(employee.ssn) : employee.ssn != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (ssn != null ? ssn.hashCode() : 0);
        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
        return result;
    }


    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }


}
