package com.fpoly.java.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Admin {
    @GetMapping("/admin/home")
    public String HomePage(){
        return "admin/admin";
    }
}
