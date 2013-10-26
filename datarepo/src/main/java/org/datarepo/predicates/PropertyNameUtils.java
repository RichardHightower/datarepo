package org.datarepo.predicates;

import org.datarepo.utils.Utils;

public class PropertyNameUtils {


    public static Function<String, String> underBarCase = new Function<String, String>() {
        @Override
        public String apply(String in) {
            return Utils.underBarCase(in);
        }
    };
    public static Function<String, String> camelCase = new Function<String, String>() {
        @Override
        public String apply(String in) {
            return Utils.camelCase(in);
        }
    };


    public static Function<String, String> camelCaseUpper = new Function<String, String>() {
        @Override
        public String apply(String in) {
            return Utils.camelCaseUpper(in);
        }
    };


    public static Function<String, String> camelCaseLower = new Function<String, String>() {
        @Override
        public String apply(String in) {
            return Utils.camelCaseLower(in);
        }
    };

    public static Function<String, String> upperCase = new Function<String, String>() {
        @Override
        public String apply(String in) {
            return in.toUpperCase();
        }
    };

    public static Function<String, String> lowerCase = new Function<String, String>() {
        @Override
        public String apply(String in) {
            return in.toLowerCase();
        }
    };

}
