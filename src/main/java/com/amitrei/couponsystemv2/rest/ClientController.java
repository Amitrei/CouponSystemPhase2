package com.amitrei.couponsystemv2.rest;

import com.amitrei.couponsystemv2.beans.AuthRequest;
import com.amitrei.couponsystemv2.services.AdminService;
import com.amitrei.couponsystemv2.services.CompanyService;
import com.amitrei.couponsystemv2.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public abstract class ClientController {

    @Autowired
    private HttpServletRequest httpServletRequest;

    protected String getToken() {
        return httpServletRequest.getHeader("authorization");

    }


    public abstract ResponseEntity<?> login(AuthRequest authRequest);

}
