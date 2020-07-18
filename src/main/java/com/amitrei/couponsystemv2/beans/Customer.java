package com.amitrei.couponsystemv2.beans;


import com.amitrei.couponsystemv2.Exceptions.IllegalActionException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor

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


    @ManyToMany(fetch = FetchType.EAGER)
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
