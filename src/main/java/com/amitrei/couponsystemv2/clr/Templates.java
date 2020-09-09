package com.amitrei.couponsystemv2.clr;

import com.amitrei.couponsystemv2.beans.Company;
import com.amitrei.couponsystemv2.beans.Customer;
import com.amitrei.couponsystemv2.repositories.CompanyRepo;
import com.amitrei.couponsystemv2.repositories.CouponRepo;
import com.amitrei.couponsystemv2.repositories.CustomerRepo;
import com.amitrei.couponsystemv2.security.LoginManager;
import com.amitrei.couponsystemv2.services.AdminService;
import com.amitrei.couponsystemv2.services.CompanyService;
import com.amitrei.couponsystemv2.services.CustomerService;
import com.amitrei.couponsystemv2.schedules.CouponExpirationDailyJob;
import com.amitrei.couponsystemv2.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Templates {

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

    public static final String RED = "\033[0;31m";
    public static final String RESET = "\033[0m";
    public static final String WHITE_UNDERLINED = "\033[4;37m";  // WHITE
    public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
    public static final String RED_BOLD = "\033[1;31m";    // RED
    public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
    public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
    public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
    public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
    public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
    public static final String WHITE_BOLD = "\033[1;37m";  // WHITE


    protected void printTitle (String title) {
        System.out.println();
        System.out.println("**************************************************************************************       ~          "+BLACK_BOLD + title + RESET +"              ~          *************************************************************************************************************************************************************************************************");
        System.out.println();

    }

    protected void adminTestTitle() {

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

    protected void companyTestTitle() {
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

    protected void customerTestTitle() {
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

    protected void dailyJobTest() {
        System.out.println("\n" +
                "                                                                                                                                                                                                                      \n" +
                "                                                                                                                                                                                                                      \n" +
                "            `7MM\"\"\"Yb.             db          `7MMF'    `7MMF'          `YMM'   `MM'                   `7MMF'      .g8\"\"8q.       `7MM\"\"\"Yp,             MMP\"\"MM\"\"YMM     `7MM\"\"\"YMM       .M\"\"\"bgd     MMP\"\"MM\"\"YMM \n" +
                "              MM    `Yb.          ;MM:           MM        MM              VMA   ,V                       MM      .dP'    `YM.       MM    Yb             P'   MM   `7       MM    `7      ,MI    \"Y     P'   MM   `7 \n" +
                "              MM     `Mb         ,V^MM.          MM        MM               VMA ,V                        MM      dM'      `MM       MM    dP                  MM            MM   d        `MMb.              MM      \n" +
                "              MM      MM        ,M  `MM          MM        MM                VMMP                         MM      MM        MM       MM\"\"\"bg.                  MM            MMmmMM          `YMMNq.          MM      \n" +
                "              MM     ,MP        AbmmmqMA         MM        MM      ,          MM                          MM      MM.      ,MP       MM    `Y                  MM            MM   Y  ,     .     `MM          MM      \n" +
                "              MM    ,dP'       A'     VML        MM        MM     ,M          MM                     (O)  MM      `Mb.    ,dP'       MM    ,9                  MM            MM     ,M     Mb     dM          MM      \n" +
                "            .JMMmmmdP'       .AMA.   .AMMA.    .JMML.    .JMMmmmmMMM        .JMML.                    Ymmm9         `\"bmmd\"'       .JMMmmmd9                 .JMML.        .JMMmmmmMMM     P\"Ybmmd\"         .JMML.    \n" +
                "                                                                                                                                                                                                                      \n" +
                "                                                                                                                                                                                                                      \n");
    }



    protected void addCustomerDummy() {


        Customer customerDummy1 = Customer.builder().email("Amitreinich@gmail.com").password("12345").firstName("Amit").lastName("Reinich").build();
        Customer customerDummy2 = Customer.builder().email("SimhaReef@gmail.com").password("12345").firstName("Simha").lastName("Reef").build();
        Customer customerDummy3 = Customer.builder().email("AviRon@gmail.com").password("12345").firstName("Avi").lastName("Ron").build();

        customerRepo.save(customerDummy1);
        customerRepo.save(customerDummy2);
        customerRepo.save(customerDummy3);

    }


    protected void addCompaniesDummy() {

        Company cDummy1 = Company.builder().email("Coca-cola@gmail.com").name("Coca-Cola").password("1234").build();
        Company cDummy2 = Company.builder().email("McDonalds@gmail.com").name("McDonalds").password("1234").build();
        Company cDummy3 = Company.builder().email("HolmesPlaces@gmail.com").name("Holmes-Places").password("1234").build();

        companyRepo.save(cDummy1);
        companyRepo.save(cDummy2);
        companyRepo.save(cDummy3);
    }
}
