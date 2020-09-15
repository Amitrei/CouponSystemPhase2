package com.amitrei.couponsystemv2.security;


import com.amitrei.couponsystemv2.services.ClientServices;
import com.amitrei.couponsystemv2.utils.JwtUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Data
public class TokenManager {

    @Autowired
    private JwtUtil jwtUtil;
    private Map<String, ClientServices> tokens=new HashMap<>();


    public String generateToken(String email,ClientServices service) {
          String token = jwtUtil.generateToken(email);
          tokens.put(token,service);
          return token;
    }

    public boolean isTokenValid(String token) {
        return ((!jwtUtil.isTokenExpired(token)) && (tokens.containsKey(token)));
    }

    public ClientServices getClientService(String token){
        return tokens.get(token);
    }
}