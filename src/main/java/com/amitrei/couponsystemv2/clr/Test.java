package com.amitrei.couponsystemv2.clr;

import com.amitrei.couponsystemv2.Exceptions.AlreadyExistsException;
import com.amitrei.couponsystemv2.Exceptions.IllegalActionException;
import com.amitrei.couponsystemv2.beans.*;
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
import de.vandermeer.asciithemes.a7.A7_Grids;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
public class Test implements CommandLineRunner {

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


        /**
         *
         * DAILYJOB TEST
         *
         */

        initTestTitle();
        System.out.println(" Adding an a expired coupon to make sure thread is working:");
        Company company = Company.builder().name("Coca-cola").email("Coca-cola@gmail.com").password("1234").build();
        Coupon coupon = Coupon.builder().category(Category.SPORT).amount(100).description("BestCouponEver").start_date(dateUtil.currentDate())
                .end_date(dateUtil.expiredDateFromToday(-1)).image("logo.png").title("Coca-cola discount").price(40).company(company).build();
        companyRepo.save(company);
        couponRepo.save(coupon);

        System.out.println("### All coupons before init thread : ");
        System.out.println(couponRepo.findAll());

        System.out.println();
        System.out.println("### Init daily thread , all coupons after init: ");
        Thread t1 = new Thread(couponExpirationDailyJob);
//        t1.start();
//        Thread.sleep(3000);
        System.out.println(couponRepo.findAll());
        System.out.println();
        System.out.println();

        /**
         *
         * ADMIN TEST
         *
         */

        adminTestTitle();
        System.out.println();
        printTitle("ADMIN LOGIN");
        System.out.println();
        System.out.println("### Admin Login with hardcoded email & password and getting adminService ");
        var loggedIn = loginManager.login("admin@admin.com", "admin", ClientType.Administrator);
        System.out.println(loggedIn.getClass());
        System.out.println();

        System.out.println("### Admin Login with wrong email & password and getting null ");
        loggedIn = loginManager.login("xxadmin@admin.comxx", "admin", ClientType.Administrator);
        System.out.println(loggedIn);

        // Casting the loginManager into new var to get all methods of adminService
        var loggedAdmin = ((AdminService) loggedIn);

        // Deleting the company & coupon that created for thread example
        adminService.deleteCompany(1);
        System.out.println();
        System.out.println();

        printTitle("ADDING NEW COMPANY");
        company = Company.builder().name("Coca-cola").email("Coca-cola@gmail.com").password("1234").build();
        adminService.addCompany(company);
        System.out.println(companyRepo.findAll());
        System.out.println();

        company.setEmail("abracadbra@gmail.com");
        System.out.println("### Trying to add the company with the same name:");


