package com.amitrei.couponsystemv2.rest;

import com.amitrei.couponsystemv2.beans.*;
import com.amitrei.couponsystemv2.exceptions.AlreadyExistsException;
import com.amitrei.couponsystemv2.exceptions.IllegalActionException;
import com.amitrei.couponsystemv2.security.ClientType;
import com.amitrei.couponsystemv2.security.LoginManager;
import com.amitrei.couponsystemv2.security.TokenManager;
import com.amitrei.couponsystemv2.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("company")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")


public class CompanyController extends ClientController {


    @Autowired
    private LoginManager loginManager;
    @Autowired
    private CompanyService companyService;

    @Autowired
    private TokenManager tokenManager;

    @Override
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {

            String token=loginManager.restLogin(authRequest.getEmail(), authRequest.getPassword(),
                    ClientType.Company);
            return new ResponseEntity<AuthResponse>(new AuthResponse(token), HttpStatus.ACCEPTED);

        } catch (IllegalActionException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("coupons/add")
    public ResponseEntity<?> addCoupon(@RequestHeader(name = "authorization") String token, @RequestBody Coupon coupon) {

        if (tokenManager.isTokenValid(token)) {
            Coupon addedCoupon = coupon;
            coupon.setCompany( ((CompanyService) tokenManager.getClientService(token)).companyDetails());
            coupon.setCompanyName(coupon.getCompany().getName());
            try {
                ((CompanyService) tokenManager.getClientService(token)).addCoupon(coupon);
                return new ResponseEntity<Coupon>(coupon, HttpStatus.CREATED);
            } catch (AlreadyExistsException e) {
                return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


    @PutMapping("coupons/update")
    public ResponseEntity<?> updateCoupon(@RequestHeader(name = "authorization") String token, @RequestBody Coupon coupon) {
        if (tokenManager.isTokenValid(token)) {

            System.out.println(coupon);
            try {
                ((CompanyService) tokenManager.getClientService(token)).updateCoupon(coupon);
                return new ResponseEntity<Coupon>(coupon, HttpStatus.CREATED);

            } catch (Exception e) {
                return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("coupons/delete/{couponID}")
    public ResponseEntity<?> deleteCoupon(@RequestHeader(name = "authorization") String token, @PathVariable int couponID) {
        if (tokenManager.isTokenValid(token)) {
            ((CompanyService) tokenManager.getClientService(token)).deleteCoupon(couponID);
            return new ResponseEntity<Integer>(couponID, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


    @GetMapping("coupons")
    public ResponseEntity<?> getCompanyCoupons(@RequestHeader(name = "authorization") String token) {
        if (tokenManager.isTokenValid(token)) {

            List<Coupon> companyCoupons = ((CompanyService) tokenManager.getClientService(token)).getCompanyCoupons();
            companyCoupons.forEach(coupon -> coupon.setCompanyName(coupon.getCompany().getName()));
            companyCoupons.forEach(coupon -> coupon.setIdOfCompany(coupon.getCompany().getId()));


            return new ResponseEntity<List<Coupon>>(companyCoupons, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


    @GetMapping("coupons/by-title/{title}")
    public ResponseEntity<?> isCouponExistsByTitle(@RequestHeader(name = "authorization") String token,@PathVariable String title) {
        if (tokenManager.isTokenValid(token)) {
              return new ResponseEntity<Boolean>(((CompanyService) tokenManager.getClientService(token)).isTitleExists(title), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


    @GetMapping("coupons/by-category/{category}")
    public ResponseEntity<?> getCompanyCoupons(@RequestHeader(name = "authorization") String token, @PathVariable Category category) {
        if (tokenManager.isTokenValid(token))
            return new ResponseEntity<List<Coupon>>(((CompanyService) tokenManager.getClientService(token)).getCompanyCoupons(category), HttpStatus.ACCEPTED);

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


    @GetMapping("coupons/{amount}")
    public ResponseEntity<?> getCompanyCoupons(@RequestHeader(name = "authorization") String token, @PathVariable double amount) {
        if (tokenManager.isTokenValid(token))
            return new ResponseEntity<List<Coupon>>(((CompanyService) tokenManager.getClientService(token)).getCompanyCoupons(amount), HttpStatus.ACCEPTED);


        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("company-details")

    public ResponseEntity<?> getCompanyDetails(@RequestHeader(name = "authorization") String token) {
        if (tokenManager.isTokenValid(token)) {
            return new ResponseEntity<Company>(((CompanyService) tokenManager.getClientService(token)).companyDetails(), HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


}
