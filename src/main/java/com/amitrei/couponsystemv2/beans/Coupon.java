package com.amitrei.couponsystemv2.beans;

import com.amitrei.couponsystemv2.Exceptions.IllegalActionException;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(exclude="customers")

public class Coupon {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @ManyToOne()
    private Company company;


    @ManyToMany(mappedBy = "coupons")
    Set<Customer> customers = new HashSet<>();




    @Column(nullable = false,name = "category_id")
    private Category category;


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



    public void setId(int id) throws IllegalActionException {
        throw new IllegalActionException("cannot change coupons id");
    }


    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", company=" + company.getId() +
                ", title='" + title + '\'' +
                ", Category : " + category.toString() +
                ", description='" + description + '\'' +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", amount=" + amount +
                ", price=" + price +
                ", image='" + image + '\'' +
//                "customers= " + customers +
                '}';
    }


}
