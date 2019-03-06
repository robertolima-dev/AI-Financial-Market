package com.mali.crypfy.gateway.services.user;

import com.mali.crypfy.gateway.services.user.exceptions.JWTAuthBuilderException;
import com.mali.crypfy.gateway.services.user.json.UserJSON;

public interface JWTAuthBuilder {
    public String sign(UserJSON user) throws JWTAuthBuilderException;
    public boolean isTokenValid(String token);
    public UserJSON getInfo(String token) throws JWTAuthBuilderException;
}
