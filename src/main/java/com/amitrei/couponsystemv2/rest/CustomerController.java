package com.amitrei.couponsystemv2.rest;


import com.amitrei.couponsystemv2.beans.*;
import com.amitrei.couponsystemv2.exceptions.IllegalActionException;
import com.amitrei.couponsystemv2.security.ClientType;
import com.amitrei.couponsystemv2.security.LoginManager;
import com.amitrei.couponsystemv2.security.TokenManager;
import com.amitrei.couponsystemv2.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("customer")
@CrossOrigin(origins = "https://couponim-web.herokuapp.com", allowedHeaders = "*")
public class CustomerController extends ClientController {


    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private LoginManager loginManager;


    @PostMapping("login")
    @Override
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            String token = loginManager.restLogin(authRequest.getEmail(), authRequest.getPassword(),
                    ClientType.Customer);
            return new ResponseEntity<AuthResponse>(new AuthResponse(token), HttpStatus.ACCEPTED);

        } catch (IllegalActionException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("purchase-coupon")
    public ResponseEntity<?> purchaseCoupon(@RequestBody Coupon coupon) {
        try {
            ((CustomerService) tokenManager.getClientService(getToken())).purchaseCoupon(coupon);
            System.out.println(((CustomerService) tokenManager.getClientService(getToken())).getCustomerCoupons());

            return new ResponseEntity<Coupon>(coupon, HttpStatus.ACCEPTED);
        } catch (IllegalActionException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }


    }

    @GetMapping("coupons")
    public ResponseEntity<?> getCustomerCoupons() {
        List<Coupon> customerCoupons = ((CustomerService) tokenManager.getClientService(getToken())).getCustomerCoupons();
        customerCoupons.forEach(coupon -> coupon.setCompanyName(coupon.getCompany().getName()));
        customerCoupons.forEach(coupon -> coupon.setIdOfCompany(coupon.getCompany().getId()));
        System.out.println(customerCoupons);
        return new ResponseEntity<List<Coupon>>(customerCoupons, HttpStatus.ACCEPTED);

    }


    @GetMapping("coupons/is-owned/{couponID}")
    public ResponseEntity<?> isCouponOwned(@PathVariable int couponID) {
        return new ResponseEntity<Boolean>(((CustomerService) tokenManager.getClientService(getToken())).isOwnCoupon(couponID), HttpStatus.ACCEPTED);

    }


    @GetMapping("coupons-by-category/{category}")

    public ResponseEntity<?> getCustomerCoupons(@PathVariable Category category) {
        List<Coupon> customerCoupons = ((CustomerService) tokenManager.getClientService(getToken())).getCustomerCoupons(category);
        return new ResponseEntity<List<Coupon>>(customerCoupons, HttpStatus.ACCEPTED);

    }


    @GetMapping("coupons/{maxPrice}")
    public ResponseEntity<?> getCustomerCoupons(@PathVariable Double maxPrice) {
        List<Coupon> customerCoupons = ((CustomerService) tokenManager.getClientService(getToken())).getCustomerCoupons(maxPrice);
        return new ResponseEntity<List<Coupon>>(customerCoupons, HttpStatus.ACCEPTED);

    }

    @GetMapping("details")
    public ResponseEntity<?> getCustomerDetails() {
        Customer currentCustomer = ((CustomerService) tokenManager.getClientService(getToken())).getCustomerDetails();
        return new ResponseEntity<Customer>(currentCustomer, HttpStatus.ACCEPTED);

    }

}
