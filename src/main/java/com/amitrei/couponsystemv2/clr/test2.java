package com.amitrei.couponsystemv2.clr;

import com.amitrei.couponsystemv2.beans.Category;
import com.amitrei.couponsystemv2.beans.Company;
import com.amitrei.couponsystemv2.beans.Coupon;
import com.amitrei.couponsystemv2.beans.Customer;
import com.amitrei.couponsystemv2.repositories.CompanyRepo;
import com.amitrei.couponsystemv2.repositories.CouponRepo;
import com.amitrei.couponsystemv2.repositories.CustomerRepo;
import com.amitrei.couponsystemv2.security.ClientType;
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
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;

//@Component
public class test2 implements CommandLineRunner {


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
    private DateUtil dateUtil;

    @Autowired
    private CouponExpirationDailyJob couponExpirationDailyJob;

    @Override
    public void run(String... args) throws Exception {


        Company company= Company.builder().name("coca-cola").email("cola@gmail.com").password("1234").coupons(new ArrayList<>()).build();


        Customer customer= Customer.builder().firstName("Amit").lastName("Rei").password("1234").email("Amitrei@gmail.com")
                .          coupons(new HashSet<>()).build();

        Coupon coupon = Coupon.builder().amount(100).category(Category.FOOD).description("adsadas")
                .end_date(dateUtil.expiredDateFromToday(10)).start_date(dateUtil.currentDate()).image("image")
                .title("title").price(99).company(company).build();



        AsciiTable at = new AsciiTable();
        at.getContext().setWidth(50).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Adding this coupon:").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRule();
        String rend = at.render();
        System.out.println(rend);


        companyRepo.save(company);
        CompanyService myCompany=(CompanyService)loginManager.login("cola@gmail.com","1234", ClientType.Company);
        AdminService myAdmin = (AdminService)loginManager.login("admin@admin.com","admin",ClientType.Administrator);

        myCompany.addCoupon(coupon);
        customer.getCoupons().add(coupon);
        customerRepo.save(customer);
        System.out.println(customerRepo.getOne(customer.getId()));
        System.out.println(companyRepo.getOne(company.getId()).getCoupons());
        System.out.println( customerRepo.getOne(customer.getId()));
        myCompany.deleteCoupon(coupon.getId());

        System.out.println(myCompany.getCompanyCoupons());
        System.out.println(companyRepo.getOne(myCompany.companyDetails().getId()).getCoupons());



    }


}
