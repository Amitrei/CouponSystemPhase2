package com.amitrei.couponsystemv2.rest;


import com.amitrei.couponsystemv2.beans.*;
import com.amitrei.couponsystemv2.exceptions.AlreadyExistsException;
import com.amitrei.couponsystemv2.exceptions.DoesNotExistsException;
import com.amitrei.couponsystemv2.exceptions.IllegalActionException;
import com.amitrei.couponsystemv2.security.ClientType;
import com.amitrei.couponsystemv2.security.LoginManager;
import com.amitrei.couponsystemv2.services.AdminService;
import com.amitrei.couponsystemv2.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("administrator")
@CrossOrigin(origins = "https://couponim-web.herokuapp.com", allowedHeaders = "*")
public class AdminController extends ClientController {


    @Autowired
    private LoginManager loginManager;

    @Autowired
    private AdminService adminService;


    private final ClientType clientType = ClientType.Administrator;

    public ResponseEntity<?> login(String email, String password) {
        return null;
    }


    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {

        try {

            String token = loginManager.restLogin(authRequest.getEmail(), authRequest.getPassword(),
                    ClientType.Administrator);
            return new ResponseEntity<AuthResponse>(new AuthResponse(token), HttpStatus.ACCEPTED);

        } catch (IllegalActionException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("companies/add")
    public ResponseEntity<?> addCompany(@RequestBody Company company) {


        try {

            adminService.addCompany(company);
            return new ResponseEntity<Company>(company, HttpStatus.CREATED);

        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }


    }


    @PutMapping("companies/update")
    public ResponseEntity<?> updateCompany(@RequestBody Company company) {

        try {
            adminService.updateCompany(company);

            return new ResponseEntity<Company>(company, HttpStatus.ACCEPTED);
        } catch (IllegalActionException | DoesNotExistsException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }


    }


    @DeleteMapping("companies/delete/{companyID}")
    public ResponseEntity<?> deleteCompany(@PathVariable int companyID) {

        try {
            Company deletedCompany = adminService.getOneCompany(companyID);
            adminService.deleteCompany(companyID);
            return new ResponseEntity<Company>(deletedCompany, HttpStatus.ACCEPTED);
        } catch (DoesNotExistsException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("companies")
    public ResponseEntity<?> getAllCompanies() {

        return new ResponseEntity<List<Company>>(adminService.getAllCompanies(), HttpStatus.ACCEPTED);

    }


    @GetMapping("companies/{companyID}")

    public ResponseEntity<?> getOneCompany(@PathVariable int companyID) {

        Company company = adminService.getOneCompany(companyID);
        List<Coupon> companyCoupons = company.getCoupons();
        companyCoupons.forEach(coupon -> coupon.setCompanyName(company.getName()));
        company.setCoupons(companyCoupons);
        return new ResponseEntity<Company>(company, HttpStatus.ACCEPTED);

    }

    @GetMapping("companies/by-name/{companyName}")

    public ResponseEntity<?> isCompanyExistsByName(@PathVariable String companyName) {
        if (adminService.companyExistsByName(companyName)) {
            return new ResponseEntity<Boolean>(true, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<Boolean>(false, HttpStatus.ACCEPTED);
        }

    }

    @GetMapping("companies/by-email/{email}")
    public ResponseEntity<?> isCompanyExistsByEmail(@PathVariable String email) {
        if (adminService.companyExistsByEmail(email)) {
            return new ResponseEntity<Boolean>(true, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<Boolean>(false, HttpStatus.ACCEPTED);
        }

    }


    @PostMapping("customers/add")
    public ResponseEntity<?> addCustomer(@RequestBody Customer customer) {

        try {
            adminService.addCustomer(customer);
            return new ResponseEntity<Customer>(customer, HttpStatus.CREATED);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @DeleteMapping("customers/delete/{customerID}")
    public ResponseEntity<?> deleteCustomer(@PathVariable int customerID) {

        adminService.deleteCustomer(customerID);
        return new ResponseEntity<Integer>(customerID, HttpStatus.ACCEPTED);

    }

    @PutMapping("customers/update")
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer) {

        adminService.updateCustomer(customer);
        return new ResponseEntity<Customer>(customer, HttpStatus.ACCEPTED);

    }


    @GetMapping("customers")
    public ResponseEntity<?> getAllCustomers() {
        return new ResponseEntity<List<Customer>>(adminService.getAllCustomers(), HttpStatus.ACCEPTED);

    }

    @GetMapping("customers/by-email/{email}")
    public ResponseEntity<?> customerExistsByEmail(@PathVariable String email) {

        if (adminService.customerExistsByEmail(email))
            return new ResponseEntity<Boolean>(true, HttpStatus.ACCEPTED);

        else
            return new ResponseEntity<Boolean>(false, HttpStatus.ACCEPTED);


    }

    @GetMapping("customers/{customerID}")
    public ResponseEntity<?> getOneCustomer(@PathVariable int customerID) {
        Customer customer = adminService.getOneCustomer(customerID);
        List<Coupon> customerCoupons = customer.getCoupons();
        customerCoupons.forEach(coupon -> coupon.setCompanyName(coupon.getCompany().getName()));
        customer.setCoupons(customerCoupons);
        return new ResponseEntity<Customer>(customer, HttpStatus.ACCEPTED);
    }


    @GetMapping("coupons")
    public ResponseEntity<?> getAllCoupons() {

        List<Coupon> allCoupons = adminService.getAllCoupons();
        allCoupons.forEach(coupon -> coupon.setCompanyName(coupon.getCompany().getName()));
        return new ResponseEntity<List<Coupon>>(allCoupons, HttpStatus.ACCEPTED);
    }

}