package com.greenwich.theunibook.controllers;

import com.greenwich.theunibook.models.Browser;
import com.greenwich.theunibook.services.BrowserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class BrowserController {

    @Autowired
    BrowserService browserService;

    @GetMapping("/mostUsedBrowser")
    public List<Browser> getMostUsedBrowsers(){
        return browserService.getMostUsedBrowsers();
    }


    @PostMapping("/browser")
    public HashMap<String, Object> addBrowser(@RequestParam String browserName){
        return browserService.addBrowser(browserName);
    }
}
