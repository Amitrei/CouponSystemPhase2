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
import com.amitrei.couponsystemv2.schedules.CouponExpirationDailyJob;
import com.amitrei.couponsystemv2.utils.DateUtil;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
//
//@Component
//@Order(2)
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
        System.out.println();
        var col = couponRepo.findAll().toString();
        companyLogin.addCoupon(companyAddCoupon);
        var col2 = couponRepo.findAll().toString();



        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Before adding coupon","After adding coupon").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(col,col2);
        at.addRule();
        rend = at.render();
        System.out.println(rend);
        System.out.println();


try{
    companyLogin.addCoupon(companyAddCoupon);
}
catch (AlreadyExistsException e) {
    col = e.getMessage();
}




        Coupon companyCoupon2 = Coupon.builder().category(Category.FOOD).amount(100).company(companyRepo.getOne(4)).start_date(dateUtil.currentDate()).end_date(dateUtil.expiredDateFromToday(10)).description("Description").image("image.png").price(100).title("BestCoupon").build();
        couponRepo.save(companyCoupon2);

        List<String> allCoupons = couponRepo.findAll().stream().flatMap(couponOfCompany -> Stream.of("[ coupon id-" +couponOfCompany.getId()+" , company id-" + couponOfCompany.getCompany().getId()+ " , title-" +couponOfCompany.getTitle() + "]")).collect(Collectors.toList());


        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Adding the same coupon again","Adding a coupon with the same title but different company").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(col,allCoupons).setTextAlignment(TextAlignment.CENTER);;
        at.addRule();
        rend = at.render();
        System.out.println(rend);


        System.out.println();
        templates.printTitle("UPDATE COUPON");
        System.out.println();



        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Updating this coupon").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(couponRepo.getOne(companyAddCoupon.getId())).setTextAlignment(TextAlignment.CENTER);;
        at.addRule();
        rend = at.render();
        System.out.println(rend);



        col = couponRepo.getOne(companyAddCoupon.getId()).toString();
        companyAddCoupon.setAmount(222);
        companyAddCoupon.setDescription("UPDATED DESCRIPTION");
        companyAddCoupon.setImage("UPDATED IMAGE");
        companyAddCoupon.setTitle("UPDATED TITLE");

        companyLogin.updateCoupon(companyAddCoupon);

        col2 = couponRepo.getOne(companyAddCoupon.getId()).toString();






        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Before update","After update").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(col,col2).setTextAlignment(TextAlignment.CENTER);;
        at.addRule();
        rend = at.render();
        System.out.println(rend);



