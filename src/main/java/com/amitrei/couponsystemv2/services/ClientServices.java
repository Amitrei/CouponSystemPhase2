package com.amitrei.couponsystemv2.services;

import com.amitrei.couponsystemv2.exceptions.IllegalActionException;
import com.amitrei.couponsystemv2.repositories.CompanyRepo;
import com.amitrei.couponsystemv2.repositories.CouponRepo;
import com.amitrei.couponsystemv2.repositories.CustomerRepo;
import com.amitrei.couponsystemv2.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;



public abstract class ClientServices {

    @Autowired
    protected CustomerRepo customerRepo;

    @Autowired
    protected CouponRepo couponRepo;

    @Autowired
    protected CompanyRepo companyRepo;

    @Autowired
    protected DateUtil dateUtil;


    public abstract boolean login(String email, String password) throws IllegalActionException;
}
