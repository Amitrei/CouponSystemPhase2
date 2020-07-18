package com.amitrei.couponsystemv2.utils;

import com.amitrei.couponsystemv2.beans.Coupon;
import com.amitrei.couponsystemv2.beans.Customer;
import com.amitrei.couponsystemv2.repositories.CouponRepo;
import com.amitrei.couponsystemv2.repositories.CustomerRepo;
import com.amitrei.couponsystemv2.services.CustomerService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.List;


@Component
@Lazy
public class CouponExpirationDailyJob implements Runnable {

    private Boolean quit = false;

    @Autowired
    private CouponRepo couponRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private DateUtil dateUtil;


    @Override
    public void run() {


        while (!quit) {
            deleteExpiredCoupon();
        }

        try {
            Thread.sleep(1000 * 60 * 60 * 24);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

    }

    public void stop() {
        this.quit = true;

    }


    public void deleteExpiredCoupon() {
        List<Coupon> allCoupons = couponRepo.findAll();
        for (Coupon coupon : allCoupons) {

            if (dateUtil.isDatePassed(coupon.getEnd_date())) {

                for (Customer couponOwner : coupon.getCustomers()) {

                    couponOwner.getCoupons().removeIf(coupon1 -> coupon1.getId() == coupon.getId());
                    customerRepo.saveAndFlush(couponOwner);

                }

                couponRepo.delete(coupon);

            }

        }

    }
}
