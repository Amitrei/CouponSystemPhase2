package com.amitrei.couponsystemv2.beans;

import com.amitrei.couponsystemv2.repositories.CompanyRepo;
import com.amitrei.couponsystemv2.repositories.CouponRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude="customers")

public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;



    @Transient
    @Autowired
    private CouponRepo couponRepo;


    @ManyToOne()
    private Company company;

//
    @ManyToMany(mappedBy = "coupons")
    Set<Customer> customers = new HashSet<>();





//
    @Column(nullable = false)
    private Category categoryId;


    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Date start_date;
    @Column(nullable = false)
    private Date end_date;
    @Column(nullable = false)
    private int amount;
    @Column(nullable = false)
    private double price;
    @Column(nullable = false)
    private String image;



    public void setId(int id) {

        System.out.println("CANNOT CHANGE ID");

    }


    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", company=" + company.getId() +
                ", title='" + title + '\'' +
                ", Category : " + categoryId.toString() +
                ", description='" + description + '\'' +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", amount=" + amount +
                ", price=" + price +
                ", image='" + image + '\'' +
                '}';
    }
}
