package com.amitrei.couponsystemv2.beans;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude="coupons")

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
        private Set<Coupon> coupons=new HashSet<>();



        public void setId(int id) {

            if(this.id==0) {
                this.id=id;
            }

            else {
                System.out.println("cannot change id EXCEPTION");
            }
        }

}
