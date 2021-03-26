package com.greenwich.theunibook.controllers;

import com.greenwich.theunibook.enums.UserRole;
import com.greenwich.theunibook.models.Browser;
import com.greenwich.theunibook.services.BrowserService;
import com.greenwich.theunibook.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class BrowserController {

    @Autowired
    BrowserService browserService;

    @Autowired
    UserService userService;

    @GetMapping("/mostUsedBrowser")
    public List<Browser> getMostUsedBrowsers(@RequestParam String email, @RequestParam String password) {


        if (userService.isAuthorized(email, password, UserRole.ADMINISTRATOR)) {

            return browserService.getMostUsedBrowsers();

        }
        return null;

    }


    @PostMapping("/browser")
    public HashMap<String, Object> addBrowser(@RequestParam String browserName){
        return browserService.addBrowser(browserName);
    }
}
