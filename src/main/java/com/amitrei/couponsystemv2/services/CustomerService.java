package com.amitrei.couponsystemv2.services;


import com.amitrei.couponsystemv2.beans.Category;
import com.amitrei.couponsystemv2.beans.Coupon;
import com.amitrei.couponsystemv2.beans.Customer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerService extends ClientServices {

    private int customerId;
    private Customer currentCustomer;


    @Override
    public boolean login(String email, String password) {


        Customer customer = customerRepo.findByEmail(email);

        if (!customerRepo.existsByEmail(email)) {
            System.out.println("EMAIL INCORRECT");
            return false;

        } else if (!customer.getPassword().equals(password)) {
            System.out.println("PASSWORD INCORRECT");
            return false;
        }


        this.currentCustomer = customer;
        customer = null;
        this.customerId = currentCustomer.getId();
        return true;
    }



    public void purchaseCoupon(Coupon coupon) {

        if (coupon.getAmount() <= 0) {
            System.out.println("NO COUPON AVILIABLE");
            return;


        } else if (dateUtil.currentDate().after(coupon.getEnd_date())) {

            System.out.println("SORRY ALREADY EXPIRED");
            return;

        } else if (currentCustomer.getCoupons().contains(coupon)) {
            System.out.println("COUPON ALREADY PURCHASED");
            return;

        }

        coupon.setAmount(coupon.getAmount() - 1);
        couponRepo.saveAndFlush(coupon);

       Customer customerFromDB = customerRepo.getOne(customerId);
       customerFromDB.getCoupons().add(coupon);
        customerRepo.save(customerFromDB);
        currentCustomer=customerFromDB;
        System.out.println(currentCustomer);
    }



    public Set<Coupon> getCustomerCoupons() {
        return currentCustomer.getCoupons();
    }


    public Set<Coupon> getCustomerCoupons(Category category) {

        Set<Coupon> filterdSet = currentCustomer.getCoupons().stream()
                .filter(coupon -> category.ordinal() == coupon.getCategoryId().ordinal())
                .collect(Collectors.toSet());
        return filterdSet;
    }


    public Set<Coupon> getCustomerCoupons(double maxPrice) {

        Set<Coupon> filterdSet = currentCustomer.getCoupons().stream()
                .filter(coupon -> maxPrice >= coupon.getPrice())
                .collect(Collectors.toSet());
        return filterdSet;
    }


    public Customer getCustomerDetails() {
        return currentCustomer;
    }


}
