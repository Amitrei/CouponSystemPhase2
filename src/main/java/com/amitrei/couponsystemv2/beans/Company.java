package com.amitrei.couponsystemv2.beans;

import com.amitrei.couponsystemv2.Exceptions.IllegalActionException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "company", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Coupon> coupons = new ArrayList<>();


    public void setId(int id) throws IllegalActionException {
        if (this.id == 0) {
            this.id = id;
        } else {
            throw new IllegalActionException("cannot change company id");
        }
    }


}
