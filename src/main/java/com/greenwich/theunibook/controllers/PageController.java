package com.greenwich.theunibook.controllers;

import com.greenwich.theunibook.enums.UserRole;
import com.greenwich.theunibook.models.Pages;
import com.greenwich.theunibook.services.PageService;
import com.greenwich.theunibook.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class PageController {

    @Autowired
    PageService pageService;

    @Autowired
    UserService userService;

    @GetMapping("/mostViewedPages")
    public HashMap<String, Object> getMostViewedPages(@RequestParam String email, @RequestParam String password) {

        if (userService.isAuthorized(email, password, UserRole.ADMINISTRATOR)) {

            return pageService.getMostViewedPages();


        }
        return null;
    }

    @PostMapping("/addPageView")
    public HashMap<String,Object> addPageView(@RequestParam int pageId){
        return pageService.addPageView(pageId);
    }
}
