package com.crypfy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class MainController {

    @RequestMapping("/")
    public String getHome() {
        return "home";
    }

    @RequestMapping("/stats")
    public String getStats() {
        return "stats";
    }

    @RequestMapping("/news")
    public String getNews() {
        return "news";
    }

    @RequestMapping("/projections")
    public String getProjections() {
        return "projections";
    }

    @RequestMapping("/simulations")
    public String getSimulations() {
        return "simulations";
    }

    @RequestMapping("/data")
    public String getData() {
        return "data";
    }

    @RequestMapping("/secret")
    public String getSecret() {
        return "secret";
    }

}
