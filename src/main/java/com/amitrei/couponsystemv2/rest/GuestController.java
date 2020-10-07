package com.amitrei.couponsystemv2.rest;


import com.amitrei.couponsystemv2.beans.Coupon;
import com.amitrei.couponsystemv2.beans.Customer;
import com.amitrei.couponsystemv2.repositories.CouponRepo;
import com.amitrei.couponsystemv2.security.LoginManager;
import com.amitrei.couponsystemv2.security.TokenManager;
import com.amitrei.couponsystemv2.services.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("guest")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class GuestController {

    @Autowired
    private LoginManager loginManager;

    @Autowired
    private GuestService guestService;

    @Autowired
    private TokenManager tokenManager;

    @GetMapping("coupons")
    public ResponseEntity<?> allCoupons() {
        List<Coupon> myList = guestService.getAllCoupons();
        myList.forEach(coupon -> coupon.setCompanyName(coupon.getCompany().getName()));
        myList.forEach(coupon -> coupon.setIdOfCompany(coupon.getCompany().getId()));
        myList.removeIf(coupon -> coupon.getAmount()<=0);

        return new ResponseEntity<>(myList, HttpStatus.OK);
    }

    @GetMapping("coupon/{couponID}")
    public ResponseEntity<?> getCoupon(@PathVariable int couponID) {
        Coupon coupon = guestService.getSingleCoupon(couponID);
        coupon.setCompanyName(coupon.getCompany().getName());
        coupon.setIdOfCompany(coupon.getCompany().getId());
        return new ResponseEntity<Coupon>(coupon, HttpStatus.OK);
    }

    @PostMapping("logout/{token}")
    public ResponseEntity<?> logout(@PathVariable String token){
        return new ResponseEntity<Boolean>(this.loginManager.logout(token),HttpStatus.ACCEPTED);
    }
}
