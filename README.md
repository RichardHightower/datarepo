datarepo
========

Data Repo


DataRepo allows you to treat Java collections more like a database.
I tired using the JDK 8 stream API on a large dataset, and it was slow.
It was more or less a linear search. This is by design, but for what I was doing, it did not work.


A simple query looks like this:

```
        List<Employee> employees = repo.query(eq("firstName", "Diana"));
```

A more involved query would look like this:

```
        List<Employee> employees = repo.query(
                and(eq("firstName", "Diana"), eq("lastName", "Smith"), eq("ssn", "21785999")));
```

Or this:
```
        List<Employee> employees = repo.query(
                and(startsWith("firstName", "Bob"), eq("lastName", "Smith"), lte("salary", 200_000),
                        gte("salary", 190_000)));
```
Or this:
```
        List<Employee> employees = repo.query(
                and(startsWith("firstName", "Bob"), eq("lastName", "Smith"), between("salary", 190_000, 200_000)));
```
Or if you want to use JDK 8 stream API, this works with it not against it:

```
        int sum = repo.query(eq("lastName", "Smith")).stream().filter(emp -> emp.getSalary()>50_000)
                .mapToInt(b -> b.getSalary())
                .sum();
```

The above would be much faster if the number of employees was quite large. It would narrow down the employees whose name started with Smith and had a salary above 50,000. Let's say you had 100,000 employees and only 50 named Smith so now you narrow to 50 quickly by using the TreeMap which effectively pulls 50 employees out of 100_000, then we do the filter over just 50 instead of the whole 100,000.

There is more work to be done, but the basic model works. 

Creating a new Repo:
```
        /* Create a repo, and decide what to index. */
        RepoBuilder repoBuilder = RepoBuilder.getInstance();

        /* Decide what to index, ssn is primaryKey, firstName, lastName, and salary are indexes. */
        repoBuilder.primaryKey("ssn")
                .searchIndex("firstName").searchIndex("lastName").searchIndex("salary") ;

        /* Create the repo with the builder. */
        Repo <String, Employee> repo
             = repoBuilder.build(String.class, Employee.class);

```

The above sets up a three SearchIndexes that allow searching by those fields quickly (for "less than", "greater than", "startsWith", etc.)

A SearchIndex is a TreeMap more or less.
A LookupIndex is a HashMap more or less.

CRUD: (add, remove, get)
```
        Employee emp = employee("Diana", "Smith", "21785999", "08.15.82", 100_000);
        repo.add(emp);
        assertNotNull(repo.get("21785999"));

        //By default the repo clones objects before it adds them
        assertNotSame(emp, repo.get("21785999"));

        //Remove the object from the repo
        repo.remove(emp);
```

The modify method is a nice method if you want to just edit the index associated with one property:
```
        repo.modify(emp, "firstName", "Di");

        //Verify that the firstName was set.
        String firstName = repo.get("21785999").getFirstName();
        assertEquals("firstName equals", "Di", firstName);
```

The above would change the object without updating every index (if you had a lot of indexes, this would be faster, and also means there is not as much cloning).


The advantage of this over using just the stream API is that if the data set is large, this would be much faster because it would use the indexes (TreeMap).

```
package org.datarepo;
...
...
public class Employee {
    String firstName;
    String lastName;
    String ssn;
    Date birthDate;
    int salary;
...
...

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

```

Everything is configurable. You can swap out factories for Filtering, SearchIndex creations, LookupIndex creation, etc. Thus, if you find a collection library that is faster or does less GC or ..., there is a an easy way to plug-it in.

Also considering writing a backup mode to make backing the data repo to a database or NoSQL database faster. 

https://github.com/RichardHightower/datarepo


Updates:

I added some more features.

```
 repo.updateByFilter(values(value("firstName", "Di")),
                and( eq("firstName", "Diana"),
                        eq("lastName", "Smith"),
                        eq("ssn", "21785999") ) );

```




The above would be equivalent to

```
    UPDATE Employee e
     SET e.firstName='Di'
    WHERE e.firstName = 'Diana'
      and e.lastName = 'Smith'
      and e.ssn = '21785999'
```

This allows you to set multiple fields at once on multiple records
so if you were doing a bulk update.

There are overloaded methods for all basic types so if you have one
value to update on each items returned from a Filter:

```
        repo.updateByFilter("firstName", "Di",
                and( eq("firstName", "Diana"),
                eq("lastName", "Smith"),
                        eq("ssn", "21785999") ) );

```

Here is the full compliment of new methods added to the Repo Interface:

```

public interface Repo <KEY, ITEM> extends Bag <ITEM>{
       ...
       void modify(ITEM item, ValueSetter... values);

       void update(KEY key, String property, Object value);
       void update(KEY key, String property, String value);
       void update(KEY key, String property, int value);
       void update(KEY key, String property, long value);
       void update(KEY key, String property, char value);
       void update(KEY key, String property, short value);
       void update(KEY key, String property, byte value);
       void update(KEY key, String property, float value);
       void update(KEY key, String property, double value);
       void update(KEY key, ValueSetter... values);

       void updateByFilter(String property, Object value, Expression... expressions);
       void updateByFilter(String property, String value, Expression... expressions);
       void updateByFilter(String property, int value, Expression... expressions);
       void updateByFilter(String property, long value, Expression... expressions);
       void updateByFilter(String property, char value, Expression... expressions);
       void updateByFilter(String property, short value, Expression... expressions);
       void updateByFilter(String property, byte value, Expression... expressions);
       void updateByFilter(String property, float value, Expression... expressions);
       void updateByFilter(String property, double value, Expression... expressions);
       void updateByFilter(List<ValueSetter> values, Expression... expressions);

```

Added some basic selection capabilities:

```

        List <Map<String, Object>> list =
           repo.query(selects(select("firstName")), eq("lastName", "Hightower"));

```

You can have as many selects as you like.
You can also bring the list back sorted:

```
        List <Map<String, Object>> list =
           repo.sortedQuery("firstName",selects(select("firstName")), eq("lastName", "Hightower"));

```

You can select properties of related properties (i.e., employee.department.name).

```
        List <Map<String, Object>> list = repo.query(
                selects(select("department", "name")),
                eq("lastName", "Hightower"));

        assertEquals("engineering", list.get(0).get("department.name"));

```

The above would try to use the fields of the classes.
If you want to use the actual properties (emp.getFoo() vs. emp.foo), then
you need to use the selectPropertyPath.

```
        List <Map<String, Object>> list = repo.query(
                selects(selectPropPath("department", "name")),
                eq("lastName", "Hightower"));

```

Note that select("department", "name") is much faster than
selectPropPath("department", "name"), which could matter in a tight loop.

By default all search indexes and lookup indexes allow duplicates (except for primary key index).

```
        repoBuilder.primaryKey("ssn")
                .searchIndex("firstName").searchIndex("lastName")
                .searchIndex("salary").searchIndex("empNum", true)
                .usePropertyForAccess(true);

```

You can override that by providing a true flag as the second argument to searchIndex.
Notice empNum is a searchable unique index.

If you prefer or need, you can get even simple searches back as maps:

```
        List<Map<String, Object>> employees = repo.queryAsMaps(eq("firstName", "Diana"));
        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals("Diana", employees.get(0).get("firstName"));
        System.out.println(employees.get(0).get("department"));

```

Note that the object to map conversion goes deep as in:

```
        System.out.println(employees.get(0).get("department"));
```

Yields:

{class=org.datarepo.Department, name=engineering}

This can be useful for debugging and ad hoc queries for tooling.