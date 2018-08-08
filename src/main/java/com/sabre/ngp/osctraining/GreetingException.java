package com.sabre.ngp.osctraining;

public class GreetingException extends Exception {
    public GreetingException(String s, Exception e) {
        super(s, e);
    }

    public GreetingException(String s) {
        super(s);
    }
}
