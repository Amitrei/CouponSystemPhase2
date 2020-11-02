package com.amitrei.couponsystemv2.exceptions;

import com.amitrei.couponsystemv2.clr.Templates;
import org.springframework.beans.factory.annotation.Autowired;

public class IllegalActionException extends Exception{


    public IllegalActionException(String message) {
        super(message);
    }
}
