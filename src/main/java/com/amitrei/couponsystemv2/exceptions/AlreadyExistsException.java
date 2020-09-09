package com.amitrei.couponsystemv2.exceptions;

import com.amitrei.couponsystemv2.clr.Templates;
import org.springframework.beans.factory.annotation.Autowired;

public class AlreadyExistsException extends  Exception {

    public AlreadyExistsException(String message) {
        super(message + " Already exists Exception");
    }
}
