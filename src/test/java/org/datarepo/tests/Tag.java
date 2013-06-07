package org.datarepo.tests;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rick
 * Date: 5/22/13
 * Time: 11:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class Tag {
    private String name = "bar";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tag(String name) {
        this.name = name;
    }

    public Tag() {
    }

    List<Meta> metas = new ArrayList<>();

    {
        metas.add(new Meta("mtag1"));
        metas.add(new Meta("mtag2"));
        metas.add(new Meta("mtag3"));

    }

}