//        try{
//            companyAddCoupon.setId(123421);
//        }
////        catch (IllegalActionException e ) {
////            col = e.getMessage();
////        }

        try{
            companyAddCoupon.setCompany(companyRepo.getOne(5));
            companyLogin.updateCoupon(companyAddCoupon);
        }

        catch (IllegalActionException e) {
            col2 = e.getMessage();
        }





        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Update coupon id","Update coupon company id").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(col,col2).setTextAlignment(TextAlignment.CENTER);;
        at.addRule();
        rend = at.render();
        System.out.println(rend);


        System.out.println();
        templates.printTitle("DELETE COUPON");
        System.out.println();


        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Deleting this coupon").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(couponRepo.getOne(companyAddCoupon.getId())).setTextAlignment(TextAlignment.CENTER);;
        at.addRule();
        rend = at.render();
        System.out.println(rend);


        // Purchasing the coupon to show that delete action also deletes the purchases

        Customer customer = customerRepo.getOne(4);
        customer.getCoupons().add(companyAddCoupon);
        customerRepo.saveAndFlush(customer);


        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Before delete coupon list","Before delete coupon all coupons purchases").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(couponRepo.findAll(),couponRepo.allPurchases().entrySet().toString()).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        rend = at.render();
        System.out.println(rend);


        companyLogin.deleteCoupon(companyAddCoupon.getId());

        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("After delete coupon list","After delete coupon all coupons purchases").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(couponRepo.findAll(),couponRepo.allPurchases().entrySet().toString()).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        rend = at.render();
        System.out.println(rend);


        System.out.println();
        templates.printTitle("GETTING ALL COUPONS");

        // Adding Dummies coupons

        Coupon couponDummy1 = Coupon.builder().category(Category.FOOD).amount(55).company(companyRepo.getOne(3)).start_date(dateUtil.currentDate()).end_date(dateUtil.expiredDateFromToday(10)).description("Burgers are good for your health").image("image.png").price(100).title("Burgers for free").build();
        Coupon couponDummy2 = Coupon.builder().category(Category.VACATION).amount(100).company(companyRepo.getOne(3)).start_date(dateUtil.currentDate()).end_date(dateUtil.expiredDateFromToday(10)).description("Best experince you will ever get").image("image.png").price(20).title("Fancy Hotel").build();
        Coupon couponDummy3 = Coupon.builder().category(Category.SPORT).amount(20).company(companyRepo.getOne(3)).start_date(dateUtil.currentDate()).end_date(dateUtil.expiredDateFromToday(10)).description("Run as fast as u can with our shoes").image("image.png").price(80).title("Running shoes").build();

        companyLogin.addCoupon(couponDummy1);
        companyLogin.addCoupon(couponDummy2);
        companyLogin.addCoupon(couponDummy3);



        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("All coupons for company "+ companyLogin.companyDetails().getId() +"- "+ companyLogin.companyDetails().getName()).setTextAlignment(TextAlignment.CENTER);
        at.addRule();

        for(Coupon coupon : companyLogin.getCompanyCoupons()) {
            at.addRow(coupon).setTextAlignment(TextAlignment.CENTER);
            at.addRule();

        }

        rend = at.render();
        System.out.println(rend);


        System.out.println();
        templates.printTitle("ALL COUPON BY CATEGORY");

        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("All coupons for company by category "+ Category.SPORT).setTextAlignment(TextAlignment.CENTER);
        at.addRule();

        for(Coupon coupon : companyLogin.getCompanyCoupons(Category.SPORT)) {
            at.addRow(coupon).setTextAlignment(TextAlignment.CENTER);
            at.addRule();

        }

        rend = at.render();
        System.out.println(rend);
        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("All coupons for company by category "+ Category.FOOD).setTextAlignment(TextAlignment.CENTER);
        at.addRule();

        for(Coupon coupon : companyLogin.getCompanyCoupons(Category.FOOD)) {
            at.addRow(coupon).setTextAlignment(TextAlignment.CENTER);
            at.addRule();

        }

        rend = at.render();
        System.out.println(rend);


        System.out.println();
        templates.printTitle("ALL COUPONS BY PRICE");




        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("All coupons for company by price: 100").setTextAlignment(TextAlignment.CENTER);
        at.addRule();

        for(Coupon coupon : companyLogin.getCompanyCoupons(100)) {
            at.addRow(coupon).setTextAlignment(TextAlignment.CENTER);
            at.addRule();

        }

        rend = at.render();
        System.out.println(rend);



        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("All coupons for company by price: 50").setTextAlignment(TextAlignment.CENTER);
        at.addRule();

        for(Coupon coupon : companyLogin.getCompanyCoupons(50)) {
            at.addRow(coupon).setTextAlignment(TextAlignment.CENTER);
            at.addRule();

        }

        rend = at.render();
        System.out.println(rend);


        System.out.println();
        templates.printTitle("COMPANY DETAILS");
        System.out.println();

        // Deleting all dummies coupon
        companyLogin.deleteCoupon(couponDummy1.getId());
        companyLogin.deleteCoupon(couponDummy2.getId());


        Company currentCompany = companyLogin.companyDetails();

        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Company details").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(currentCompany).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        rend = at.render();
        System.out.println(rend);

















    }








}
