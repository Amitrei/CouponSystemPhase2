package com.amitrei.couponsystemv2.Exceptions;

public class DoesNotExistsException  extends  Exception{


    public DoesNotExistsException(String message) {
        super(message + "Does not Exists Exception");
    }
}
