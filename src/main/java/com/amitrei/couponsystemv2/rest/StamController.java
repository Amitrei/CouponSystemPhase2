package com.amitrei.couponsystemv2.rest;


import com.amitrei.couponsystemv2.beans.Coupon;
import com.amitrei.couponsystemv2.beans.Customer;
import com.amitrei.couponsystemv2.repositories.CouponRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("stam")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class StamController {


    @Autowired
    private CouponRepo couponRepo;


//    @GetMapping("/all")
//    public ResponseEntity<?> allCoupons() {
//        List<Coupon> myList = couponRepo.findAll();
//        myList.forEach(coupon -> coupon.setCompanyName(coupon.getCompany().getName()));
//        return new ResponseEntity<>(myList, HttpStatus.OK);
//    }
}
