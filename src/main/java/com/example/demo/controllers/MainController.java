package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @GetMapping("/main")
    public String main(){
        return "main";
    }

    @GetMapping("/team")
    public String team(){
        return "team";
    }

    @GetMapping("/stats")
    public String stats(){
        return "stats";
    }

    @GetMapping("/citats")
    public String citats(){
        return "citats";
    }

}