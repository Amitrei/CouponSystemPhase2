package com.amitrei.couponsystemv2.services;


import com.amitrei.couponsystemv2.exceptions.AlreadyExistsException;
import com.amitrei.couponsystemv2.exceptions.DoesNotExistsException;
import com.amitrei.couponsystemv2.exceptions.IllegalActionException;
import com.amitrei.couponsystemv2.beans.Company;
import com.amitrei.couponsystemv2.beans.Coupon;
import com.amitrei.couponsystemv2.beans.Customer;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Lazy

public class AdminService extends ClientServices {



    private final String ADMIN_EMAIL = "admin@admin.com";
    private final String ADMIN_PASSWORD = "admin";


    @Override
    public boolean login(String email, String password) throws IllegalActionException {

        if(!email.equals(ADMIN_EMAIL)) throw new IllegalActionException("Incorrect email address");
        if(!password.equals(ADMIN_PASSWORD)) throw new IllegalActionException("Incorrect password");

        return true;
    }


    public void addCompany(Company company) throws AlreadyExistsException {

        for (Company comp : companyRepo.findAll()) {

            if (comp.getName().equals(company.getName()))
                throw new AlreadyExistsException("company name");

            if (comp.getEmail().equals(company.getEmail()))
                throw new AlreadyExistsException("company email");


        }


        companyRepo.save(company);

    }


    public void updateCompany(Company company) throws IllegalActionException, DoesNotExistsException {

        if (!companyRepo.existsById(company.getId()))
            throw new DoesNotExistsException("company");

        if (!companyRepo.getOne(company.getId()).getName().equals(company.getName())) {
            throw new IllegalActionException("cannot change company name");
        }

        companyRepo.saveAndFlush(company);

    }


    public void deleteCompany(int companyID) throws DoesNotExistsException {

        if (!companyRepo.existsById(companyID)) throw new DoesNotExistsException("company");
        Company thisCompany = companyRepo.getOne(companyID);


        if (thisCompany.getCoupons().size() > 0) {
            for (Coupon coupon : thisCompany.getCoupons()) { couponRepo.deletePurchase(coupon.getId()); }
        }

        companyRepo.delete(thisCompany);
    }


    public List<Company> getAllCompanies() {
        return companyRepo.findAll();
    }


    public Company getOneCompany(int companyId) {
        return companyRepo.getOne(companyId);
    }


    public void addCustomer(Customer customer) throws AlreadyExistsException {
        if (customerRepo.existsByEmail(customer.getEmail())) throw new AlreadyExistsException("customer");
        customerRepo.save(customer);
    }


    public void updateCustomer(Customer customer) {
        // Checking if ID is changed in the Customer setId()
        customerRepo.saveAndFlush(customer);
    }


    public void deleteCustomer(int customerId) {
        // ManyToMany bi-directional connection will delete all customer purchases
        customerRepo.delete(customerRepo.getOne(customerId));

    }

    public Boolean companyExistsByName(String companyName){
        return this.companyRepo.existsByName(companyName);
    }
    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }
    public Boolean companyExistsByEmail(String email){return this.companyRepo.existsByEmail(email);}
    public boolean customerExistsByEmail(String email){ return this.customerRepo.existsByEmail(email);}


    public Customer getOneCustomer(int customerId) {
        return customerRepo.getOne(customerId);
    }

    public List<Coupon> getAllCoupons(){ return couponRepo.findAll();}
}


