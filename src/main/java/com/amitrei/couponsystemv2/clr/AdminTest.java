package com.amitrei.couponsystemv2.clr;

import com.amitrei.couponsystemv2.beans.Category;
import com.amitrei.couponsystemv2.beans.Company;
import com.amitrei.couponsystemv2.beans.Coupon;
import com.amitrei.couponsystemv2.beans.Customer;
import com.amitrei.couponsystemv2.exceptions.AlreadyExistsException;
import com.amitrei.couponsystemv2.exceptions.IllegalActionException;
import com.amitrei.couponsystemv2.repositories.CompanyRepo;
import com.amitrei.couponsystemv2.repositories.CouponRepo;
import com.amitrei.couponsystemv2.repositories.CustomerRepo;
import com.amitrei.couponsystemv2.security.ClientType;
import com.amitrei.couponsystemv2.security.LoginManager;
import com.amitrei.couponsystemv2.services.AdminService;
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

import java.util.ArrayList;
import java.util.List;


@Component
@Order(1)
public class AdminTest implements CommandLineRunner {

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
         * ADMIN TEST
         *
         */

        templates.adminTestTitle();
        System.out.println();
        templates.printTitle("ADMIN LOGIN");
        System.out.println();
        System.out.println("### Admin Login with hardcoded email & password and getting adminService ");
        var loggedIn = loginManager.login("admin@admin.com", "admin", ClientType.Administrator);
        System.out.println(loggedIn.getClass());
        System.out.println();

        System.out.println("### Admin Login with wrong email & password  ");
        String wrongDetailsAdmin=null;

        try {
            loggedIn = loginManager.login("xxadmin@admin.comxx", "admin", ClientType.Administrator);
        }

        catch (IllegalActionException e) {
            wrongDetailsAdmin=e.getMessage();
        }
        System.out.println(wrongDetailsAdmin);

        // Casting the loginManager into new var to get all methods of adminService
        var loggedAdmin = ((AdminService) loggedIn);


        Company company = Company.builder().name("Coca-cola").email("Coca-cola@gmail.com").password("1234").build();
        Coupon coupon = Coupon.builder().category(Category.SPORT).amount(100).description("BestCouponEver").start_date(dateUtil.currentDate())
                .end_date(dateUtil.expiredDateFromToday(-1)).image("logo.png").title("Coca-cola discount").price(40).company(company).build();
        companyRepo.save(company);
        couponRepo.save(coupon);

        // Deleting the company & coupon that created for thread example
        adminService.deleteCompany(1);
        System.out.println();
        System.out.println();

        String beforeAdded=companyRepo.findAll().toString();
        templates.printTitle("ADDING NEW COMPANY");
         company = Company.builder().name("Coca-cola").email("Coca-cola@gmail.com").password("1234").build();
        adminService.addCompany(company);


        AsciiTable at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Before Adding DB", "After Adding DB").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(beforeAdded,companyRepo.findAll());
        at.addRule();
        String rend = at.render();
        System.out.println(rend);


        // changing email to makesure name is throwing the exception
        company.setEmail("abracadbra@gmail.com");




        String sameNameError=null;
        try {
            adminService.addCompany(company);
        } catch (AlreadyExistsException e) {
            sameNameError=e.getMessage();
        }




        System.out.println();
        company.setEmail("Coca-cola@gmail.com");
        company.setName("changedName");





        String sameEmailError=null;
        try {
            adminService.addCompany(company);
        } catch (AlreadyExistsException e) {
          sameEmailError=e.getMessage();
        }


        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Adding a company with the same name", "Adding the company with the same email").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(sameNameError,sameEmailError).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        rend = at.render();
        System.out.println(rend);



        System.out.println();
        System.out.println();

        // Returning the company to the original name
        company.setName("Coca-cola");


        templates.printTitle("UPDATE COMPANY");


        var col1 = companyRepo.getOne(company.getId()).toString();
        company.setEmail("UpdateEmail@gmail.com");
        company.setPassword("UpdatedPassword");
        adminService.updateCompany(company);
        var col2 = companyRepo.getOne(company.getId()).toString();


        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Before Update from DB", "After Update from DB").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(col1, col2);
        at.addRule();
         rend = at.render();
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

        templates.printTitle("DELETE COMPANY");

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


        Customer customer = Customer.builder().email("amitrei@").firstName("amit").lastName("asas").password("123213").coupons(new ArrayList<>()).build();

        customerRepo.save(customer);
        couponRepo.save(coupon);
        customer.getCoupons().add(coupon);
        customerRepo.saveAndFlush(customer);


        col1 = companyRepo.getOne(company.getId()).toString();
        col2 = couponRepo.allPurchases().entrySet().toString();

        adminService.deleteCompany(company.getId());

        String col1After = companyRepo.findAll().toString();
        var col2After = couponRepo.allPurchases().entrySet().toString();


        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Company from DB before delete", "Customers vs coupons before delete").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(col1,col2).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow("Company list from DB After delete", "Customers vs coupons after delete").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(col1After,col2After).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        rend = at.render();
        System.out.println(rend);
        System.out.println();
        System.out.println();

        customer.setCoupons(new ArrayList<>());
        customerRepo.delete(customer);


        templates.printTitle("GETTING ALL COMPANIES");

