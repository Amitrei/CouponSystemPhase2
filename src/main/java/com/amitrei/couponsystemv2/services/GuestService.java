package com.amitrei.couponsystemv2.services;


import com.amitrei.couponsystemv2.beans.Coupon;
import com.amitrei.couponsystemv2.repositories.CouponRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("prototype")
public class GuestService {

    @Autowired
    private CouponRepo couponRepo;



    public List<Coupon> getAllCoupons() {
        return couponRepo.findAll();
    }

    public Coupon getSingleCoupon(int couponID) { return couponRepo.getOne(couponID);}
}




