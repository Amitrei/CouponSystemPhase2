package com.amitrei.couponsystemv2.services;


import com.amitrei.couponsystemv2.beans.Company;
import com.amitrei.couponsystemv2.beans.Coupon;
import com.amitrei.couponsystemv2.beans.Customer;
import com.amitrei.couponsystemv2.repositories.CustomerRepo;
import com.amitrei.couponsystemv2.security.ClientType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AdminService extends ClientServices {

    private final String ADMIN_EMAIL = "admin@admin.com";
    private final String ADMIN_PASSWORD = "admin";


    @Override
    public boolean login(String email, String password) {
        return (email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD));
    }

    public void addCompany(Company company) {

        for (Company comp : companyRepo.findAll()) {

            if (comp.getName().equals(company.getName()) || comp.getEmail().equals(company.getEmail())) {
                // *********   throw ALREADY EXISTS *********
                System.out.println("ALREADY EXISTS");
                return;

            }

        }
        companyRepo.save(company);

    }


    public void updateCompany(Company company) {

        // Cannot change the company name
        if (!companyRepo.getOne(company.getId()).getName().equals(company.getName())) {
            System.out.println("CANNOT CHANGE NAME!");
            return;
        }
        //CANNOT CHANGE NAME EXCEPTION

        else if (!companyRepo.existsById(company.getId())) {
            System.out.println("NOT EXISTS COMPANY");
        }

        companyRepo.saveAndFlush(company);

    }


    @Transactional
    public void deleteCompany(int companyID) {

        if (!companyRepo.existsById(companyID)) {
            // ******** DOESNOT EXISTS EXCEPTION
            System.out.println("company doesnot exists EXCEPTION");
            return;
        }


        Company thisCompany = companyRepo.getOne(companyID);


        if (thisCompany.getCoupons().size() > 0) {


//O(n)^2
// NEED TO SEPERATE TO 2 DIFFERENTS LOOPS

            for (Coupon companyCoupon : thisCompany.getCoupons()) {

                for (Customer couponOwner : companyCoupon.getCustomers()) {

                    couponOwner.getCoupons().remove(companyCoupon);
                    customerRepo.saveAndFlush(couponOwner);

                }

            }
            companyRepo.delete(thisCompany);
        }
    }


    public List<Company> getAllCompanies() {
        return companyRepo.findAll();
    }


    public Company getOneCompany(int companyId) {
        return companyRepo.getOne(companyId);
    }


    public void addCustomer(Customer customer) {

        if (customerRepo.existsByEmail(customer.getEmail())) {
            System.out.println("ALREADY EXISTS EXCEPTION");
            return;
        }
        customerRepo.save(customer);
    }


    public void updateCustomer(Customer customer) {

        // Checking if ID is changed in the Customer setId()

        customerRepo.saveAndFlush(customer);
    }


    public void deleteCustomer(int customerId) {

        // ManyToMany bidirectional connection will delete all customer purchases

        customerRepo.delete(customerRepo.getOne(customerId));

    }

    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }


    public Customer getOneCustomer(int customerId) {
        return customerRepo.getOne(1);
    }


}


