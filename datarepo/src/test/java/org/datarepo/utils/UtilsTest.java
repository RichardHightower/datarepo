package org.datarepo.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilsTest {

    @Test
    public void testCamel() throws Exception {
        String myFoo = "MY FOO_BAR_FUN\t_STUFF";

        String camelCaseUpper = Utils.camelCaseUpper(myFoo);
        assertEquals("MyFooBarFunStuff", camelCaseUpper);

        String camelCaseLower = Utils.camelCaseLower(myFoo);
        assertEquals("myFooBarFunStuff", camelCaseLower);

    }

    @Test
    public void testUnderBarCase() throws Exception {
        String myFoo = "FooFunFaceFact";

        String underBar = Utils.underBarCase(myFoo);
        assertEquals("FOO_FUN_FACE_FACT", underBar);

    }

    @Test
    public void testUnderBarCase2() throws Exception {
        String myFoo = "FooFunFaceFact Fire Free FOO foo\tbar";

        String underBar = Utils.underBarCase(myFoo);
        assertEquals("FOO_FUN_FACE_FACT_FIRE_FREE_FOO_FOO_BAR", underBar);

    }

}
