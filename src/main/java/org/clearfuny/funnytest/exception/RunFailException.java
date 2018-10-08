package org.clearfuny.funnytest.exception;

public class RunFailException extends Exception {

    private RunFailException(String message, Throwable cause){
        super(message, cause);


    }

    public static RunFailException build(String stepId, String msg, Throwable cause) {
        String message = String.format("[%s]failed: %s", stepId, msg);
        return new RunFailException(message, cause);
    }
}
