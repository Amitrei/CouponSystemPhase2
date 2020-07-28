package com.amitrei.couponsystemv2.clr;

import com.amitrei.couponsystemv2.exceptions.AlreadyExistsException;
import com.amitrei.couponsystemv2.exceptions.IllegalActionException;
import com.amitrei.couponsystemv2.beans.*;
import com.amitrei.couponsystemv2.repositories.CompanyRepo;
import com.amitrei.couponsystemv2.repositories.CouponRepo;
import com.amitrei.couponsystemv2.repositories.CustomerRepo;
import com.amitrei.couponsystemv2.security.ClientType;
import com.amitrei.couponsystemv2.security.LoginManager;
import com.amitrei.couponsystemv2.services.AdminService;
import com.amitrei.couponsystemv2.services.ClientServices;
import com.amitrei.couponsystemv2.services.CompanyService;
import com.amitrei.couponsystemv2.services.CustomerService;
import com.amitrei.couponsystemv2.utils.CouponExpirationDailyJob;
import com.amitrei.couponsystemv2.utils.DateUtil;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Order(3)
public class CompanyTest implements CommandLineRunner {

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
         * COMPANY TESTS
         *
         *
         */


        templates.companyTestTitle();
        System.out.println();
        System.out.println();
        templates.printTitle("COMPANY LOGIN");
        ClientServices companyBadLogin=null;
        String  incorrectMessage=null;




        System.out.println("### Company Login with incorrect details:");

        try {
            companyBadLogin = loginManager.login("Cocax-Cola@gmail.com", "1xx234", ClientType.Company);
        }

        catch (IllegalActionException e) {
            System.out.println(e.getMessage());
        }

        System.out.println();
        System.out.println("### Company Login with correct details:");

        ClientServices companyGoodLogin=null;
        try {
            companyGoodLogin = loginManager.login("Coca-Cola@gmail.com", "1234", ClientType.Company);
        }

        catch (IllegalActionException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(companyGoodLogin.getClass());
        CompanyService companyLogin=(CompanyService) companyGoodLogin;

        System.out.println();
        System.out.println();
        templates.printTitle("ADD COUPON");
        System.out.println();
        Coupon companyAddCoupon = Coupon.builder().category(Category.FOOD).amount(100).company(companyLogin.companyDetails()).start_date(dateUtil.currentDate()).end_date(dateUtil.expiredDateFromToday(10)).description("Description").image("image.png").price(100).title("BestCoupon").build();
        AsciiTable at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Adding this coupon:").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(companyAddCoupon);
        at.addRule();
        String rend = at.render();
        System.out.println(rend);






    }






}
