package com.mali.crypfy.gateway.services.user.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mali.crypfy.gateway.security.JWTAuthenticationFilter;
import com.mali.crypfy.gateway.services.user.JWTAuthBuilder;
import com.mali.crypfy.gateway.services.user.exceptions.JWTAuthBuilderException;
import com.mali.crypfy.gateway.services.user.json.UserJSON;
import com.mali.crypfy.gateway.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("admin")
public class JWTAuthAdminBuilderImpl implements JWTAuthBuilder {

    final static Logger logger = LoggerFactory.getLogger(JWTAuthBuilderImpl.class);

    public static final String SECRET_TOKEN = "robertoulissesfelipevinicius";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String sign(UserJSON user) throws JWTAuthBuilderException {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_TOKEN);

            Map<String, Object> headerClaims = new HashMap();
            headerClaims.put("owner", "crypfy");

            String userJson = objectMapper.writeValueAsString(user);

            String token = JWT.create().
                    withIssuer("crypfy").withClaim("user",userJson).withExpiresAt(DateUtils.addDaysFromNow(1))
                    .withIssuedAt(new Date()).withHeader(headerClaims)
                    .sign(algorithm);

            return token;
        } catch (UnsupportedEncodingException e) {
            logger.error("error on encode jwt algorithm",e);
            throw new JWTAuthBuilderException("ocorreu um erro no servidor");
        } catch (JsonProcessingException e) {
            logger.error("error on build json",e);
            throw new JWTAuthBuilderException("ocorreu um erro no servidor");
        }
    }

    @Override
    public boolean isTokenValid(String token){
        try {
            token = token.replace(JWTAuthenticationFilter.TOKEN_PREFIX,"").trim();
            Algorithm algorithm = Algorithm.HMAC256(SECRET_TOKEN);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("crypfy")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (UnsupportedEncodingException e) {
            return false;
        } catch (JWTVerificationException exception){
            return false;
        }
    }

    @Override
    public UserJSON getInfo(String token) throws JWTAuthBuilderException {
        try {
            token = token.replace(JWTAuthenticationFilter.TOKEN_PREFIX,"").trim();
            DecodedJWT jwt = JWT.decode(token);
            String userJson = jwt.getClaim("user").asString();
            return objectMapper.readValue(userJson,UserJSON.class);
        } catch (JWTDecodeException e){
            logger.error("invalid token",e);
            throw new JWTAuthBuilderException("Token inválido");
        } catch (JsonParseException e) {
            logger.error("invalid json",e);
            throw new JWTAuthBuilderException("Json inválido");
        } catch (JsonMappingException e) {
            logger.error("invalid json",e);
            throw new JWTAuthBuilderException("Json inválido");
        } catch (IOException e) {
            logger.error("io exception",e);
            throw new JWTAuthBuilderException("Json inválido");
        }
    }
}
