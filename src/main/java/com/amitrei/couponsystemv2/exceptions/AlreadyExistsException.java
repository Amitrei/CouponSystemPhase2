package com.amitrei.couponsystemv2.exceptions;

public class AlreadyExistsException extends  Exception {

    public AlreadyExistsException(String message) {
        super(message + " Already exists Exception");
    }
}
