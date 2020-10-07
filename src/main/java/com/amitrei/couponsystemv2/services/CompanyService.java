package com.amitrei.couponsystemv2.services;


import com.amitrei.couponsystemv2.exceptions.AlreadyExistsException;
import com.amitrei.couponsystemv2.exceptions.IllegalActionException;
import com.amitrei.couponsystemv2.beans.Category;
import com.amitrei.couponsystemv2.beans.Company;
import com.amitrei.couponsystemv2.beans.Coupon;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class CompanyService extends ClientServices {

    private int companyId;
    private Company currentCompany;


    @Override
    public boolean login(String email, String password) throws IllegalActionException {


        if (!companyRepo.existsByEmail(email)) {
            throw new IllegalActionException("Incorrect email address");
        }

        Company company = companyRepo.findByEmail(email);

         if (!company.getPassword().equals(password))
            throw new IllegalActionException("Incorrect password");


        this.currentCompany = company;
        this.companyId = currentCompany.getId();
        return true;

    }

    public void addCoupon(Coupon coupon) throws AlreadyExistsException {

        for (Coupon companyCoupon : currentCompany.getCoupons()) {


            if (companyCoupon.getTitle().equals(coupon.getTitle()))
                throw new AlreadyExistsException("coupon title");


        }

        //Saving coupon as ManyToOne bi-directional will automatically update the company coupon list
        couponRepo.save(coupon);

        // Updating the plain object to include all new coupons.
        List<Coupon> updatedCouponList = companyRepo.getOne(currentCompany.getId()).getCoupons();
        currentCompany.setCoupons(updatedCouponList);


    }

    public void updateCoupon(Coupon coupon) throws IllegalActionException {

        // ** Checking if coupon id is changed in the Coupon id setter **


        // Comparing companies id's between coupon in DB and the new coupon
        int CompanyOfCoupon = couponRepo.getOne(coupon.getId()).getCompany().getId();
        if (CompanyOfCoupon != coupon.getCompany().getId())
            throw new IllegalActionException("illegal to change company id");



        if(coupon.getAmount()<=0)  throw new IllegalActionException("amount cannot be less or equal to zero.");
        if(coupon.getPrice()<=0)  throw new IllegalActionException("price cannot be less or equal to zero.");





        couponRepo.saveAndFlush(coupon);
        List<Coupon> updatedCouponList = companyRepo.getOne(currentCompany.getId()).getCoupons();
        currentCompany.setCoupons(updatedCouponList);
    }


    public void deleteCoupon(int couponId) {

        couponRepo.deleteById(couponId);
        couponRepo.deletePurchase(couponId);
        currentCompany.getCoupons().removeIf(coupon -> coupon.getId()==couponId);


    }

    public List<Coupon> getCompanyCoupons() {
        return currentCompany.getCoupons();
    }

    public List<Coupon> getCompanyCoupons(Category category) {

        List<Coupon> listByCategory = currentCompany.getCoupons().stream()
                .filter(coupon -> category.ordinal() == coupon.getCategory().ordinal())
                .collect(Collectors.toList());

        return listByCategory;
    }


    public List<Coupon> getCompanyCoupons(double maxPrice) {

        List<Coupon> listByPrice = currentCompany.getCoupons().stream()
                .filter(coupon -> maxPrice >= coupon.getPrice())
                .collect(Collectors.toList());

        return listByPrice;
    }

    public boolean isTitleExists(String title) {
                long isExists= this.currentCompany.getCoupons().stream()
                .filter(coupon->coupon.getTitle().toLowerCase().equals(title.toLowerCase())).count();


        return isExists > 0;


    }
    public Company companyDetails() {
        return currentCompany;
    }


}
