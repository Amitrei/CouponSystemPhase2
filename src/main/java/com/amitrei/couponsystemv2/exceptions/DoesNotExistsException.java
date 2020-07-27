package com.amitrei.couponsystemv2.exceptions;

public class DoesNotExistsException  extends  Exception{


    public DoesNotExistsException(String message) {
        super(message + "Does not Exists Exception");
    }
}
