package com.mali.crypfy.gateway.security;

import com.mali.crypfy.gateway.services.user.JWTAuthBuilder;
import com.mali.crypfy.gateway.services.user.impl.JWTAuthAdminBuilderImpl;
import com.mali.crypfy.gateway.services.user.impl.JWTAuthBuilderImpl;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

public class JWTAuthenticationFilter extends GenericFilterBean {

    public static final String HEADER_AUTH = "authorization";
    public static final String TOKEN_PREFIX = "Bearer";

    private JWTAuthBuilder jwtAuthBuilder;
    private JWTAuthBuilder jwtAuthAdminBuilder;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String pathInfo = ((HttpServletRequest) request).getRequestURI();
        boolean isAdminContext = isAdminContext(pathInfo);

        //lazy dependency jwt auth load
        if(jwtAuthBuilder == null || jwtAuthAdminBuilder == null){
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);

            this.jwtAuthBuilder = webApplicationContext.getBean(JWTAuthBuilderImpl.class);
            this.jwtAuthAdminBuilder = webApplicationContext.getBean(JWTAuthAdminBuilderImpl.class);
        }

        String authHeader = httpRequest.getHeader(HEADER_AUTH);

        if(authHeader == null) {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = authHeader.replace(TOKEN_PREFIX,"").trim();
        JWTAuthBuilder jwtAuthBuilderContext = (isAdminContext) ? jwtAuthAdminBuilder : jwtAuthBuilder;

        if(jwtAuthBuilderContext.isTokenValid(token))
            chain.doFilter(request,response);
        else
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED);

    }

    public boolean isAdminContext(String pathInfo) {
        if (pathInfo != null && !pathInfo.equals("") && pathInfo.charAt(0) == '/')
            pathInfo = pathInfo.replaceFirst("/","");
        String[] splited =  pathInfo.split("/");
        return (splited[0].equals("admin")) ? true : false;
    }
}
