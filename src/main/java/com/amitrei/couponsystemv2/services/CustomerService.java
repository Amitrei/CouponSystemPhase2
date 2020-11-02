package com.amitrei.couponsystemv2.services;


import com.amitrei.couponsystemv2.exceptions.IllegalActionException;
import com.amitrei.couponsystemv2.beans.Category;
import com.amitrei.couponsystemv2.beans.Coupon;
import com.amitrei.couponsystemv2.beans.Customer;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class CustomerService extends ClientServices {

    private int customerId;
    private Customer currentCustomer;


    @Override
    public boolean login(String email, String password) throws IllegalActionException {



        if (!customerRepo.existsByEmail(email))
            throw new IllegalActionException("Incorrect login details please try again");


        Customer customer = customerRepo.findByEmail(email);


            if (!customer.getPassword().equals(password))
                throw new IllegalActionException("Incorrect login details please try again");


        this.currentCustomer = customer;
        this.customerId = currentCustomer.getId();
        return true;
    }


    public void purchaseCoupon(Coupon coupon) throws IllegalActionException {


        if (coupon.getAmount() <= 0)
            throw new IllegalActionException("coupon is out of stock");

        if (dateUtil.currentDate().after(coupon.getEnd_date()))
            throw new IllegalActionException("coupon is already expired");



        // .contains does not work
        for (Coupon customerCoupon : currentCustomer.getCoupons()) {
            if (customerCoupon.getId() == coupon.getId())
                throw new IllegalActionException("customer already purchased this coupon");
        }


        coupon.setAmount(coupon.getAmount() - 1);
        couponRepo.saveAndFlush(coupon);

        Customer customerFromDB= customerRepo.getOne(currentCustomer.getId());
        customerFromDB.getCoupons().add(coupon);

        customerRepo.saveAndFlush(customerFromDB);
        currentCustomer.setCoupons(customerFromDB.getCoupons());
    }

    public Boolean isOwnCoupon(int couponId ){
        for (Coupon customerCoupon : currentCustomer.getCoupons()) {
            if (customerCoupon.getId() == couponId)
            return true;
        }
        return false;
    }

    public List<Coupon> getCustomerCoupons() {
        return currentCustomer.getCoupons();
    }


    public List<Coupon> getCustomerCoupons(Category category) {

        List<Coupon> filteredList = currentCustomer.getCoupons().stream()
                .filter(coupon -> category.ordinal() == coupon.getCategory().ordinal())
                .collect(Collectors.toList());
        return filteredList;
    }


    public List<Coupon> getCustomerCoupons(double maxPrice) {

        List<Coupon> filteredList = currentCustomer.getCoupons().stream()
                .filter(coupon -> maxPrice >= coupon.getPrice())
                .collect(Collectors.toList());
        return filteredList;
    }


    public Customer getCustomerDetails() {
        return this.currentCustomer;
    }

}
