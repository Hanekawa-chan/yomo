package com.example.demo.controllers;

import com.example.demo.dao.DAO;
import com.example.demo.models.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    Session session = new Session();
    public final DAO dao;
    private boolean trig = false;

    @Autowired
    public MainController(DAO dao) {
        this.dao = dao;
    }

    @GetMapping("/main")
    public String main(Model model){
        model.addAttribute("logout", session.logout);
        return "main";
    }

    @GetMapping("/team")
    public String team(Model model){
        model.addAttribute("logout", session.logout);
        return "team";
    }

    @GetMapping("/stats")
    public String stats(Model model){
        model.addAttribute("logout", session.logout);

        String rate = "#";

        try{
            trig = false;
            rate = dao.main.getInfo().get("tygagamer").toString().substring(0, 4);
        }
        catch(Exception e){
            trig = true;
        }
        model.addAttribute("falseTyga", trig);
        model.addAttribute("tyga", trig ? "#" : rate);

        try{
            trig = false;
            rate = dao.main.getInfo().get("ALExANDROss").toString().substring(0,4);
        }
        catch(Exception e){
            trig = true;
        }
        model.addAttribute("falseIlyan", trig);
        model.addAttribute("ilyan", trig ? "#" : rate);

        try{
            trig = false;
            rate = dao.main.getInfo().get("sorryihavenoname").toString().substring(0,4);
        }
        catch(Exception e){
            trig = true;
        }
        model.addAttribute("falseMeon", trig);
        model.addAttribute("meon", trig ? "#" : rate);

        try{
            trig = false;
            rate = dao.main.getInfo().get("76561198138688894").toString().substring(0,4);
        }
        catch(Exception e){
            trig = true;
        }
        model.addAttribute("falseSalik", trig);
        model.addAttribute("salik", trig ? "#" : rate);

        try{
            trig = false;
            rate = dao.main.getInfo().get("76561198160302549").toString().substring(0,4);
        }
        catch(Exception e){
            trig = true;
        }
        model.addAttribute("falseArchi", trig);
        model.addAttribute("archi", trig ? "#" : rate);

        return "stats";
    }

    @GetMapping("/citats")
    public String citats(Model model){
        model.addAttribute("logout", session.logout);
        return "citats";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("logout", session.logout);
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam(value="login", required = true) String login,
                        @RequestParam(value="pass", required = true) String pass,
                        @RequestParam(value="auth", required = true) String auth){
        session.setName(login);
        session.setPass(pass);
        session.setAuth(auth);
        session.login(dao);
        return "redirect:/main";
    }

    @GetMapping("/logout")
    public String logout() {
        session.logout();
        return "redirect:/main";
    }

    @GetMapping("/refresh")
    public String refresh(){
        if (session.logout) {
            session.refresh(dao);
            return "redirect:/stats";
        }
        else {
            return "redirect:/main";
        }
    }
}