package com.amitrei.couponsystemv2.exceptions;

public class IllegalActionException extends Exception{

    public IllegalActionException(String message) {
        super("illegal action Exception caused by: " +message);
    }
}
