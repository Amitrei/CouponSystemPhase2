package com.amitrei.couponsystemv2.security;


import com.amitrei.couponsystemv2.services.ClientServices;
import com.amitrei.couponsystemv2.utils.JwtUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

@Component
@Data
public class TokenManager {

    @Autowired
    private JwtUtil jwtUtil;
    private Map<String, ClientServices> tokens = new HashMap<>();


    public String generateToken(String email, ClientType clientType, ClientServices service) {
        String token = jwtUtil.generateToken(email, clientType);
        tokens.put(token, service);
        return token;
    }

    public boolean isTokenValid(String token) {
        token = token.substring(7);
        return ((!jwtUtil.isTokenExpired(token)) && (tokens.containsKey(token)));
    }


    public boolean deleteToken(String token) {
        return this.tokens.remove(token) != null;
    }



    @Scheduled(fixedRate = 1000*60*30)
    public void deleteExpiredTokens(){
        Iterator<Entry<String,ClientServices>> iterator = tokens.entrySet().iterator();
        while(iterator.hasNext()){
            Entry<String,ClientServices> entry = iterator.next();
            if(jwtUtil.isTokenExpired(entry.getKey()))
                iterator.remove();


        }
}

    public ClientServices getClientService(String token) {
        return tokens.get(token.substring(7));
    }
}