        try {
            adminService.addCompany(company);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println();
        company.setEmail("Coca-cola@gmail.com");
        company.setName("changedName");
        System.out.println("### Trying to add the company with the same email:");


        try {
            adminService.addCompany(company);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println();
        System.out.println();

        // Returning the company to the original name
        company.setName("Coca-cola");


        printTitle("UPDATE COMPANY");


        var col1 = companyRepo.getOne(company.getId()).toString();
        company.setEmail("UpdateEmail@gmail.com");
        company.setPassword("UpdatedPassword");
        adminService.updateCompany(company);
        var col2 = companyRepo.getOne(company.getId()).toString();


        AsciiTable at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Before Update from DB", "After Update from DB").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(col1, col2);
        at.addRule();
        String rend = at.render();
        System.out.println(rend);


        System.out.println();

        company.setName("UpdatedName");

        try {

            adminService.updateCompany(company);

        } catch (IllegalActionException e) {
            col1 = e.getMessage();
        }

        company.setName("Coca-cola");

        try {
            company.setId(1234);
        } catch (IllegalActionException e) {

            col2 = e.getMessage();
        }


        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Update name", "Update ID").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(col1, col2).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        rend = at.render();
        System.out.println(rend);
        System.out.println();
        System.out.println();

        printTitle("DELETE COMPANY");

        coupon = new Coupon();
        coupon.setTitle("BestCoupon");
        coupon.setAmount(100);
        coupon.setPrice(199);
        coupon.setStart_date(dateUtil.currentDate());
        coupon.setEnd_date(dateUtil.expiredDateFromToday(10));
        coupon.setImage("image.png");
        coupon.setCompany(company);
        coupon.setDescription("Best discount in town!");
        coupon.setCategory(Category.FOOD);


        Customer customer = Customer.builder().email("amitrei@").firstName("amit").lastName("asas").password("123213").coupons(new HashSet<>()).build();

        customer.getCoupons().add(coupon);
        couponRepo.save(coupon);
        customerRepo.save(customer);


        col1=companyRepo.getOne(company.getId()).toString();
        col2 = customerRepo.getOne(customer.getId()).toString();

        adminService.deleteCompany(company.getId());

        String col1After =companyRepo.findAll().toString();
        var col2After = customerRepo.getOne(customer.getId()).toString();


        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Company from DB before delete", "Customers who purchased the coupon").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(col1, col2).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow("Company list from DB After delete","Customer who purchased the coupon").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(col1After,col2After).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        rend = at.render();
        System.out.println(rend);
        System.out.println();
        System.out.println();

        customer.setCoupons(new HashSet<>());
        customerRepo.delete(customer);


        printTitle("GETTING ALL COMPANIES");

        addCompaniesDummy();

        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("All companies from DB").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        for(Company companyFromList : adminService.getAllCompanies()) {
            at.addRow(companyFromList.toString()).setTextAlignment(TextAlignment.CENTER);
            at.addRule();
        }
        rend = at.render();
        System.out.println(rend);
        System.out.println();
        System.out.println();

        printTitle("GET COMPANY BY ID");
        int companyCocaColaId=companyRepo.findByEmail("Coca-cola@gmail.com").getId();
        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Get company from DB by id: " + companyCocaColaId).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(adminService.getOneCompany(companyCocaColaId)).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        rend = at.render();
        System.out.println(rend);
        System.out.println();
        System.out.println();



        printTitle("ADDING NEW CUSTOMER");

        col1=customerRepo.findAll().toString();
        customer = Customer.builder().email("HiItsMoshe@gmail.com").firstName("Moshe").lastName("Cohen").password("123213").coupons(new HashSet<>()).build();
        try {
            adminService.addCustomer(customer);
        }

        catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

        col2=customerRepo.findAll().toString();
        customer = Customer.builder().email("HiItsMoshe@gmail.com").firstName("Moshiko").lastName("Plotke").password("123213").coupons(new HashSet<>()).build();

        try {
            adminService.addCustomer(customer);
        }

        catch (AlreadyExistsException e) {
            col1After=e.getMessage();
        }

        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Before customers list from DB:","After adding customer").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(col1,col2).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(null,"Trying adding another customer with the same email:").setTextAlignment(TextAlignment.CENTER);
        at.addRow(null,col1After).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        rend = at.render();
        System.out.println(rend);
        System.out.println();
        System.out.println();

        printTitle("UPDATE CUSTOMER");
        col1=customerRepo.findAll().toString();
        Customer customer2=customerRepo.findByEmail(customer.getEmail());
        customer2.setFirstName("updatedCustomer");
        customer2.setLastName("updatedCustomer");
        customer2.setEmail("updateCustomer@gmail.com");

        adminService.updateCustomer(customer2);
        col2=customerRepo.findAll().toString();

        try {
            customer2.setId(1231243);
        }
        catch (IllegalActionException e) {
            col1After=e.getMessage();
        }


        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Before customers list from DB:","After updating customer").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(col1,col2).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(null,"Trying update the customer ID:").setTextAlignment(TextAlignment.CENTER);
        at.addRow(null,col1After).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        rend = at.render();
        System.out.println(rend);
        System.out.println();
        System.out.println();

        printTitle("DELETE CUSTOMER");

        /**
         *   making a purchase for the customer
         */

        coupon = Coupon.builder().company(companyRepo.getOne(companyCocaColaId)).price(100)
                .title("Best drink in town").image("cola.png").start_date(dateUtil.currentDate())
                .end_date(dateUtil.expiredDateFromToday(10))
                .description("best coupon ever").amount(15).category(Category.FOOD)
                .build();

        couponRepo.save(coupon);
        customer2.getCoupons().add(coupon);
        customerRepo.saveAndFlush(customer2);


        col1=customerRepo.findAll().toString();
        col2=couponRepo.allPurchases().entrySet().toString();

        adminService.deleteCustomer(customer2.getId());


        col1After=customerRepo.findAll().toString();
        col2After=couponRepo.allPurchases().entrySet().toString();




        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("List of customers from DB","List of All purchases from DB").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(col1,col2).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(null,"After Delete").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(col1After,col2After).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        rend = at.render();
        System.out.println(rend);
        System.out.println();
        System.out.println();






    }












    private void addCompaniesDummy() {

        Company cDummy1= Company.builder().email("Coca-cola@gmail.com").name("Coca-Cola").password("1234").build();
        Company cDummy2= Company.builder().email("McDonalds@gmail.com").name("McDonalds").password("1234").build();
        Company cDummy3= Company.builder().email("HolmesPlaces@gmail.com").name("Holmes-Places").password("1234").build();

        companyRepo.save(cDummy1);
        companyRepo.save(cDummy2);
        companyRepo.save(cDummy3);
    }


    private void printTitle(String title) {
        System.out.println();
        System.out.println("**************************************************************************************       ~          " + title + "              ~          *************************************************************************************************************************************************************************************************");
        System.out.println();

    }

    private void adminTestTitle() {

        System.out.println("                                                                                                                                                                                \n" +
                "                                                                                                                                                                                \n" +
                "                                                                      db      `7MM\"\"\"Yb. `7MMM.     ,MMF'`7MMF'`7MN.   `7MF'    MMP\"\"MM\"\"YMM `7MM\"\"\"YMM   .M\"\"\"bgd MMP\"\"MM\"\"YMM \n" +
                "                                                                     ;MM:       MM    `Yb. MMMb    dPMM    MM    MMN.    M      P'   MM   `7   MM    `7  ,MI    \"Y P'   MM   `7 \n" +
                "                                                                    ,V^MM.      MM     `Mb M YM   ,M MM    MM    M YMb   M           MM        MM   d    `MMb.          MM      \n" +
                "                                                                   ,M  `MM      MM      MM M  Mb  M' MM    MM    M  `MN. M           MM        MMmmMM      `YMMNq.      MM      \n" +
                "                                                                   AbmmmqMA     MM     ,MP M  YM.P'  MM    MM    M   `MM.M           MM        MM   Y  , .     `MM      MM      \n" +
                "                                                                  A'     VML    MM    ,dP' M  `YM'   MM    MM    M     YMM           MM        MM     ,M Mb     dM      MM      \n" +
                "                                                                .AMA.   .AMMA..JMMmmmdP' .JML. `'  .JMML..JMML..JML.    YM         .JMML.    .JMMmmmmMMM P\"Ybmmd\"     .JMML.    \n" +
                "                                                                                                                                                                                \n" +
                "                                                                                                                                                                                ");
    }

    private void companyTestTitle() {
        System.out.println("                                                                                                                                                                                             \n" +
                "                                                                                                                                                                                             \n" +
                "                                                      .g8\"\"\"bgd   .g8\"\"8q. `7MMM.     ,MMF'`7MM\"\"\"Mq.   db      `7MN.   `7MF'`YMM'   `MM'    MMP\"\"MM\"\"YMM `7MM\"\"\"YMM   .M\"\"\"bgd MMP\"\"MM\"\"YMM \n" +
                "                                                    .dP'     `M .dP'    `YM. MMMb    dPMM    MM   `MM. ;MM:       MMN.    M    VMA   ,V      P'   MM   `7   MM    `7  ,MI    \"Y P'   MM   `7 \n" +
                "                                                    dM'       ` dM'      `MM M YM   ,M MM    MM   ,M9 ,V^MM.      M YMb   M     VMA ,V            MM        MM   d    `MMb.          MM      \n" +
                "                                                    MM          MM        MM M  Mb  M' MM    MMmmdM9 ,M  `MM      M  `MN. M      VMMP             MM        MMmmMM      `YMMNq.      MM      \n" +
                "                                                    MM.         MM.      ,MP M  YM.P'  MM    MM      AbmmmqMA     M   `MM.M       MM              MM        MM   Y  , .     `MM      MM      \n" +
                "                                                    `Mb.     ,' `Mb.    ,dP' M  `YM'   MM    MM     A'     VML    M     YMM       MM              MM        MM     ,M Mb     dM      MM      \n" +
                "                                                      `\"bmmmd'    `\"bmmd\"' .JML. `'  .JMML..JMML. .AMA.   .AMMA..JML.    YM     .JMML.          .JMML.    .JMMmmmmMMM P\"Ybmmd\"     .JMML.    \n" +
                "                                                                                                                                                                                             \n" +
                "                                                                                                                                                                                             ");
    }

    private void customerTestTitle() {
        System.out.println("                                                                                                                                                                                              \n" +
                "                                                                                                                                                                                              \n" +
                "                                      .g8\"\"\"bgd `7MMF'   `7MF' .M\"\"\"bgd MMP\"\"MM\"\"YMM   .g8\"\"8q.   `7MMM.     ,MMF'`7MM\"\"\"YMM  `7MM\"\"\"Mq.      MMP\"\"MM\"\"YMM `7MM\"\"\"YMM   .M\"\"\"bgd MMP\"\"MM\"\"YMM \n" +
                "                                    .dP'     `M   MM       M  ,MI    \"Y P'   MM   `7 .dP'    `YM.   MMMb    dPMM    MM    `7    MM   `MM.     P'   MM   `7   MM    `7  ,MI    \"Y P'   MM   `7 \n" +
                "                                    dM'       `   MM       M  `MMb.          MM      dM'      `MM   M YM   ,M MM    MM   d      MM   ,M9           MM        MM   d    `MMb.          MM      \n" +
                "                                    MM            MM       M    `YMMNq.      MM      MM        MM   M  Mb  M' MM    MMmmMM      MMmmdM9            MM        MMmmMM      `YMMNq.      MM      \n" +
                "                                    MM.           MM       M  .     `MM      MM      MM.      ,MP   M  YM.P'  MM    MM   Y  ,   MM  YM.            MM        MM   Y  , .     `MM      MM      \n" +
                "                                    `Mb.     ,'   YM.     ,M  Mb     dM      MM      `Mb.    ,dP'   M  `YM'   MM    MM     ,M   MM   `Mb.          MM        MM     ,M Mb     dM      MM      \n" +
                "                                      `\"bmmmd'     `bmmmmd\"'  P\"Ybmmd\"     .JMML.      `\"bmmd\"'   .JML. `'  .JMML..JMMmmmmMMM .JMML. .JMM.       .JMML.    .JMMmmmmMMM P\"Ybmmd\"     .JMML.    \n" +
                "                                                                                                                                                                                              \n" +
                "                                                                                                                                                                                              ");
    }

    private void initTestTitle() {
        System.out.println("\n" +
                "                                                                                                                                                                                                                   \n" +
                "                                                                                                                                                                                                                   \n" +
                "                `7MMF'    `7MN.   `7MF'    `7MMF'    MMP\"\"MM\"\"YMM     `7MMF'          db          MMP\"\"MM\"\"YMM     `7MM\"\"\"YMM                          MMP\"\"MM\"\"YMM     `7MM\"\"\"YMM       .M\"\"\"bgd     MMP\"\"MM\"\"YMM \n" +
                "                  MM        MMN.    M        MM      P'   MM   `7       MM           ;MM:         P'   MM   `7       MM    `7                          P'   MM   `7       MM    `7      ,MI    \"Y     P'   MM   `7 \n" +
                "                  MM        M YMb   M        MM           MM            MM          ,V^MM.             MM            MM   d                                 MM            MM   d        `MMb.              MM      \n" +
                "                  MM        M  `MN. M        MM           MM            MM         ,M  `MM             MM            MMmmMM                                 MM            MMmmMM          `YMMNq.          MM      \n" +
                "                  MM        M   `MM.M        MM           MM            MM         AbmmmqMA            MM            MM   Y  ,                              MM            MM   Y  ,     .     `MM          MM      \n" +
                "                  MM        M     YMM        MM           MM            MM        A'     VML           MM            MM     ,M                              MM            MM     ,M     Mb     dM          MM      \n" +
                "                .JMML.    .JML.    YM      .JMML.       .JMML.        .JMML.    .AMA.   .AMMA.       .JMML.        .JMMmmmmMMM                            .JMML.        .JMMmmmmMMM     P\"Ybmmd\"         .JMML.    \n" +
                "                                                                                                                                                                                                                   \n" +
                "                                                                                                                                                                                                                   \n");
    }


    private void printCloseTest() {

        System.out.println("\n" +
                "                                                                                                                                                                                                       \n" +
                "                                                                                                                                                                                                       \n" +
                "                      .g8\"\"\"bgd     `7MMF'            .g8\"\"8q.        .M\"\"\"bgd     `7MMF'    `7MN.   `7MF'      .g8\"\"\"bgd                  MMP\"\"MM\"\"YMM     `7MM\"\"\"YMM       .M\"\"\"bgd     MMP\"\"MM\"\"YMM \n" +
                "                    .dP'     `M       MM            .dP'    `YM.     ,MI    \"Y       MM        MMN.    M      .dP'     `M                  P'   MM   `7       MM    `7      ,MI    \"Y     P'   MM   `7 \n" +
                "                    dM'       `       MM            dM'      `MM     `MMb.           MM        M YMb   M      dM'       `                       MM            MM   d        `MMb.              MM      \n" +
                "                    MM                MM            MM        MM       `YMMNq.       MM        M  `MN. M      MM                                MM            MMmmMM          `YMMNq.          MM      \n" +
                "                    MM.               MM      ,     MM.      ,MP     .     `MM       MM        M   `MM.M      MM.    `7MMF'                     MM            MM   Y  ,     .     `MM          MM      \n" +
                "                    `Mb.     ,'       MM     ,M     `Mb.    ,dP'     Mb     dM       MM        M     YMM      `Mb.     MM                       MM            MM     ,M     Mb     dM          MM      \n" +
                "                      `\"bmmmd'      .JMMmmmmMMM       `\"bmmd\"'       P\"Ybmmd\"      .JMML.    .JML.    YM        `\"bmmmdPY                     .JMML.        .JMMmmmmMMM     P\"Ybmmd\"         .JMML.    \n" +
                "                                                                                                                                                                                                       \n" +
                "                                                                                                                                                                                                       \n");
    }
}
