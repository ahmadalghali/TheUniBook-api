package com.greenwich.theunibook.controllers;

import com.greenwich.theunibook.services.IdeaService;
import com.greenwich.theunibook.services.ReportService;
import com.greenwich.theunibook.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class ReportController {

    @Autowired
    ReportService reportService;

    @PostMapping("/reportIdea")
    public HashMap<String,Object> reportIdea(@RequestParam int userId, @RequestParam int reportId, @RequestParam int ideaId){
        return reportService.reportIdea(userId, ideaId, reportId);
    }

}
