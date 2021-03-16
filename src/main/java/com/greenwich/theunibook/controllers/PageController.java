package com.greenwich.theunibook.controllers;

import com.greenwich.theunibook.models.Pages;
import com.greenwich.theunibook.services.PageService;
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

    @GetMapping("/mostViewedPages")
    public HashMap<String, Object> getMostViewedPages(){
        return pageService.getMostViewedPages();
    }

    @PostMapping("/addPageView")
    public HashMap<String,Object> addPageView(@RequestParam int pageId){
        return pageService.addPageView(pageId);
    }
}
