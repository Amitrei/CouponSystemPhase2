package com.amitrei.couponsystemv2.clr;

import com.amitrei.couponsystemv2.beans.Company;
import com.amitrei.couponsystemv2.beans.Coupon;
import com.amitrei.couponsystemv2.repositories.CompanyRepo;
import com.amitrei.couponsystemv2.repositories.CouponRepo;
import com.amitrei.couponsystemv2.repositories.CustomerRepo;
import com.amitrei.couponsystemv2.security.LoginManager;
import com.amitrei.couponsystemv2.services.AdminService;
import com.amitrei.couponsystemv2.services.CompanyService;
import com.amitrei.couponsystemv2.services.CustomerService;
import com.amitrei.couponsystemv2.utils.CouponExpirationDailyJob;
import com.amitrei.couponsystemv2.utils.DateUtil;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Order(4)
public class DailyJobTest implements CommandLineRunner {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private CouponRepo couponRepo;

    @Autowired
    private CompanyRepo companyRepo;

    @Autowired
    private AdminService adminService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private LoginManager loginManager;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private Templates templates;

    @Autowired
    private DateUtil dateUtil;

    @Autowired
    private CouponExpirationDailyJob couponExpirationDailyJob;


    @Override
    public void run(String... args) throws Exception {
        /**
         *
         * DAILYJOB TEST
         *
         */

        templates.dailyJobTest();
        System.out.println();

        AsciiTable at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("List of coupon before initiate the daily job").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        for (Coupon coupon : couponRepo.findAll()) {
            at.addRow("Coupon id - " + coupon.getId() + " , Coupon end date " + coupon.getEnd_date()).setTextAlignment(TextAlignment.CENTER);
            at.addRule();
        }


        Coupon coupon = couponRepo.getOne(9);
        coupon.setEnd_date(dateUtil.expiredDateFromToday(-100));
        couponRepo.saveAndFlush(coupon);


        String rend = at.render();
        System.out.println(rend);
        Thread t1 = new Thread(couponExpirationDailyJob);
        t1.start();
        Thread.sleep(3000);


        System.out.println();
        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("List of coupon after initiate the daily job").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        for (Coupon coupon2 : couponRepo.findAll()) {
            at.addRow("Coupon id - " + coupon2.getId() + " , Coupon end date " + coupon2.getEnd_date()).setTextAlignment(TextAlignment.CENTER);
            at.addRule();
        }
        rend = at.render();
        System.out.println(rend);


    }


}
