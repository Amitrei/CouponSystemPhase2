package com.amitrei.couponsystemv2.services;


import com.amitrei.couponsystemv2.Exceptions.IllegalActionException;
import com.amitrei.couponsystemv2.beans.Category;
import com.amitrei.couponsystemv2.beans.Coupon;
import com.amitrei.couponsystemv2.beans.Customer;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Lazy
public class CustomerService extends ClientServices {

    private int customerId;
    private Customer currentCustomer;


    @Override
    public boolean login(String email, String password) throws IllegalActionException {


        Customer customer = customerRepo.findByEmail(email);

        if (!customerRepo.existsByEmail(email))
            throw new IllegalActionException("Incorrect email address");


        else if (!customer.getPassword().equals(password))
            throw new IllegalActionException("Incorrect password");


        this.currentCustomer = customer;
        customer = null;
        this.customerId = currentCustomer.getId();
        return true;
    }




    public void purchaseCoupon(Coupon coupon) throws IllegalActionException {

        if (coupon.getAmount() <= 0)
            throw new IllegalActionException("coupon is out of stock");

        else if (dateUtil.currentDate().after(coupon.getEnd_date()))

            throw new IllegalActionException("coupon is already expired");

        else if (currentCustomer.getCoupons().contains(coupon))
            throw new IllegalActionException("customer already purchased this coupon");


        coupon.setAmount(coupon.getAmount() - 1);
        couponRepo.saveAndFlush(coupon);
        Customer customerFromDB = customerRepo.getOne(customerId);
        customerFromDB.getCoupons().add(coupon);
        customerRepo.save(customerFromDB);
        currentCustomer = customerFromDB;
    }


    public Set<Coupon> getCustomerCoupons() {
        return currentCustomer.getCoupons();
    }


    public Set<Coupon> getCustomerCoupons(Category category) {

        Set<Coupon> filteredSet = currentCustomer.getCoupons().stream()
                .filter(coupon -> category.ordinal() == coupon.getCategory().ordinal())
                .collect(Collectors.toSet());
        return filteredSet;
    }


    public Set<Coupon> getCustomerCoupons(double maxPrice) {

        Set<Coupon> filteredSet = currentCustomer.getCoupons().stream()
                .filter(coupon -> maxPrice >= coupon.getPrice())
                .collect(Collectors.toSet());
        return filteredSet;
    }


    public Customer getCustomerDetails() {
        return this.currentCustomer;
    }


}
