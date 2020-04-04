package org.example.cafemanager.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "redirect:login";
    }

    @GetMapping("/login")
    public String login(Authentication authentication) {
        if (null != authentication && authentication.isAuthenticated()) {
            return "redirect:welcome";
        }
        return "login";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/error/access-denied")
    public String accessDenied() {
        return "error/access-denied";
    }
}
