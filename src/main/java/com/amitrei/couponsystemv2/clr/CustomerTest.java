package com.amitrei.couponsystemv2.clr;

import com.amitrei.couponsystemv2.beans.Category;
import com.amitrei.couponsystemv2.beans.Coupon;
import com.amitrei.couponsystemv2.exceptions.IllegalActionException;
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


@Component
@Order(3)
public class CustomerTest implements CommandLineRunner {

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


        templates.customerTestTitle();

        System.out.println();
        templates.printTitle("CUSTOMER LOGIN");
        String wrongLogin = null;
        try {
            loginManager.login("SimhaReef@gmail.com", "WRONG PASSWORD", ClientType.Customer);

        } catch (IllegalActionException e) {
            wrongLogin = e.getMessage();
        }

        System.out.println("### Customer Login with incorrect details:");
        System.out.println(wrongLogin);
        System.out.println();
        System.out.println("### Customer Login with correct details:");
        ClientServices goodLogin = loginManager.login("SimhaReef@gmail.com", "12345", ClientType.Customer);
        System.out.println(goodLogin.getClass());
        CustomerService customerLogin = (CustomerService) goodLogin;


        System.out.println();
        templates.printTitle("PURCHASE COUPON");
        System.out.println();

        AsciiTable at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Purchasing this coupon").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(couponRepo.getOne(8));
        at.addRule();
        String rend = at.render();
        System.out.println(rend);

        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Before purchasing customer coupons list","Before purchasing customer vs coupon").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(customerLogin.getCustomerCoupons(),couponRepo.allPurchases().entrySet());
        at.addRule();
        rend = at.render();
        System.out.println(rend);

