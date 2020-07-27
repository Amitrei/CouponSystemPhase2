package com.amitrei.couponsystemv2.security;


import com.amitrei.couponsystemv2.exceptions.IllegalActionException;
import com.amitrei.couponsystemv2.services.AdminService;
import com.amitrei.couponsystemv2.services.ClientServices;
import com.amitrei.couponsystemv2.services.CompanyService;
import com.amitrei.couponsystemv2.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class LoginManager {


    @Autowired
    private AdminService adminService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CompanyService companyService;


    public ClientServices login(String email, String password, ClientType clientType) throws IllegalActionException {

        switch (clientType) {

            case Administrator:
                if (adminService.login(email, password)) return adminService;
                else {
                    return null;
                }

            case Company:

                if (companyService.login(email, password)) return companyService;
                else {
                    return null;
                }

            case Customer:
                if (customerService.login(email, password)) return customerService;
                else {
                    return null;
                }

            default:
                return null;
        }

    }
}
