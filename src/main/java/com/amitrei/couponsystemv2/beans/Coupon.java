package com.amitrei.couponsystemv2.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude="customers")

public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @ManyToOne()
    private Company company;

//
    @ManyToMany(mappedBy = "coupons")
    Set<Customer> customers = new HashSet<>();



//
//    @Column(nullable = false)
//    @Enumerated()
//    private Category categoryId;


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


    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", company=" + company.getId() +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", amount=" + amount +
                ", price=" + price +
                ", image='" + image + '\'' +
                '}';
    }
}
