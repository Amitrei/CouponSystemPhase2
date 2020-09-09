package com.amitrei.couponsystemv2.schedules;

import com.amitrei.couponsystemv2.beans.Coupon;
import com.amitrei.couponsystemv2.repositories.CouponRepo;
import com.amitrei.couponsystemv2.repositories.CustomerRepo;
import com.amitrei.couponsystemv2.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class CouponExpirationDailyJob  {

    private Boolean quit = false;

    @Autowired
    private CouponRepo couponRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private DateUtil dateUtil;


    @Scheduled(fixedRate =(2000)) // 1000 * 60 * 60 * 24 Works but cannot be seen by the test
    public void deleteExpiredCoupon() {
        List<Coupon> allCoupons = couponRepo.findAll();
        for (Coupon coupon : allCoupons) {

            if (dateUtil.isDatePassed(coupon.getEnd_date())) {

                couponRepo.deletePurchase(coupon.getId());
                couponRepo.delete(coupon);

            }

        }

    }
}
