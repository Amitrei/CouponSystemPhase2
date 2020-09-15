package com.amitrei.couponsystemv2.rest;


import com.amitrei.couponsystemv2.beans.AuthRequest;
import com.amitrei.couponsystemv2.beans.Company;
import com.amitrei.couponsystemv2.beans.Customer;
import com.amitrei.couponsystemv2.exceptions.AlreadyExistsException;
import com.amitrei.couponsystemv2.exceptions.DoesNotExistsException;
import com.amitrei.couponsystemv2.exceptions.IllegalActionException;
import com.amitrei.couponsystemv2.services.AdminService;
import com.amitrei.couponsystemv2.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class AdminController extends  ClientController {


    @Autowired
    AdminService adminService;

    @Autowired
    JwtUtil jwtUtill;


private final String clientType="admin";

    @PostMapping("login")
    public ResponseEntity<?> login(String email,String password) {

 return null;
    }


    @PostMapping("login2")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {

        try {
            adminService.login(authRequest.getEmail(),authRequest.getPassword());
        } catch (IllegalActionException e) {
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(jwtUtill.generateToken(authRequest.getEmail()),HttpStatus.ACCEPTED);
    }

//
//    @PostMapping("auth")
//    public ResponseEntity<?> auth(@RequestBody AuthRequest authRequest)   {
//
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
//        }
//        catch (Exception e) {
//
//            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
//        }
//
//        return new ResponseEntity<String>(jwtUtill.generateToken(authRequest.getEmail(),authRequest.getType()),HttpStatus.OK);
//    }





    @PostMapping("company/add")
    public ResponseEntity<?> addCompany (@RequestBody Company company) {



        try {
            adminService.addCompany(company);
            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);

        }

    }


    @PutMapping("company/update")
    public ResponseEntity<?> updateCompany(@RequestBody Company company) {
        try {
            adminService.updateCompany(company);
            return  new ResponseEntity<Company>(company,HttpStatus.ACCEPTED);
        } catch (IllegalActionException | DoesNotExistsException e) {
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("company/delete/{companyID}")
    public ResponseEntity<?> deleteCompany(@PathVariable int companyID){
        try {
            adminService.deleteCompany(companyID);
            return new ResponseEntity<Integer>(companyID,HttpStatus.ACCEPTED);
        } catch (DoesNotExistsException e) {
            return  new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("company/all")
    public ResponseEntity<?> getAllCompanies(@RequestHeader(name = "autorization") String token) {
        if(jwtUtill.validateToken(token,"admin@admin.com")) {
            return new ResponseEntity< List<Company>>(adminService.getAllCompanies(), HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>("Sorry no token",HttpStatus.BAD_REQUEST);
    }


@GetMapping("company/{companyID}")
    public ResponseEntity<?> getOneCompany(@PathVariable  int companyID) {
        Company company = adminService.getOneCompany(companyID);
        return new ResponseEntity<Company>(company,HttpStatus.ACCEPTED);
}



@PostMapping("customer/add")
    public ResponseEntity<?> addCustomer(@RequestBody Customer customer) {
    try {
        adminService.addCustomer(customer);
        return new ResponseEntity<Customer>(customer,HttpStatus.CREATED);
    } catch (AlreadyExistsException e) {
        return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }
}


@DeleteMapping("customer/delete/{customerID}")
public ResponseEntity<?> deleteCustomer(@PathVariable int customerID){
        adminService.deleteCustomer(customerID);
        return new ResponseEntity<Integer>(customerID,HttpStatus.ACCEPTED);
}

@PutMapping("customer/update")
public ResponseEntity<?> updateCustomer(Customer customer) {
        adminService.updateCustomer(customer);
        return new ResponseEntity<Customer>(customer,HttpStatus.ACCEPTED);
}
@GetMapping("customer/all")
    public ResponseEntity<?> getAllCustomers() {
        return new ResponseEntity<List<Customer>>(adminService.getAllCustomers(),HttpStatus.ACCEPTED);
}

@GetMapping("customer/{customerID}")
    public ResponseEntity<?> getOneCustomer(@PathVariable  int customerID) {
        Customer customer = adminService.getOneCustomer(customerID);
        return new ResponseEntity<Customer>(customer,HttpStatus.ACCEPTED);
}

}