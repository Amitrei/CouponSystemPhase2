package com.amitrei.couponsystemv2.repositories;

import com.amitrei.couponsystemv2.beans.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompanyRepo extends JpaRepository<Company, Integer> {


    @Query("SELECT COUNT(c) FROM Company c where c.email=:email AND c.password=:password")
    int isCompanyExists(@Param("email") String email, @Param("password") String password);


    boolean existsByEmail(String email);

    Company findByEmail(String email);

    @Query("SELECT c.id FROM Company c where c.name=:companyName")
    int getCompanyIDFromDB(@Param("companyName") String companyName);



}
