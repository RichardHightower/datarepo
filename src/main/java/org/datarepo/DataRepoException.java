package org.datarepo;

public class DataRepoException extends RuntimeException {
    public DataRepoException() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public DataRepoException(String message) {
        super(message);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public DataRepoException(String message, Throwable cause) {
        super(message, cause);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public DataRepoException(Throwable cause) {
        super(cause);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected DataRepoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }



}