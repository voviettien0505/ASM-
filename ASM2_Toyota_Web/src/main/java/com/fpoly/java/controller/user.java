package com.fpoly.java.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class user {
    @GetMapping("/home")
    public String HomePage(){
        return "user/user";
    }
}
