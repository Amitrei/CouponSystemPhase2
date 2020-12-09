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

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("company")
@CrossOrigin(origins = "https://couponim-web.herokuapp.com", allowedHeaders = "*")


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

            String token = loginManager.restLogin(authRequest.getEmail(), authRequest.getPassword(),
                    ClientType.Company);
            return new ResponseEntity<AuthResponse>(new AuthResponse(token), HttpStatus.ACCEPTED);

        } catch (IllegalActionException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("coupons/add")
    public ResponseEntity<?> addCoupon(@RequestBody Coupon coupon) {

        Coupon addedCoupon = coupon;
        coupon.setCompany(((CompanyService) tokenManager.getClientService(getToken())).companyDetails());
        coupon.setCompanyName(coupon.getCompany().getName());
        try {
            ((CompanyService) tokenManager.getClientService(getToken())).addCoupon(coupon);
            return new ResponseEntity<Coupon>(coupon, HttpStatus.CREATED);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @PutMapping("coupons/update")
    public ResponseEntity<?> updateCoupon(@RequestBody Coupon coupon) {

        System.out.println(coupon);
        try {
            ((CompanyService) tokenManager.getClientService(getToken())).updateCoupon(coupon);
            return new ResponseEntity<Coupon>(coupon, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("coupons/delete/{couponID}")
    public ResponseEntity<?> deleteCoupon(@PathVariable int couponID) {
        ((CompanyService) tokenManager.getClientService(getToken())).deleteCoupon(couponID);
        return new ResponseEntity<Integer>(couponID, HttpStatus.ACCEPTED);
    }


    @GetMapping("coupons")
    public ResponseEntity<?> getCompanyCoupons() {

        List<Coupon> companyCoupons = ((CompanyService) tokenManager.getClientService(getToken())).getCompanyCoupons();
        companyCoupons.forEach(coupon -> coupon.setCompanyName(coupon.getCompany().getName()));
        companyCoupons.forEach(coupon -> coupon.setIdOfCompany(coupon.getCompany().getId()));


        return new ResponseEntity<List<Coupon>>(companyCoupons, HttpStatus.ACCEPTED);
    }


    @GetMapping("coupons/by-title/{title}")
    public ResponseEntity<?> isCouponExistsByTitle(@PathVariable String title) {
        return new ResponseEntity<Boolean>(((CompanyService) tokenManager.getClientService(getToken())).isTitleExists(title), HttpStatus.ACCEPTED);
    }

    @GetMapping("coupons/total-purchases")
    public ResponseEntity<?> getTotalPurchases() {
        return new ResponseEntity<Integer>(((CompanyService) tokenManager.getClientService(getToken())).getTotalPurchases(), HttpStatus.ACCEPTED);
    }


    @GetMapping("coupons/by-category/{category}")
    public ResponseEntity<?> getCompanyCoupons(@PathVariable Category category) {
        return new ResponseEntity<List<Coupon>>(((CompanyService) tokenManager.getClientService(getToken())).getCompanyCoupons(category), HttpStatus.ACCEPTED);

    }


    @GetMapping("coupons/{amount}")
    public ResponseEntity<?> getCompanyCoupons(@PathVariable double amount) {
        return new ResponseEntity<List<Coupon>>(((CompanyService) tokenManager.getClientService(getToken())).getCompanyCoupons(amount), HttpStatus.ACCEPTED);


    }

    @GetMapping("company-details")

    public ResponseEntity<?> getCompanyDetails() {
        return new ResponseEntity<Company>(((CompanyService) tokenManager.getClientService(getToken())).companyDetails(), HttpStatus.ACCEPTED);

    }


}
