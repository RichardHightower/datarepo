package org.datarepo.tests;

import java.util.ArrayList;
import java.util.List;

public class Meta {

    private String name0 = "bar";


    public Meta(String name) {
        this.name0 = name;
    }

    public Meta() {
    }


    List<Meta2> metas2 = new ArrayList<>();
    {
        metas2.add(new Meta2("2tag1"));
        metas2.add(new Meta2("2tag2"));
        metas2.add(new Meta2("2tag3"));

    }

}