        // Purchasing the coupon
        int tempCoupBefore=couponRepo.getOne(8).getAmount();
        try {
            customerLogin.purchaseCoupon(couponRepo.getOne(8));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        int tempCoupAfter=couponRepo.getOne(8).getAmount();


        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("After purchasing customer coupons list","After purchasing customer vs coupon").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(customerLogin.getCustomerCoupons(),couponRepo.allPurchases().entrySet()).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        rend = at.render();
        System.out.println(rend);
        System.out.println();



        at = new AsciiTable();
        at.getContext().setWidth(150).setFrameLeftMargin(70);
        at.addRule();
        at.addRow("Amount before purchase","Amount after purchase").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(tempCoupBefore,tempCoupAfter).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        rend = at.render();
        System.out.println(rend);
        System.out.println();
















        String getException = null;

        try{
            customerLogin.purchaseCoupon(couponRepo.getOne(8));

        }
        catch (IllegalActionException e) {
            getException = e.getMessage();

        }


        at = new AsciiTable();
        at.getContext().setWidth(150).setFrameLeftMargin(70);
        at.addRule();
        at.addRow("Trying purchasing the same coupon").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(getException).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        rend = at.render();
        System.out.println(rend);



        Coupon coupon = couponRepo.getOne(8);
        coupon.setAmount(0);
        couponRepo.saveAndFlush(coupon);



        try{
            customerLogin.purchaseCoupon(couponRepo.getOne(8));

        }
        catch (IllegalActionException e) {
            getException = e.getMessage();

        }


        System.out.println();
        at = new AsciiTable();
        at.getContext().setWidth(150).setFrameLeftMargin(70);
        at.addRule();
        at.addRow("Trying purchasing coupon with 0 amount").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow( "Coupon id - " + coupon.getId() + " , Amount " + coupon.getAmount()).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(getException).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        rend = at.render();
        System.out.println(rend);


        coupon.setAmount(100);
        coupon.setEnd_date(dateUtil.expiredDateFromToday(-1));
        couponRepo.saveAndFlush(coupon);



        try{
            customerLogin.purchaseCoupon(couponRepo.getOne(8));

        }
        catch (IllegalActionException e) {
            getException = e.getMessage();

        }


        System.out.println();
        at = new AsciiTable();
        at.getContext().setWidth(150).setFrameLeftMargin(70);
        at.addRule();
        at.addRow("Trying purchasing coupon with expired date").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow( "Coupon id - " + coupon.getId() + " , End date " + coupon.getEnd_date()).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(getException).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        rend = at.render();
        System.out.println(rend);


        System.out.println();
        templates.printTitle("ALL CUSTOMER COUPONS");


        // Adding coupons dummies and purchasing them

        Coupon couponDummy1 = Coupon.builder().category(Category.FOOD).amount(55).company(companyRepo.getOne(3)).start_date(dateUtil.currentDate()).end_date(dateUtil.expiredDateFromToday(10)).description("Burgers are good for your health").image("image.png").price(100).title("Burgers for free").build();
        Coupon couponDummy2 = Coupon.builder().category(Category.VACATION).amount(100).company(companyRepo.getOne(3)).start_date(dateUtil.currentDate()).end_date(dateUtil.expiredDateFromToday(10)).description("Best experince you will ever get").image("image.png").price(20).title("Fancy Hotel").build();

        // Not Needed
//        couponRepo.save(couponDummy1);
//        couponRepo.save(couponDummy2);

        customerLogin.purchaseCoupon(couponDummy1);
        customerLogin.purchaseCoupon(couponDummy2);





        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("All customer Coupons:").setTextAlignment(TextAlignment.CENTER);
        at.addRule();

        for(Coupon customerCoupon : customerLogin.getCustomerCoupons()) {

            at.addRow("Coupon id - "+customerCoupon.getId() + " , Coupon title - " + customerCoupon.getTitle() + " , Coupon description - " +customerCoupon.getDescription()).setTextAlignment(TextAlignment.CENTER);
            at.addRule();
        }
        rend = at.render();
        System.out.println(rend);
        System.out.println();


        System.out.println();
        templates.printTitle("ALL CUSTOMER COUPONS BY CATEGORY");
        System.out.println();

        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("All customer Coupons of category:" + Category.VACATION).setTextAlignment(TextAlignment.CENTER);
        at.addRule();

        for(Coupon customerCoupon : customerLogin.getCustomerCoupons(Category.VACATION)) {

            at.addRow("Coupon id - "+customerCoupon.getId() + " Coupon title - " + customerCoupon.getTitle() + " Coupon Category - " +customerCoupon.getCategory()).setTextAlignment(TextAlignment.CENTER);
            at.addRule();
        }
        rend = at.render();
        System.out.println(rend);
        System.out.println();

        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("All customer Coupons of category:" + Category.SPORT).setTextAlignment(TextAlignment.CENTER);
        at.addRule();

        for(Coupon customerCoupon : customerLogin.getCustomerCoupons(Category.SPORT)) {

            at.addRow("Coupon id - "+customerCoupon.getId() + " Coupon title - " + customerCoupon.getTitle() + " Coupon Category - " +customerCoupon.getCategory()).setTextAlignment(TextAlignment.CENTER);
            at.addRule();
        }
        rend = at.render();
        System.out.println(rend);
        System.out.println();

        System.out.println();
        templates.printTitle("ALL CUSTOMER COUPONS BY PRICE");
        System.out.println();


        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("All customer Coupons by price 100:").setTextAlignment(TextAlignment.CENTER);
        at.addRule();

        for(Coupon customerCoupon : customerLogin.getCustomerCoupons(100)) {

            at.addRow("Coupon id - "+customerCoupon.getId() + " Coupon title - " + customerCoupon.getTitle() + " Coupon price - " +customerCoupon.getPrice()).setTextAlignment(TextAlignment.CENTER);
            at.addRule();
        }
        rend = at.render();
        System.out.println(rend);
        System.out.println();

        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("All customer Coupons by price 60:").setTextAlignment(TextAlignment.CENTER);
        at.addRule();

        for(Coupon customerCoupon : customerLogin.getCustomerCoupons(60)) {

            at.addRow("Coupon id - "+customerCoupon.getId() + " Coupon title - " + customerCoupon.getTitle() + " Coupon price - " +customerCoupon.getPrice()).setTextAlignment(TextAlignment.CENTER);
            at.addRule();
        }
        rend = at.render();
        System.out.println(rend);
        System.out.println();


    }
}
