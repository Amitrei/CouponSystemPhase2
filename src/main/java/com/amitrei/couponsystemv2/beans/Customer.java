package com.amitrei.couponsystemv2.beans;


import com.amitrei.couponsystemv2.Exceptions.IllegalActionException;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder


public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;


    @ManyToMany()
    @JoinTable(name = "customer_vs_coupons")
    private Set<Coupon> coupons = new HashSet<>();


    public void setId(int id) throws IllegalActionException {

        if (this.id == 0) {
            this.id = id;
        } else {
            throw new IllegalActionException("cannot change customer id");
        }
    }


}