        addCompaniesDummy();

        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("All companies from DB").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        for (Company companyFromList : adminService.getAllCompanies()) {
            at.addRow(companyFromList.toString()).setTextAlignment(TextAlignment.CENTER);
            at.addRule();
        }
        rend = at.render();
        System.out.println(rend);
        System.out.println();
        System.out.println();

        templates.printTitle("GET COMPANY BY ID");
        int companyCocaColaId = companyRepo.findByEmail("Coca-cola@gmail.com").getId();
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


        templates.printTitle("ADDING NEW CUSTOMER");

        col1 = customerRepo.findAll().toString();
        customer = Customer.builder().email("HiItsMoshe@gmail.com").firstName("Moshe").lastName("Cohen").password("123213").coupons(new ArrayList<>()).build();
        try {
            adminService.addCustomer(customer);
        } catch (AlreadyExistsException e) {
            System.out.println(e.getMessage());
        }

        col2 = customerRepo.findAll().toString();
        customer = Customer.builder().email("HiItsMoshe@gmail.com").firstName("Moshiko").lastName("Plotke").password("123213").coupons(new ArrayList<>()).build();

        try {
            adminService.addCustomer(customer);
        } catch (AlreadyExistsException e) {
            col1After = e.getMessage();
        }

        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Before customers list from DB:", "After adding customer").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(col1, col2).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(null, "Trying adding another customer with the same email:").setTextAlignment(TextAlignment.CENTER);
        at.addRow(null, col1After).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        rend = at.render();
        System.out.println(rend);
        System.out.println();
        System.out.println();

        templates.printTitle("UPDATE CUSTOMER");
        col1 = customerRepo.findAll().toString();
        Customer customer2 = customerRepo.findByEmail(customer.getEmail());
        customer2.setFirstName("updatedCustomer");
        customer2.setLastName("updatedCustomer");
        customer2.setEmail("updateCustomer@gmail.com");

        adminService.updateCustomer(customer2);
        col2 = customerRepo.findAll().toString();

        try {
            customer2.setId(1231243);
        } catch (IllegalActionException e) {
            col1After = e.getMessage();
        }


        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Before customers list from DB:", "After updating customer").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(col1, col2).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(null, "Trying update the customer ID:").setTextAlignment(TextAlignment.CENTER);
        at.addRow(null, col1After).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        rend = at.render();
        System.out.println(rend);
        System.out.println();
        System.out.println();

        templates.printTitle("DELETE CUSTOMER");

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


        col1 = customerRepo.findAll().toString();
        col2 = couponRepo.allPurchases().entrySet().toString();

        adminService.deleteCustomer(customer2.getId());


        col1After = customerRepo.findAll().toString();
        col2After = couponRepo.allPurchases().entrySet().toString();


        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("List of customers from DB", "List of All purchases from DB").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(col1, col2).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(null, "After Delete").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(col1After, col2After).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        rend = at.render();
        System.out.println(rend);
        System.out.println();
        System.out.println();


        templates.printTitle("GETTING ALL CUSTOMERS");

        addCustomerDummy();
        List<Customer> allCustomers = adminService.getAllCustomers();


        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("List of all customers from DB").setTextAlignment(TextAlignment.CENTER);
        at.addRule();

        for (Customer customer1 : allCustomers) {
            at.addRow(customer1.getId() + " " + customer1.getFirstName() + " " + customer1.getLastName()).setTextAlignment(TextAlignment.CENTER);
            at.addRule();

        }

        rend = at.render();
        System.out.println(rend);

        System.out.println();
        System.out.println();


        templates.printTitle("GETTING CUSTOMER BY ID");
        Customer customerById = adminService.getOneCustomer(4);
        at = new AsciiTable();
        at.getContext().setWidth(200).setFrameLeftMargin(20);
        at.addRule();
        at.addRow("Getting customer from DB by id 4").setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        at.addRow(customerById.getId() + " " + customerById.getFirstName() + " " + customerById.getLastName()).setTextAlignment(TextAlignment.CENTER);
        at.addRule();
        rend = at.render();
        System.out.println(rend);
        System.out.println();
        System.out.println();
        couponRepo.deleteById(coupon.getId());


    }

    private void addCustomerDummy() {


        Customer customerDummy1 = Customer.builder().email("Amitreinich@gmail.com").password("12345").firstName("Amit").lastName("Reinich").build();
        Customer customerDummy2 = Customer.builder().email("SimhaReef@gmail.com").password("12345").firstName("Simha").lastName("Reef").build();
        Customer customerDummy3 = Customer.builder().email("AviRon@gmail.com").password("12345").firstName("Avi").lastName("Ron").build();

        customerRepo.save(customerDummy1);
        customerRepo.save(customerDummy2);
        customerRepo.save(customerDummy3);

    }


    private void addCompaniesDummy() {

        Company cDummy1 = Company.builder().email("Coca-cola@gmail.com").name("Coca-Cola").password("1234").build();
        Company cDummy2 = Company.builder().email("McDonalds@gmail.com").name("McDonalds").password("1234").build();
        Company cDummy3 = Company.builder().email("HolmesPlaces@gmail.com").name("Holmes-Places").password("1234").build();

        companyRepo.save(cDummy1);
        companyRepo.save(cDummy2);
        companyRepo.save(cDummy3);
    }





}
