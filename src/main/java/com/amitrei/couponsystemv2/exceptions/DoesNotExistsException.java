package com.amitrei.couponsystemv2.exceptions;

import com.amitrei.couponsystemv2.clr.Templates;
import org.springframework.beans.factory.annotation.Autowired;

public class DoesNotExistsException  extends  Exception{



    public DoesNotExistsException(String message) {
        super(message + "Does not Exists Exception");
    }
}
