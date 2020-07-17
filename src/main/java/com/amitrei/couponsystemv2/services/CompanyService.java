package com.amitrei.couponsystemv2.services;


import com.amitrei.couponsystemv2.beans.Category;
import com.amitrei.couponsystemv2.beans.Company;
import com.amitrei.couponsystemv2.beans.Coupon;
import com.amitrei.couponsystemv2.beans.Customer;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService extends ClientServices {

    private int companyId;
    private Company currentCompany;


    @Override
    public boolean login(String email, String password) {

        // Keep the company in a variable to minimize the amount of queries.
        Company company = companyRepo.findByEmail(email);


        if (!companyRepo.existsByEmail(email)) {
            System.out.println("incorrect email");
            return false;
        } else if (!company.getPassword().equals(password)) {
            System.out.println("INCORRECT PASSWORD");
            return false;
        }


        this.currentCompany = company;
        company=null;
        this.companyId = currentCompany.getId();
        return true;

    }

    public void addCoupon(Coupon coupon) {

        for (Coupon companyCoupon : currentCompany.getCoupons()) {


            if (companyCoupon.getTitle().equals(coupon.getTitle())) {
                System.out.println("ALREADY EXISTS TITLE IN COMPANY");
                return;
            }

        }

        //Saving coupon as ManyToOne bidirectional will automaticlly update the company couponlist from the DB
        couponRepo.save(coupon);

        // Updating the plain object to include all new coupons.
        List<Coupon> updatedCouponList = companyRepo.getOne(currentCompany.getId()).getCoupons();
        currentCompany.setCoupons(updatedCouponList);


    }

    public void updateCoupon(Coupon coupon) {

        // ** Checking if coupon id is changed in the Coupon id setter **


        // ** Comparing companies id's between coupon in DB and the new coupon ** //
        int CompanyOfCoupon = couponRepo.getOne(coupon.getId()).getCompany().getId();
        if (CompanyOfCoupon != coupon.getCompany().getId()) {
            System.out.println("CANNOT CHANGE COMPANY EXCEPTION");
            return;
        }


        couponRepo.saveAndFlush(coupon);
        List<Coupon> updatedCouponList = companyRepo.getOne(currentCompany.getId()).getCoupons();
        currentCompany.setCoupons(updatedCouponList);
    }


    @Transactional
    public void deleteCoupon(int couponId) {

        Coupon currentCoupon = couponRepo.getOne(couponId);

        for (Customer couponOwner : currentCoupon.getCustomers()) {


            // Un-tie the bidirectional between the coupon and the customer

            couponOwner.getCoupons().remove(currentCoupon);
            customerRepo.saveAndFlush(couponOwner);

        }

        // Deleting coupon from company coupon List will delete the coupon cause of cascadeType Remove.
        currentCompany.getCoupons().remove(currentCoupon);
        couponRepo.delete(currentCoupon);

    }

    public List<Coupon> getCompanyCoupons() {
        return currentCompany.getCoupons();
    }

    public List<Coupon> getCompanyCoupons(Category category) {

        List<Coupon> listByCategory = currentCompany.getCoupons().stream()
                .filter(coupon -> category.ordinal() == coupon.getCategoryId().ordinal())
                .collect(Collectors.toList());
        return listByCategory;
    }

    public List<Coupon> getCompanyCoupons(double maxPrice) {

        List<Coupon> listByPrice = currentCompany.getCoupons().stream()
                .filter(coupon -> maxPrice >= coupon.getPrice())
                .collect(Collectors.toList());
        return listByPrice;
    }


    public Company companyDetails() {

        return currentCompany;
    }


}
