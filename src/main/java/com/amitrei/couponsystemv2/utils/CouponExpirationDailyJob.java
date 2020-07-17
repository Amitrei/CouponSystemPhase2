package com.amitrei.couponsystemv2.utils;

import com.amitrei.couponsystemv2.beans.Coupon;
import com.amitrei.couponsystemv2.beans.Customer;
import com.amitrei.couponsystemv2.repositories.CouponRepo;
import com.amitrei.couponsystemv2.repositories.CustomerRepo;
import com.amitrei.couponsystemv2.services.CustomerService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component
public class CouponExpirationDailyJob implements Runnable {

    private Boolean quit = false;

    @Autowired
    private CouponRepo couponRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private DateUtil dateUtil;


    @Override
    @Transactional
    public void run() {


        while (!quit) {

            List<Coupon> allCoupons = couponRepo.findAll();
            for (Coupon coupon : allCoupons) {

                if(dateUtil.isDatePassed(coupon.getEnd_date())) {

                    for(Customer couponOwner : coupon.getCustomers()) {

                        couponOwner.getCoupons().remove(coupon);
                        customerRepo.saveAndFlush(couponOwner);

                    }

                    couponRepo.delete(coupon);
                }
            }

            try {
                Thread.sleep(1000*60*60*24);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public void stopIt() {
        this.quit = true;

    }
}
