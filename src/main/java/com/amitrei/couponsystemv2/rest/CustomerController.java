package com.amitrei.couponsystemv2.rest;


import com.amitrei.couponsystemv2.beans.*;
import com.amitrei.couponsystemv2.exceptions.IllegalActionException;
import com.amitrei.couponsystemv2.security.ClientType;
import com.amitrei.couponsystemv2.security.LoginManager;
import com.amitrei.couponsystemv2.security.TokenManager;
import com.amitrei.couponsystemv2.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customer")
@CrossOrigin(origins = "https://couponim-web.herokuapp.com", allowedHeaders = "*")
public class CustomerController extends ClientController {


    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private LoginManager loginManager;


    @PostMapping("login")
    @Override
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            String token = loginManager.restLogin(authRequest.getEmail(), authRequest.getPassword(),
                    ClientType.Customer);
            return new ResponseEntity<AuthResponse>(new AuthResponse(token), HttpStatus.ACCEPTED);

        } catch (IllegalActionException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("purchase-coupon")
    public ResponseEntity<?> purchaseCoupon(@RequestHeader(name = "authorization") String token, @RequestBody Coupon coupon) {
        if (tokenManager.isTokenValid(token)) {
            try {
                ((CustomerService) tokenManager.getClientService(token)).purchaseCoupon(coupon);
                System.out.println(((CustomerService) tokenManager.getClientService(token)).getCustomerCoupons());

                return new ResponseEntity<Coupon>(coupon, HttpStatus.ACCEPTED);
            } catch (IllegalActionException e) {
                return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }


        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("coupons")
    public ResponseEntity<?> getCustomerCoupons(@RequestHeader(name = "authorization") String token) {
        if (tokenManager.isTokenValid(token)) {
            List<Coupon> customerCoupons = ((CustomerService) tokenManager.getClientService(token)).getCustomerCoupons();
            customerCoupons.forEach(coupon -> coupon.setCompanyName(coupon.getCompany().getName()));
            customerCoupons.forEach(coupon -> coupon.setIdOfCompany(coupon.getCompany().getId()));
            System.out.println(customerCoupons);
            return new ResponseEntity<List<Coupon>>(customerCoupons, HttpStatus.ACCEPTED);

        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


    @GetMapping("coupons/is-owned/{couponID}")
    public ResponseEntity<?> isCouponOwned(@RequestHeader(name = "authorization") String token,@PathVariable int couponID) {
        if (tokenManager.isTokenValid(token)) {
            return new ResponseEntity<Boolean>(((CustomerService) tokenManager.getClientService(token)).isOwnCoupon(couponID), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }



    @GetMapping("coupons-by-category/{category}")

    public ResponseEntity<?> getCustomerCoupons(@RequestHeader(name = "authorization") String token, @PathVariable  Category category) {
        if (tokenManager.isTokenValid(token)) {
            List<Coupon> customerCoupons = ((CustomerService) tokenManager.getClientService(token)).getCustomerCoupons(category);
            return new ResponseEntity<List<Coupon>>(customerCoupons, HttpStatus.ACCEPTED);

        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }



    @GetMapping("coupons/{maxPrice}")
    public ResponseEntity<?> getCustomerCoupons(@RequestHeader(name = "authorization") String token, @PathVariable  Double maxPrice) {
        if (tokenManager.isTokenValid(token)) {
            List<Coupon> customerCoupons = ((CustomerService) tokenManager.getClientService(token)).getCustomerCoupons(maxPrice);
            return new ResponseEntity<List<Coupon>>(customerCoupons, HttpStatus.ACCEPTED);

        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("details")
    public ResponseEntity<?> getCustomerDetails(@RequestHeader(name = "authorization") String token) {
        if (tokenManager.isTokenValid(token)) {
            Customer currentCustomer = ((CustomerService) tokenManager.getClientService(token)).getCustomerDetails();
            return new ResponseEntity<Customer>(currentCustomer, HttpStatus.ACCEPTED);

        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
