package com.amitrei.couponsystemv2.clr;

import com.amitrei.couponsystemv2.beans.*;
import com.amitrei.couponsystemv2.repositories.CompanyRepo;
import com.amitrei.couponsystemv2.repositories.CouponRepo;
import com.amitrei.couponsystemv2.repositories.CustomerRepo;
import com.amitrei.couponsystemv2.services.AdminService;
import com.amitrei.couponsystemv2.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class test implements CommandLineRunner {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private CouponRepo couponRepo;

    @Autowired
    private CompanyRepo companyRepo;

    @Autowired
    private AdminService adminService;


    @Autowired
    private DateUtil dateUtil;










    @Override
    public void run(String... args) throws Exception {




        Customer customer = new Customer();
        customer.setFirstName("Amit");
        customer.setLastName("Reini");
        customer.setPassword("1234");
        customer.setEmail("amit@gmail.com");


        Customer customer2 = new Customer();
        customer2.setFirstName("moshe");
        customer2.setLastName("xxx");
        customer2.setPassword("1234");
        customer2.setEmail("amit@gmail.com");



        Company company= new Company();
        company.setEmail("aasfsa@gmail.com");
        company.setName("amit");
        company.setPassword("12434");
        companyRepo.saveAndFlush(company);

        Coupon coupon = new Coupon();
        coupon.setAmount(100);
//        coupon.setCategoryId(Category.FOOD);
        coupon.setDescription("asfasf");
        coupon.setAmount(100);
        coupon.setImage("asfsa");
        coupon.setPrice(14);
        coupon.setEnd_date(dateUtil.expiredDateFromToday(10));
        coupon.setStart_date(dateUtil.currentDate());
        coupon.setTitle("asfasf");
        coupon.setCompany(company);
//
        Coupon coupon2 = new Coupon();
        coupon2.setAmount(100);
//        coupon2.setCategoryId(Category.FOOD);
        coupon2.setDescription("asfasf");
        coupon2.setAmount(100);
        coupon2.setImage("asfsa");
        coupon2.setPrice(14);
        coupon2.setEnd_date(dateUtil.expiredDateFromToday(10));
        coupon2.setStart_date(dateUtil.setDate(01,05,2020));
        coupon2.setTitle("asfasf");
        coupon2.setCompany(company);






//
        couponRepo.save(coupon);
        couponRepo.save(coupon2);
        customer.getCoupons().add(coupon);
        customer.getCoupons().add(coupon2);

        adminService.addCustomer(customer);
        adminService.deleteCustomer(1);
















    }
}
