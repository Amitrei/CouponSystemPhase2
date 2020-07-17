package com.amitrei.couponsystemv2.repositories;

import com.amitrei.couponsystemv2.beans.Coupon;
import com.amitrei.couponsystemv2.beans.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepo extends JpaRepository<Customer,Integer> {


    boolean existsByEmail(String email);
    boolean existsByEmailAndPassword(String email,String Password);


    @Query(value = "SELECT * FROM customer_coupons WHERE customer_id=:customerId", nativeQuery = true)
    List<Integer> getCustomerCouponsId(@Param("customerId") int customerId);


    Customer findByEmail(String email);

}
