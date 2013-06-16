datarepo
========

Data Repo


DataRepo allows you to treat Java collections more like a database.
DataRepo is not an in memory database, and cannot substitute arranging
your objects into data structures optimized for your application.

If you want to spend your time providing customer value and building your
objects and classes and using the Collections API for your data structures,
then DataRepo is meant for you. This does not preclude breaking out the Knuth
books and coming up with an optimized data structure. It just helps keep the
mundane things easy and so you can spend your time making the hard things
possible.

This project came out of a need. I was working on a project that planned
to store large collection of domain objects in memory for speed, and somebody
asked an all to important question that I overlooked. How are we going to query this
data. My answer was we will use the Collections API and the Streaming API.
Then I tried to do this...

I tired using the JDK 8 stream API on a large data set, and it was slow.
It was a linear search/filter. This is by design, but for
what I was doing, it did not work. I needed indexes to support arbitrary
queries.

DataRepo augments the streaming API.
DataRepo does not endeavor to replace the JDK 8 stream API, and in fact it works
well with it. DataRepo allows you to create indexed collections. The indexes
can be anything (it is pluggable). At this moment in time, the indexes are
based on ConcurrentHashMap and ConcurrentSkipListMap. By design, DataRepo works
with standard collection libraries. There is no plan to create a set of custom
collections. One should be able to plug in Guava, Concurrent Trees or Trove
if one desires to do so.


DataRepo makes doing index based queries on collections a lot easier.
It provides a simplified API for doing so. It allows linear search for a sense
of completion but I recommend using it primarily for using indexes and then using
the streaming API for the rest (for type safety and speed).

You can use a wrapper class to wrap a collection into a indexed collection.

Let's say you have a method that creates 200,000 employee objects like this:
```
        List<Employee> employees = TestHelper.createMetricTonOfEmployees(200_000);

```

So now we have 200,000 employees. Let's search them...

First wrap Employees in a searchable query:

```
        employees = $q(employees);

```
Now search:

```
        List<Employee> results = query(employees, eq("firstName", firstName));
```

So what is the main difference between the above and the stream API?

```
        employees.stream().filter(emp -> emp.getFirstName().equals(firstName)
```

(To unwrap a collection you use $c. $q is short for querable collection, and
$c is short for give me a plain colleciton. $c gives you back your orginal collection.)

About a factor of 20,000% faster to use DataRepo!

There is an API that looks just like your built in collections.
There is also an API that looks more like a DAO object or a Repo Object.

A simple query with the Repo/DAO object looks like this:

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

There are also NestedKeySearchIndex, TypeHierarchyIndex, UniqueSearchIndex,
UniqueLookupIndex, SearchIndexDefault, LookupIndexDefault and a few more planned.

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

The above would change the object without updating every index.
If you had a lot of indexes, this would be faster.


The advantage of this over using just the stream API is that if
the data set is large, this would be much faster because it would use
the indexes (TreeMap more or less).

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

Everything is configurable. You can swap out factories for Filtering,
SearchIndex creations, LookupIndex creation, etc. Thus, if you find
a collection library that is faster or does less GC or ...,
there is a an easy way to plug-it in.

Also considering writing a backup mode to make backing the data repo
to a database or NoSQL database faster.

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



Here is some basic selection capabilities:

```

        List <Map<String, Object>> list =
           repo.query(selects(select("firstName")), eq("lastName", "Hightower"));

```

You can have as many selects as you like.
You can also bring the list back sorted:

```
        List <Map<String, Object>> list =
           repo.sortedQuery("firstName",selects(select("firstName")),
             eq("lastName", "Hightower"));

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

By default all search indexes and lookup indexes allow duplicates
(except for primary key index).

```
        repoBuilder.primaryKey("ssn")
                .searchIndex("firstName").searchIndex("lastName")
                .searchIndex("salary").searchIndex("empNum", true)
                .usePropertyForAccess(true);

