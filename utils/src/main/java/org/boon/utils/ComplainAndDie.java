package org.boon.utils;

public class ComplainAndDie {



    public static void complain(String msg, Object... args) {
        throw new UnsupportedOperationException(String.format(msg, args));
    }



    @SuppressWarnings("serial")
    public static class AssertionException extends RuntimeException {

        public AssertionException() {
            super();
        }

        public AssertionException(String message, Throwable cause) {
            super(message, cause);
        }

        public AssertionException(String message) {
            super(message);
        }

        public AssertionException(Throwable cause) {
            super(cause);
        }
    }



    public static void die(Exception ex) {
            throw new AssertionException(ex);
    }

    public static void die(boolean condition, String message) {
        if (condition) {
            throw new AssertionException(message);
        }
    }

    public static boolean die(String message) {
        throw new AssertionException(message);
    }

    public static void die(String message, Object... args) {
        throw new AssertionException(String.format(message, args));
    }

    public static void die(Throwable t, String message, Object... args) {
        throw new AssertionException(String.format(message, args), t);
    }



    public static void handleUnexpectedException(Exception ex) {
        die(ex);
    }

    public static void handleUnexpectedException(String msg, Exception ex) {
        throw new AssertionException(msg, ex);
    }
}
