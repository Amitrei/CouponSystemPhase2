package com.amitrei.couponsystemv2.rest;


import com.amitrei.couponsystemv2.beans.*;
import com.amitrei.couponsystemv2.exceptions.AlreadyExistsException;
import com.amitrei.couponsystemv2.exceptions.DoesNotExistsException;
import com.amitrei.couponsystemv2.exceptions.IllegalActionException;
import com.amitrei.couponsystemv2.security.ClientType;
import com.amitrei.couponsystemv2.services.AdminService;
import com.amitrei.couponsystemv2.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("administrator")
@CrossOrigin(origins = "https://couponim-web.herokuapp.com", allowedHeaders = "*")
public class AdminController extends ClientController {


    @Autowired
    AdminService adminService;

    @Autowired
    JwtUtil jwtUtill;


    private final ClientType clientType = ClientType.Administrator;

    public ResponseEntity<?> login(String email, String password) {
        return null;
    }


    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {

        try {
            adminService.login(authRequest.getEmail(), authRequest.getPassword());
        } catch (IllegalActionException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<AuthResponse>(new AuthResponse(jwtUtill.generateToken(authRequest.getEmail(), clientType)), HttpStatus.ACCEPTED);
    }


    @PostMapping("companies/add")
    public ResponseEntity<?> addCompany(@RequestHeader(name = "authorization") String token, @RequestBody Company company) {


        if (jwtUtill.validateToken(token, "admin@admin.com", this.clientType)) {

            try {

                adminService.addCompany(company);
                return new ResponseEntity<Company>(company,HttpStatus.CREATED);

            } catch (AlreadyExistsException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

            }
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }


    @PutMapping("companies/update")
    public ResponseEntity<?> updateCompany(@RequestHeader(name = "authorization") String token, @RequestBody Company company) {

        if (jwtUtill.validateToken(token, "admin@admin.com", this.clientType)) {
            try {
                adminService.updateCompany(company);
                return new ResponseEntity<Company>(company, HttpStatus.ACCEPTED);
            } catch (IllegalActionException | DoesNotExistsException e) {
                return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }


    @DeleteMapping("companies/delete/{companyID}")
    public ResponseEntity<?> deleteCompany(@RequestHeader(name = "authorization") String token, @PathVariable int companyID) {
        if (jwtUtill.validateToken(token, "admin@admin.com", this.clientType)) {

            try {
                Company deletedCompany=adminService.getOneCompany(companyID);
                adminService.deleteCompany(companyID);
                return new ResponseEntity<Company>(deletedCompany, HttpStatus.ACCEPTED);
            } catch (DoesNotExistsException e) {
                return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @GetMapping("companies")
    public ResponseEntity<?> getAllCompanies(@RequestHeader(name = "authorization") String token) {

        if (jwtUtill.validateToken(token, "admin@admin.com", this.clientType)) {
            return new ResponseEntity<List<Company>>(adminService.getAllCompanies(), HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


    @GetMapping("companies/{companyID}")

    public ResponseEntity<?> getOneCompany(@RequestHeader(name = "authorization") String token, @PathVariable int companyID) {
        if (jwtUtill.validateToken(token, "admin@admin.com", this.clientType)) {

            Company company = adminService.getOneCompany(companyID);
            List<Coupon> companyCoupons= company.getCoupons();
            companyCoupons.forEach(coupon -> coupon.setCompanyName(company.getName()));
            company.setCoupons(companyCoupons);
            return new ResponseEntity<Company>(company, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @GetMapping("companies/by-name/{companyName}")

    public ResponseEntity<?> isCompanyExistsByName(@RequestHeader(name = "authorization") String token, @PathVariable String companyName) {
        if (jwtUtill.validateToken(token, "admin@admin.com", this.clientType)) {
            if(adminService.companyExistsByName(companyName)) {
                return new ResponseEntity<Boolean>(true, HttpStatus.ACCEPTED);
            }

            else{
                return new ResponseEntity<Boolean>(false, HttpStatus.ACCEPTED);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @GetMapping("companies/by-email/{email}")
    public ResponseEntity<?> isCompanyExistsByEmail(@RequestHeader(name = "authorization") String token, @PathVariable String email) {
        if (jwtUtill.validateToken(token, "admin@admin.com", this.clientType)) {
            if(adminService.companyExistsByEmail(email)) {
                return new ResponseEntity<Boolean>(true, HttpStatus.ACCEPTED);
            }

            else{
                return new ResponseEntity<Boolean>(false, HttpStatus.ACCEPTED);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }



    @PostMapping("customers/add")
    public ResponseEntity<?> addCustomer(@RequestHeader(name = "authorization") String token, @RequestBody Customer customer) {
        if (jwtUtill.validateToken(token, "admin@admin.com", this.clientType)) {

            try {
                adminService.addCustomer(customer);
                return new ResponseEntity<Customer>(customer, HttpStatus.CREATED);
            } catch (AlreadyExistsException e) {
                return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }




    @DeleteMapping("customers/delete/{customerID}")
    public ResponseEntity<?> deleteCustomer(@RequestHeader(name = "authorization") String token, @PathVariable int customerID) {
        if (jwtUtill.validateToken(token, "admin@admin.com", this.clientType)) {

            adminService.deleteCustomer(customerID);
            return new ResponseEntity<Integer>(customerID, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @PutMapping("customers/update")
    public ResponseEntity<?> updateCustomer(@RequestHeader(name = "authorization") String token, @RequestBody Customer customer) {
        if (jwtUtill.validateToken(token, "admin@admin.com", this.clientType)) {

            adminService.updateCustomer(customer);
            return new ResponseEntity<Customer>(customer, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }


    @GetMapping("customers")
    public ResponseEntity<?> getAllCustomers(@RequestHeader(name = "authorization") String token) {
        if (jwtUtill.validateToken(token, "admin@admin.com", this.clientType)) {
            return new ResponseEntity<List<Customer>>(adminService.getAllCustomers(), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }
    @GetMapping("customers/by-email/{email}")
    public ResponseEntity<?> customerExistsByEmail(@RequestHeader(name = "authorization") String token , @PathVariable String email) {
        if (jwtUtill.validateToken(token, "admin@admin.com", this.clientType)) {

            if (adminService.customerExistsByEmail(email))
            return new ResponseEntity<Boolean>(true, HttpStatus.ACCEPTED);

            else
                return new ResponseEntity<Boolean>(false, HttpStatus.ACCEPTED);

        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @GetMapping("customers/{customerID}")
    public ResponseEntity<?> getOneCustomer(@RequestHeader(name = "authorization") String token,@PathVariable int customerID) {
        if (jwtUtill.validateToken(token, "admin@admin.com", this.clientType)) {
            Customer customer = adminService.getOneCustomer(customerID);
            List<Coupon> customerCoupons=customer.getCoupons();
            customerCoupons.forEach(coupon -> coupon.setCompanyName(coupon.getCompany().getName()));
            customer.setCoupons(customerCoupons);
            return new ResponseEntity<Customer>(customer, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


    @GetMapping("coupons")
    public ResponseEntity<?> getAllCoupons(@RequestHeader(name = "authorization") String token) {
        System.out.println(token + "token from header");
        if (jwtUtill.validateToken(token, "admin@admin.com", this.clientType)) {

            List<Coupon> allCoupons = adminService.getAllCoupons();
            allCoupons.forEach(coupon -> coupon.setCompanyName(coupon.getCompany().getName()));
            return new ResponseEntity<List<Coupon>>(allCoupons, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }
}