```

You can override that by providing a true flag as the second argument to
searchIndex.

Notice empNum is a searchable unique index.

If you prefer or need, you can get even simple searches back as maps:

```
        List<Map<String, Object>> employees = repo.queryAsMaps(eq("firstName", "Diana"));
        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals("Diana", employees.get(0).get("firstName"));
        System.out.println(employees.get(0).get("department"));

```
I am not sure if this is a feature or a misfeature. My thought was that once
you are dealing with data, you need to present that data in a way that does
not ties consumers of data to your actual API. Having a Map of String / basic
types seems to be a way to achieve this.

Note that the object to map conversion goes deep as in:

```
        System.out.println(employees.get(0).get("department"));
```

Yields:

{class=Department, name=engineering}

This can be useful for debugging and ad hoc queries for tooling.
I am considering adding support to easily convert to a JSON string.


Added the ability to query collection properties.
This should work with collections and arrays as deeply nested as you like.
```
        List <Map<String, Object>> list = repo.query(
                selects(select("tags", "metas", "metas2", "metas3", "name3")),
                eq("lastName", "Hightower"));

        print("list", list);

        assertEquals("3tag1", idx(list.get(0).get("tags.metas.metas2.metas3.name3"), 0));

```

The print out of the above looks like this:

```
list [{tags.metas.metas2.metas3.name3=[3tag1, 3tag2, 3tag3,
3tag1, 3tag2, 3tag3, 3tag1, 3tag2, 3tag3, 3tag1, 3tag2, 3tag3,
3tag1, 3tag2, 3tag3, 3tag1, 3tag2, 3tag3, 3tag1, 3tag2, 3tag3,
3tag1, 3tag2, 3tag3, 3tag1, 3tag2, 3tag3, 3tag1, 3tag2, 3tag3,
3tag1, 3tag2, 3tag3, 3tag1, 3tag2, 3tag3, 3tag1, 3tag2, 3tag3,
3tag1, 3tag2, 3tag3, 3tag1, 3tag2, 3tag3, 3tag1, 3tag2, 3tag3,
3tag1, 3tag2, 3tag3, 3tag1, 3tag2, 3tag3, 3tag1, 3tag2, 3tag3,
3tag1, 3tag2, 3tag3, 3tag1, 3tag2, 3tag3, 3tag1, 3tag2, 3tag3,
3tag1, 3tag2, 3tag3, 3tag1, 3tag2, 3tag3, 3tag1, 3tag2, 3tag3,
3tag1, 3tag2, 3tag3, 3tag1, 3tag2, 3tag3]},
...
```

I created several relationship classes to test this:

```
public class Employee {
    List <Tag> tags = new ArrayList<>();
    {
        tags.add(new Tag("tag1"));
        tags.add(new Tag("tag2"));
        tags.add(new Tag("tag3"));

    }
...
public class Tag {
...
    List<Meta> metas = new ArrayList<>();
    {
        metas.add(new Meta("mtag1"));
        metas.add(new Meta("mtag2"));
        metas.add(new Meta("mtag3"));

    }

}
public class Meta {
...
       List<Meta2> metas2 = new ArrayList<>();
       {
           metas2.add(new Meta2("2tag1"));
           metas2.add(new Meta2("2tag2"));
           metas2.add(new Meta2("2tag3"));

       }

   }

...
public class Meta2 {



    List<Meta3> metas3 = new ArrayList<>();
    {
        metas3.add(new Meta3("3tag1"));
        metas3.add(new Meta3("3tag2"));
        metas3.add(new Meta3("3tag3"));

    }
public class Meta3 {

...

```

You can also search by type:

```
        List<Employee> results = sortedQuery(queryableList, "firstName", typeOf("SalesEmployee"));

        assertEquals(1, results.size());
        assertEquals("SalesEmployee", results.get(0).getClass().getSimpleName());
```

The above finds all employees with the simple classname of SalesEmployee. It also works with full class name as in:

```
        List<Employee> results = sortedQuery(queryableList, "firstName", typeOf("SalesEmployee"));

        assertEquals(1, results.size());
        assertEquals("SalesEmployee", results.get(0).getClass().getSimpleName());
```

You can search by the actual class too:

```
        List<Employee> results = sortedQuery(queryableList, "firstName", instanceOf(SalesEmployee.class));

        assertEquals(1, results.size());
        assertEquals("SalesEmployee", results.get(0).getClass().getSimpleName());

```

You can also query classes that implement certain interfaces:

```
        List<Employee> results = sortedQuery(queryableList, "firstName", implementsInterface(Comparable.class));

        assertEquals(1, results.size());
        assertEquals("SalesEmployee", results.get(0).getClass().getSimpleName());

```

You can also index nested fields/properties and they can be collection fields or property non collection fields as deeply nested as you would like:


```
        /* Create a repo, and decide what to index. */
        RepoBuilder repoBuilder = RepoBuilder.getInstance();

        /* Look at the nestedIndex. */
        repoBuilder.primaryKey("id")
                .searchIndex("firstName").searchIndex("lastName")
                .searchIndex("salary").uniqueSearchIndex("empNum")
                .nestedIndex("tags", "metas", "metas2", "name2");


```

Later you can use the nestedIndex to search.

```
        List<Map<String, Object>> list = repo.query(
                selects(select("tags", "metas", "metas2", "name2")),
                eqNested("2tag1", "tags", "metas", "metas2", "name2"));

```

The safe way to use the nestedIndex is to use eqNested. You can use eq, gt, gte, etc. if you have the index like so:

```
        List<Map<String, Object>> list = repo.query(
                selects(select("tags", "metas", "metas2", "name2")),
                eq("tags.metas.metas2.name2", "2tag1"));

```


You can also add support for subclasses

```
        List<Employee> queryableList = $q(h_list, Employee.class, SalesEmployee.class, HourlyEmployee.class);
        List<Employee> results = sortedQuery(queryableList, "firstName", eq("commissionRate", 1));
        assertEquals(1, results.size());
        assertEquals("SalesEmployee", results.get(0).getClass().getSimpleName());

        results = sortedQuery(queryableList, "firstName", eq("weeklyHours", 40));
        assertEquals(1, results.size());
        assertEquals("HourlyEmployee", results.get(0).getClass().getSimpleName());

```

The data repo has a similar feature in its DataRepoBuilder.build(...) method for specifying subclasses.
This allows you to seemless query fields form subclasses and classes in the same repo or searchable collection.

Why not just use JFilter or CQEngine or ...?

Mostly because of different goals. This DataRepo collection framework is aimed at
generalists. Using cqengine (https://code.google.com/p/cqengine/) did not
seem plausible to me because of its use of Attributes. I wanted an idiomatic
Java approach. I originally dismissed cqengine out right. I came back to it
when I was looking for ways to support endsWith and contains queries. I may
end up using Concurrent Trees
(https://code.google.com/p/concurrent-trees/)
which is written by the same author as cqengine. I may also use Quanitization
like cqengine does.

After implementing a working query plan engine, I became very interested in the
way cqengine did its query plan engine, and I was impressed with the overall design.
Cqengine is an inspiration for DataRepo.

Some may think that the query DSL that DataRepo comes with looks a lot like
cqengine. I'd like to point them to Crank which was based on Presto, which
originated in 2003 and the query DSL was mostly written by me. :)

Please compare this Crank DAO DSL example to DataRepo.

```

 employees = genericDao.find(
                                  eq("department.name", "Engineering"),
                                  or( startsLike("firstName", "Rick"),like("firstName", "Ri"))
                                );

        employees = genericDao.find(
                                or (
                                      eq("department.name", "Engineering"),
                                      like("firstName", "Ri")
                                )
                     );

        employees = genericDao.find( in("age", 1, 2, 3, 4, 5, 6, 40) );

```

This reminds me, I need to look into http://www.querydsl.com/.
Also, I need to maybe create a Query By Example like Crank had.
(I am not into to code generation or byte code manipulation.)

Another key difference between cqengine and DataRepo is DataRepo embraces
optimized reflection and supports type safe access while cqengine
supports reflection and promotes a type safe access.

Also DataRepo intends to focus on making querying dates easier, and reducing
the footprint of String in memory. There are a lot of good ideas in cqengine that
DataRepo plans on adopting variants of the following: ResultSet, Quanitiziation, RadixTree,
and InverseRadixTree indexes come to mind.

DataRepo has different features already.
Pagination support is part of the plan. There is a query/node visitor API with
an eye to Map/Reduce.
DataRepo has support for querying objects based on types and interfaces.
Later this will be expanded querying objects based on annotations.
DataRepo has support for updating objects in mass. DataRepo has support for modifying just
one property of an object and therefore only updating one index.
It supports viewing objects as list of maps instead of objects, and I plan on
supporting auto JSON string. In the end, I view this as a data view on your
objects, and I want that data accessible in non API specific formats.
DataRepo has support for compound indexes and nested indexes as does cqengine, but
I feel DataRepo does it in a more natural way.

DataRepo goes through a ton of trouble to reduce the number of wrapper objects
that need to be created to almost nothing, and cqengine does not. I think that
DataRepo will be easier on the JVM GC than cqengine will.

I forsee (long term) integrating DataRepo with JCache and Lucene.
Also I forsee allowing DataRepo to be backed up to a database easily,
and to a plain old file system
readily. (MySQL, Hessian, Object Serializaiton and MongoDB). I can also see
Far into the future, DataRepo could become a a simplified CRUD library
on top of MongoDB and MySQL like Crank was.
DataRepo would never endeavor to be as flexible as Hibernate or JPA,  but
rather enforce a "this is the way you map Java objects to the RDBMS"
or rather don't worry about it, it will just work approach.  Lack of choice
can be debilitating, but it can also be empowering if you just need to get
something off the ground quickly for a greenfield app. The answer to
"Can I use this custom RDBMS mapping?" will always be use Hibernate or JPA.
Persistence is not a near term project goal. I think there are more pressing
problems to solve. You can use what you use today to do persistence and caching,
and use DataRepo to augment what you have.

Also I want the ability to support joins across DataRepos so you can join
Department to Employee without duplicating the object in two repos, yet
the final product that people see is always coherent.

I want a beefed up in memory cache probably pluggable but based on
Guava LRU cache. (https://code.google.com/p/guava-libraries/wiki/CachesExplained).

I may also want the ability to do queries that look more like SQL,
and also support queries that look more like MongoDB like JFilter.
These are far into the future features.

I view DataRepo as a long term project that I look forward to
evolving and growing over the years.

One jar to rule them all. I plan on DataRepo being one jar file.
So if I decide to use RadixTree or Guava, I will likely fork them and put them
under org.datarepo. Example: org.datarepo.borrowed.com.google.common.cache.Cache.java. I will
also strip them of any features that I don't plan on using. My goal is for DataRepo
to be one single, well tested library not a library that depends on five libraries
that depend on five libraries, ad infintum.  I view caching and RadixTree as
core components of this effort. I also might end up writing my own LRU cache instead
or my own RadixTree. I do enjoy programming. :)

Where will the focus be?
Over the rest of this year the focus is going to be on getting a minimum viable
API. Date handling, joins, String compression, projections/stats and caching are
on the top of my list.

I want a rock solid code coverage but will forgo that until I think I have a decent API.

Then some articles explaining the project with benchmarks.

Then Radix trees for endsWith and contain searches.

Then Lucene integration. I do not plan on embedding Lucene classes.
This will be a separate library / plugin.

Then object persistence (start with the Redis model of every n seconds),
and maybe evolve into more reliable or just delegate more reliable stuff to
MySQL/NoSQL/MongoDB.

That is it. That is the vision.

I care less about the persistence and more about the API and searching.
I will explain this focus more later. I have run into a common practice, and
I think DataRepo can scratch an itch. I forsee DataRepo as being a very mainstream
solution. I see it fitting well with existing solutions that people use for caching
query results  (Memcache, ehcache, data grids). I forsee it as a solution
to reducing cache coherency issues.
