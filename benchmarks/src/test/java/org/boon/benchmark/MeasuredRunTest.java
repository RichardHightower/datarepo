package org.boon.benchmark;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MeasuredRunTest {

    Map<String, List<MeasuredRun>> results = new ConcurrentHashMap<>();
    int foo;

    @Before
    public void setUp() throws Exception {
        results = new ConcurrentHashMap<>();
    }


    @Test
    public void testName() throws Exception {

//
//        Runnable runner = new Runnable() {
//            @Override
//            public void run() {
//                for (int index = 0; index < 10; index++) {
//                    MeasuredRun run = new MeasuredRun("testRun", 10, 100, results) {
//
//                        int index = 0;
//                        @Override
//                        protected void init() {
//                            index = 0;
//                        }
//
//                        @Override
//                        protected void test() {
//                            for (; index < 1_000_000; index++) {
//                                foo = index + index;
//                                //foo = foo + index % 100;
//                            }
//
//                        }
//                    };
//            }
//        List<MeasuredRun> runResults = results.get("testRun");
//        System.out.println(runResults.get(0).time());
//        System.out.println(runResults);
//        System.out.println(foo);


    }


}

