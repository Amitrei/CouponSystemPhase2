package com.amitrei.couponsystemv2.beans;

import com.amitrei.couponsystemv2.exceptions.IllegalActionException;
import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Coupon {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;



//    @JsonIgnore
@JsonBackReference
@ManyToOne()
    private Company company;




//     For Http post request - JSON will include the company name and avoiding dependency circulation exception.
    @Transient
    private String companyName;


    @Column(name = "category_id")
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


//
//    public void setId(int id) throws IllegalActionException {
//        throw new IllegalActionException("cannot change coupons id");
//    }


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
                '}';
    }


}
