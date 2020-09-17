package com.amitrei.couponsystemv2.security;


import com.amitrei.couponsystemv2.exceptions.IllegalActionException;
import com.amitrei.couponsystemv2.services.AdminService;
import com.amitrei.couponsystemv2.services.ClientServices;
import com.amitrei.couponsystemv2.services.CompanyService;
import com.amitrei.couponsystemv2.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class LoginManager {


    @Autowired
    private AdminService adminService;

    @Autowired
    private ApplicationContext ctx;

    @Autowired
    private CustomerService blankCustomerService;

    @Autowired
    private CompanyService blankCompanyService;

    @Autowired
    private TokenManager tokenManager;

    public ClientServices login(String email, String password, ClientType clientType) throws IllegalActionException {

        switch (clientType) {

            case Administrator:
                if (adminService.login(email, password)) return adminService;
                    return null;


            case Company:

                if (blankCompanyService.login(email, password)) return blankCompanyService;
                    return null;


            case Customer:
                if (blankCustomerService.login(email, password)) return blankCustomerService;
                    return null;



            default: return null;


        }

    }

    public String restLogin(String email, String password, ClientType clientType) throws IllegalActionException {

        switch (clientType) {

            case Administrator:
                if (adminService.login(email, password))  return tokenManager.generateToken(email,ClientType.Administrator,adminService);


                return "badLogin ";


            case Company:
                CompanyService companyService = ctx.getBean(CompanyService.class);
                if (companyService.login(email, password)) return tokenManager.generateToken(email,ClientType.Company,companyService);
                return "badLogin";


            case Customer:
                CustomerService customerService = ctx.getBean(CustomerService.class);
                if (customerService.login(email, password)) return tokenManager.generateToken(email,ClientType.Customer,customerService);
                return "badLogin";



            default: return null;


        }

    }
}
