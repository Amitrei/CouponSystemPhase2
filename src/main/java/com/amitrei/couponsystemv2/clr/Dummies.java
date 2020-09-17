package com.amitrei.couponsystemv2.clr;

import com.amitrei.couponsystemv2.beans.Category;
import com.amitrei.couponsystemv2.beans.Company;
import com.amitrei.couponsystemv2.beans.Coupon;
import com.amitrei.couponsystemv2.beans.Customer;
import com.amitrei.couponsystemv2.repositories.CompanyRepo;
import com.amitrei.couponsystemv2.repositories.CouponRepo;
import com.amitrei.couponsystemv2.repositories.CustomerRepo;
import com.amitrei.couponsystemv2.services.CustomerService;
import com.amitrei.couponsystemv2.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class Dummies implements CommandLineRunner {

    @Autowired
    private DateUtil dateUtil;
    @Autowired
    private CompanyRepo companyRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private CouponRepo couponRepo;


    @Autowired
    private CustomerService customerService;


    @Override
    public void run(String... args) throws Exception {

        Company company = Company.builder().email("Coca-cola@gmail.com").name("Coca-cola").password("12345").coupons(new ArrayList<>()).build();
        Company company2 = Company.builder().email("Mcdonalds@gmail.com").name("McDonalds").password("12345").coupons(new ArrayList<>()).build();
        Company company3 = Company.builder().email("HolmesPlace@gmail.com").name("Holmes Place").password("12345").coupons(new ArrayList<>()).build();
        Company company4 = Company.builder().email("SkiDeal@gmail.com").name("Ski-Deal").password("12345").coupons(new ArrayList<>()).build();
        Company company5 = Company.builder().email("Greg-Cafe@gmail.com").name("Greg-Cafe").password("12345").coupons(new ArrayList<>()).build();
        Company company6 = Company.builder().email("Mega-Sport@gmail.com").name("Megasport").password("12345").coupons(new ArrayList<>()).build();
        Company company7 = Company.builder().email("Tnova@gmail.com").name("Tnova").password("12345").coupons(new ArrayList<>()).build();
        Company company8 = Company.builder().email("Ksp@gmail.com").name("Ksp").password("12345").coupons(new ArrayList<>()).build();
        Company company9 = Company.builder().email("Ikea@gmail.com").name("Ikea").password("12345").coupons(new ArrayList<>()).build();
        Company company10 = Company.builder().email("Castro@gmail.com").name("Castro").password("12345").coupons(new ArrayList<>()).build();


            Customer customer = Customer.builder().firstName("Simha").lastName("Reef").password("1234").email("Simha-Reef@gmail.com").coupons(new ArrayList<>()).build();
            Customer customer2 = Customer.builder().firstName("Amit").lastName("Reinich").password("1234").email("Amitrein@gmail.com").coupons(new ArrayList<>()).build();
            Customer customer3 = Customer.builder().firstName("Avi").lastName("Ron").password("1234").email("AviRon@gmail.com").coupons(new ArrayList<>()).build();
            Customer customer4 = Customer.builder().firstName("Simha").lastName("Reef").password("1234").email("Simha-Reef@gmail.com").coupons(new ArrayList<>()).build();


        Coupon coupon = Coupon.builder().title("New Cola Grapes!").image("coca-cola").price(15).description("Best taste for the cheapest price")
                .start_date(dateUtil.currentDate())
                .end_date(dateUtil.expiredDateFromToday(10))
                .category(Category.FOOD)
                .company(company)
                .amount(100)
                .build();

        Coupon coupon2 = Coupon.builder().title("McAmerica").image("mcdonalds").price(45).description("enjoy the perfect burger on the planet")
                .start_date(dateUtil.currentDate())
                .end_date(dateUtil.expiredDateFromToday(10))
                .category(Category.FOOD)
                .company(company2)
                .amount(75)
                .build();


        Coupon coupon4 = Coupon.builder().title("Mayrhofen Ski package").image("ski-deal").price(300).description("All included package for your perfect vacation")
                .start_date(dateUtil.currentDate())
                .end_date(dateUtil.expiredDateFromToday(10))
                .category(Category.VACATION)
                .company(company4)
                .amount(15)
                .build();
        Coupon coupon3 = Coupon.builder().title("1+1 Gym Membership").image("holmes-place").price(15).description("Sign up today!")
                .start_date(dateUtil.currentDate())
                .end_date(dateUtil.expiredDateFromToday(10))
                .category(Category.SPORT)
                .company(company3)
                .amount(25)
                .build();
        Coupon coupon5 = Coupon.builder().title("Israeli breakfast").image("greg-cafe").price(50).description("Enjoy your morning with a good breakfast!")
                .start_date(dateUtil.currentDate())
                .end_date(dateUtil.expiredDateFromToday(10))
                .category(Category.FOOD)
                .company(company5)
                .amount(56)
                .build();
        Coupon coupon7 = Coupon.builder().title("New Ingredient Milk ").image("tnova").price(5).description("Brand new ingredient for our new milk, you better taste it!")
                .start_date(dateUtil.currentDate())
                .end_date(dateUtil.expiredDateFromToday(10))
                .category(Category.FOOD)
                .company(company7)
                .amount(100)
                .build();
        Coupon coupon6 = Coupon.builder().title("Nike SMax500").image("mega-sport").price(110).description("Run as fast as you can")
                .start_date(dateUtil.currentDate())
                .end_date(dateUtil.expiredDateFromToday(10))
                .category(Category.SPORT)
                .company(company6)
                .amount(45)
                .build();


        Coupon coupon8 = Coupon.builder().title("Lenovo 510G Gamer PC").image("ksp").price(110).description("A prefect gaming experience with the new Lenovo computer.")
                .start_date(dateUtil.currentDate())
                .end_date(dateUtil.expiredDateFromToday(10))
                .category(Category.ELECTRICS)
                .company(company8)
                .amount(45)
                .build();

        Coupon coupon9 = Coupon.builder().title("King size Italian bad").image("ikea").price(345).description("King size Italian bad limited edition")
                .start_date(dateUtil.currentDate())
                .end_date(dateUtil.expiredDateFromToday(10))
                .category(Category.HOME)
                .company(company9)
                .amount(45)
                .build();

        Coupon coupon10 = Coupon.builder().title("New Oversized T Shirt").image("castro").price(60).description("Keep up with the modern style ang grab our new oversized T shirts")
                .start_date(dateUtil.currentDate())
                .end_date(dateUtil.expiredDateFromToday(10))
                .category(Category.CLOTHES)
                .company(company10)
                .amount(14)
                .build();


            companyRepo.saveAll(Arrays.asList(company,company2,company3,company4,company5,company6,company7,company8,company9,company10));
            customerRepo.saveAll(Arrays.asList(customer,customer2,customer3,customer4));
            couponRepo.saveAll(Arrays.asList(coupon,coupon2,coupon3,coupon4,coupon5,coupon6,coupon7,coupon8,coupon9,coupon10));

            customerService.login("Amitrein@gmail.com","1234");
            customerService.purchaseCoupon(coupon);
            customerService.purchaseCoupon(coupon2);
            customerService.purchaseCoupon(coupon3);
            customerService.purchaseCoupon(coupon4);
            customerService.purchaseCoupon(coupon5);



    }
}
