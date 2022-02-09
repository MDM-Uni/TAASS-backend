package com.petlife.utentemicroservizio.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SimpleCORSFilter {

    /*
    public static final Logger logger = LoggerFactory.getLogger(SimpleCORSFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Initializing CORS");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest requestToUse = (HttpServletRequest) request;
        HttpServletResponse responseToUse = (HttpServletResponse) response;

        responseToUse.setHeader("Access-Control-Allow-Origin", requestToUse.getHeader("Origin"));
        chain.doFilter(requestToUse, responseToUse);

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

     */
}
